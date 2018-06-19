package com.room.mokeys.model;

/**
 * Created by yhh5158 on 2017/5/31.
 */

public class AliAuthInfo extends BaseBean {
    String name;
    String identity_code;
    String auth_url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity_code() {
        return identity_code;
    }

    public void setIdentity_code(String identity_code) {
        this.identity_code = identity_code;
    }

    public String getAuth_url() {
        return auth_url;
    }

    public void setAuth_url(String auth_url) {
        this.auth_url = auth_url;
    }
}
