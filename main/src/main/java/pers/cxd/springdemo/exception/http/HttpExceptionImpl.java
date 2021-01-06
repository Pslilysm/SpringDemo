package pers.cxd.springdemo.exception.http;

import org.springframework.http.HttpStatus;

public class HttpExceptionImpl extends RuntimeException implements HttpException {

    private final int code;
    private final String msg;

    private HttpExceptionImpl(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }

    public static HttpExceptionImpl create(int code, String msg){
        return new HttpExceptionImpl(code, msg);
    }

    public static HttpExceptionImpl create(HttpStatus code, String msg){
        return new HttpExceptionImpl(code.value(), msg);
    }

}
