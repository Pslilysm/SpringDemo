package pers.cxd.springdemo.bean;

import pers.cxd.springdemo.config.HttpCode;

public class CommonResp<D> {

    int code;
    String msg;
    D data;

    private CommonResp(int code, String msg, D data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
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

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    public static <D> CommonResp<D> create(int code, String msg, D data){
        return new CommonResp<>(code, msg, data);
    }

    public static <D> CommonResp<D> createWithOk(String msg, D data){
        return create(HttpCode.Common.OK, msg, data);
    }

}
