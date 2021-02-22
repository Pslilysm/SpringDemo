package pers.cxd.springdemo.socket;

import android.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.cxd.springdemo.bean.account.AccountInfo;
import pers.cxd.springdemo.mapper.AccountMapper;
import pers.cxd.springdemo.service.AccountService;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

@Service
public class ServerSocketService implements ClientStateListener {

    private static ServerSocketService sInstance;

    public static ServerSocketService getInstance(){
        return sInstance;
    }

    final String TAG = ServerSocketService.class.getSimpleName();

    @PostConstruct
    public void init(){
        sInstance = this;
    }

    @Autowired
    AccountService mAccountService;

    private final ExecutorService mExecutor = Executors.newCachedThreadPool();
    private final Map<AccountInfo, ClientMsgReaderWriter> mUiClients = new ConcurrentHashMap<>(); // UI客户端
    private final Map<String, ClientMsgReaderWriter> mMachineClients = new ConcurrentHashMap<>(); // 桩机客户端

    private final Runnable mHeartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "before send heart beat pack, ui clients size = " + mUiClients.size() + ", machine client size = " + mMachineClients.size());
            mUiClients.forEach(new BiConsumer<AccountInfo, ClientMsgReaderWriter>() {
                @Override
                public void accept(AccountInfo accountInfo, ClientMsgReaderWriter clientMsgReaderWriter) {
                    try {
                        clientMsgReaderWriter.sendMsg(SocketMsgType.SERVER_HEART_BEAT, "heart beat");
                    } catch (IOException e) {
                        clientMsgReaderWriter.disconnect();
                    }
                }
            });
            mMachineClients.forEach(new BiConsumer<String, ClientMsgReaderWriter>() {
                @Override
                public void accept(String s, ClientMsgReaderWriter clientMsgReaderWriter) {
                    try {
                        clientMsgReaderWriter.sendMsg(SocketMsgType.SERVER_HEART_BEAT, "heart beat");
                    } catch (IOException e) {
                        clientMsgReaderWriter.disconnect();
                    }
                }
            });
            Log.i(TAG, "after send heart beat pack, ui clients size = " + mUiClients.size() + ", machine client size = " + mMachineClients.size());
        }
    };

    public void accept(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        for (;;){
            Socket socket = serverSocket.accept();
            mExecutor.execute(new ClientMsgReaderWriter(socket, this));
        }
    }

    public AccountInfo getAccountInfoByToken(String token){
        return mAccountService.getAccountInfoByToken(token);
    }

    @Override
    public void onClientConnected(Object clientInfo, ClientMsgReaderWriter clientMsgReaderWriter) {
        if (clientInfo instanceof AccountInfo){
            mUiClients.put((AccountInfo) clientInfo, clientMsgReaderWriter);
        }else if (clientInfo instanceof String){
            mMachineClients.put((String) clientInfo, clientMsgReaderWriter);
        }
    }

    @Override
    public void onClientSendMsg(Object clientInfo, byte msgType, String msg) {
        Log.i(TAG, "onClientSendMsg() called with: clientInfo = [" + clientInfo + "], msgType = [" + msgType + "], msg = [" + msg + "]");
    }

    @Override
    public void onClientDisconnected(Object clientInfo, ClientMsgReaderWriter clientMsgReaderWriter) {
        if (clientInfo instanceof AccountInfo){
            mUiClients.remove(clientInfo);
        }else if (clientInfo instanceof String){
            mMachineClients.remove(clientInfo);
        }
    }

}
