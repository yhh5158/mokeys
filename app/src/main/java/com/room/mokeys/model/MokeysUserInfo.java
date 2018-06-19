package com.room.mokeys.model;

import java.util.ArrayList;

/**
 * Created by yhh5158 on 2017/5/31.
 */

public class MokeysUserInfo extends BaseBean {
    String id;
    String uid;
    String name;
    String identify_code;
    String mobile;
    String nickname;
    String avatar;
    String avatar_small;
    String status;
    ArrayList<MokeysReservesInfo> reserves;
    ArrayList<MokeysContractInfo> contracts;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentify_code() {
        return identify_code;
    }

    public void setIdentify_code(String identify_code) {
        this.identify_code = identify_code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_small() {
        return avatar_small;
    }

    public void setAvatar_small(String avatar_small) {
        this.avatar_small = avatar_small;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<MokeysReservesInfo> getReserves() {
        return reserves;
    }

    public void setReserves(ArrayList<MokeysReservesInfo> reserves) {
        this.reserves = reserves;
    }

    public ArrayList<MokeysContractInfo> getContracts() {
        return contracts;
    }

    public void setContracts(ArrayList<MokeysContractInfo> contracts) {
        this.contracts = contracts;
    }
}
