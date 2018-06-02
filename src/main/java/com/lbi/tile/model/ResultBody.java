package com.lbi.tile.model;


import java.io.Serializable;

/**
 * @author  jiazhijie on 2017/6/29.
 */
public class ResultBody<T> implements Serializable {

    private static final long serialVersionUID = 6207021438300520567L;
    private boolean success=false;
    private Integer errcode = -1;
    private String errmsg = "";
    private T data = null;

    public ResultBody(){

    }

    /**
     * 成功返回
     *
     * @param data
     */
    public ResultBody(T data) {
        this.success=true;
        this.errcode = 0;
        this.errmsg = "success";
        this.data = data;
    }

    public ResultBody(int errcode, String errmsg) {
        this.success=true;
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public ResultBody(int errcode, String errmsg, T data) {
        this.success=true;
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.data = data;
    }

    public ResultBody(boolean success, int errcode, String errmsg, T data) {
        this.success=success;
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.data = data;
    }

    public boolean getSuccess(){
        return success;
    }

    public void setSuccess(boolean success){
        this.success=success;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }



}
