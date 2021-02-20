package pers.cxd.springdemo.socket;

public interface ClientStateListener {

    void onClientConnected(Object clientInfo, ClientMsgReaderWriter clientMsgReaderWriter);

    void onClientSendMsg(Object clientInfo, byte msgType, String msg);

    void onClientDisconnected(Object clientInfo, ClientMsgReaderWriter clientMsgReaderWriter);

}
