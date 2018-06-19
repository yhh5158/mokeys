package com.room.mokeys.model;

/**
 * Created by yhh5158 on 2017/5/31.
 */

public class AliCheckAuthInfo extends BaseBean {
    boolean auth_passed;

    public boolean isAuth_passed() {
        return auth_passed;
    }

    public void setAuth_passed(boolean auth_passed) {
        this.auth_passed = auth_passed;
    }
}
