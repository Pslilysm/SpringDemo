package pers.cxd.springdemo.bean;

public class ErrorResp<D> {

    int errorCode;
    String errorMsg;
    D errorData;

    private ErrorResp(int errorCode, String errorMsg, D errorData) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.errorData = errorData;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public D getErrorData() {
        return errorData;
    }

    public static <D> ErrorResp<D> create(int errorCode, String errorMsg, D errorData){
        return new ErrorResp<>(errorCode, errorMsg, errorData);
    }

}
