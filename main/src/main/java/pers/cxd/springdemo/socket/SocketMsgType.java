package pers.cxd.springdemo.socket;

public interface SocketMsgType {

    byte APP_CLIENT_CONNECT = 0;
    byte WEB_CLIENT_CONNECT = 1;
    byte MACHINE_CLIENT_CONNECT = 2;

    byte SERVER_HEART_BEAT = 3;

}
