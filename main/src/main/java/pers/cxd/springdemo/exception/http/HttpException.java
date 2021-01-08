package pers.cxd.springdemo.exception.http;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {

    private final int mHttpCode;
    private final Object mErrorBody;

    public HttpException(int mHttpCode, Object mErrorBody) {
        this.mHttpCode = mHttpCode;
        this.mErrorBody = mErrorBody;
    }

    public int httpCode() {
        return mHttpCode;
    }

    public Object errorBody() {
        return mErrorBody;
    }

    public static HttpException create(int httpCode, Object errorBody){
        return new HttpException(httpCode, errorBody);
    }

    public static HttpException create(HttpStatus status, Object errorBody){
        return create(status.value(), errorBody);
    }

}
