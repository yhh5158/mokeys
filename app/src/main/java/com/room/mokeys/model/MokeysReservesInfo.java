package com.room.mokeys.model;

/**
 * Created by yhh5158 on 2017/5/31.
 *    "reserves": [
 ║                {
 ║                    "id": "6",
 ║                    "uid": "9929689234364993",
 ║                    "hid": "3110999576107449",
 ║                    "rid": "1",
 ║                    "amount": "50",
 ║                    "create_time": "2017-07-24 15:29:01",
 ║                    "expire_time": "1500902941"
 ║                }
 ║            ]
 */

public class MokeysReservesInfo extends BaseBean {
    String id;
    String uid;
    String hid;
    String rid;
    String amount;
    String create_time;
    String expire_time;
    String estate;
    MokeysHouseInfo house;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public String getEstate() {
        return estate;
    }

    public void setEstate(String estate) {
        this.estate = estate;
    }

    public MokeysHouseInfo getHouse() {
        return house;
    }

    public void setHouse(MokeysHouseInfo house) {
        this.house = house;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }
}
