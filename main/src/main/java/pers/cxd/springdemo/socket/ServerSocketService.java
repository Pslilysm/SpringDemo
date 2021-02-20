package pers.cxd.springdemo.socket;

import pers.cxd.springdemo.bean.account.AccountInfo;

import java.io.*;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSocketService implements ClientStateListener {

    private static class SingletonHolder {
        private static final ServerSocketService sInstance = new ServerSocketService();
    }

    public static ServerSocketService getInstance(){
        return SingletonHolder.sInstance;
    }

    private final ExecutorService mExecutor = Executors.newCachedThreadPool();
    private final Map<AccountInfo, ClientMsgReaderWriter> mUiClients = new ConcurrentHashMap<>(); // UI客户端
    private final Map<String, ClientMsgReaderWriter> mMachineClients = new ConcurrentHashMap<>(); // 桩机客户端

    public void accept(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        for (;;){
            mExecutor.execute(new ClientMsgReaderWriter(serverSocket.accept(), this));
        }
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
