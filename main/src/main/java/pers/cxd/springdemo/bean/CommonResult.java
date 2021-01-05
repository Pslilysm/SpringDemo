package pers.cxd.springdemo.bean;

public class CommonResult<T> {

    int code;
    String msg;
    T data;

    CommonResult<?> next;

    private CommonResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private static CommonResult<?> sPool;
    private static final int sMaxPoolSize = 100_000;
    private static int sPoolSize;


    public static <T> CommonResult<T> obtain(int code, String msg, T data){
        synchronized (CommonResult.class){
            if (sPool != null){
                CommonResult<T> t = (CommonResult<T>) sPool;
                sPool = t.next;
                t.next = null;
                t.code = code;
                t.msg = msg;
                t.data = data;
                sPoolSize--;
                return t;
            }
        }
        return new CommonResult<>(code, msg, data);
    }

    public void recycle(){
        msg = null;
        data = null;
        synchronized (CommonResult.class){
            if (sPoolSize < sMaxPoolSize){
                next = sPool;
                sPool = this;
                sPoolSize++;
            }
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
