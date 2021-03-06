package com.room.mokeys.model;

import cn.droidlover.xdroidmvp.net.IModel;

/**
 * Created by wanglei on 2016/12/11.
 */

public class MokeysBaseModel<T> implements IModel {
    int code;
    String msg;
    T data;

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

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isAuthError() {
        return false;
    }

    @Override
    public boolean isBizError() {
        return false;
    }


    @Override
    public String getErrorMsg() {
        return null;
    }
}
