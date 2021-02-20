package pers.cxd.springdemo.socket;

import android.util.Log;
import pers.cxd.springdemo.bean.account.AccountInfo;
import pers.cxd.springdemo.util.Pair;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientMsgReaderWriter implements Runnable{

    final String TAG = ClientMsgReaderWriter.class.getSimpleName();

    final Socket mSocket;
    DataInputStream mDis;
    DataOutputStream mDos;

    Object mClientInfo;
    final ClientStateListener mListener;

    public ClientMsgReaderWriter(Socket socket, ClientStateListener listener) {
        this.mSocket = socket;
        this.mListener = listener;
        Log.i(TAG, "received a new Client -> " + socket.getRemoteSocketAddress().toString());
    }

    @Override
    public void run() {
        try {
            mDis = new DataInputStream(new BufferedInputStream(mSocket.getInputStream()));
            mDos = new DataOutputStream(new BufferedOutputStream(mSocket.getOutputStream()));
            Pair<Byte, String> clientInfoPair = readDataOnce();
            Log.i(TAG, "run: get client info = " + clientInfoPair);
            if (clientInfoPair.first() == SocketMsgType.APP_CLIENT_CONNECT){
                String token = clientInfoPair.second();
                mClientInfo = ServerSocketService.getInstance().getAccountInfoByToken(token);
            }else if (clientInfoPair.first() == SocketMsgType.WEB_CLIENT_CONNECT){
                //
            } else if (clientInfoPair.first() == SocketMsgType.MACHINE_CLIENT_CONNECT){
                mClientInfo = clientInfoPair.second();
            } else {
                disconnect();
                return;
            }
        } catch (IOException ex) {
            // when connection disconnected
            Log.e(TAG, "run: ", ex);
            disconnect();
            return;
        }
        mListener.onClientConnected(mClientInfo, this);
        for (;;){
            try {
                Pair<Byte, String> msgPair = readDataOnce();
                mListener.onClientSendMsg(mClientInfo, msgPair.first(), msgPair.second());
            } catch (IOException e) {
                Log.e(TAG, "run: ", e);
                disconnect();
                mListener.onClientDisconnected(mClientInfo, this);
                return;
            }
        }
    }

    public void disconnect(){
        Log.d(TAG, "disconnect() called");
        try {
            mSocket.close();
        } catch (IOException ex) {
            Log.e(TAG, "disconnect: ", ex);
        }
    }

    private Pair<Byte, String> readDataOnce() throws IOException{
        int packLength = mDis.readInt();
        byte msgType = (byte) mDis.read();
        byte[] data = new byte[packLength];
        int ret = mDis.read(data);
        if (ret < 0){
            throw new IOException();
        }
        return new Pair<>(msgType, new String(data, StandardCharsets.UTF_8));
    }

    public void sendMsgIgnoreIOEx(byte msgType, String msg){
        try {
            sendMsg(msgType, msg);
        } catch (IOException e) {
            disconnect();
        }
    }

    public void sendMsg(byte msgType, String msg) throws IOException{
        DataOutputStream dos = mDos;
        if (dos != null){
            int packLength = msg.length();
            dos.writeInt(packLength);
            dos.write(msgType);
            dos.write(msg.getBytes());
            dos.flush();
        }
    }

}
