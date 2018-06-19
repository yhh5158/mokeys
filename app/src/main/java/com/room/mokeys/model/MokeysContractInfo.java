package com.room.mokeys.model;

/**
 * Created by yhh5158 on 2017/5/31.
 * "contract": {
 ║            "id": "2",
 ║            "cid": "9462696335768538",
 ║            "uid": "0",
 ║            "hid": "3110999576107449",
 ║            "rid": "1",
 ║            "type": "1",
 ║            "status": "1",
 ║            "amount": "2510",
 ║            "house_pledge": "2510",
 ║            "service_bill": "200",
 ║            "sign_img_url": "",
 ║            "create_time": "2017-07-10 18:02:22"
 ║        },
 ║
 */

public class MokeysContractInfo extends BaseBean {
    String id;
    String cid;
    String uid;
    String hid;
    String rid;
    String type;
    String status;
    String amount;
    String house_pledge;
    String service_bill;
    String sign_img_url;
    String create_time;
    String sign_expire_time;

    public String getSign_expire_time() {
        return sign_expire_time;
    }

    public void setSign_expire_time(String sign_expire_time) {
        this.sign_expire_time = sign_expire_time;
    }

    String update_time;
    String signed_contract;
    String estate;
    MokeysHouseInfo house;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHouse_pledge() {
        return house_pledge;
    }

    public void setHouse_pledge(String house_pledge) {
        this.house_pledge = house_pledge;
    }

    public String getService_bill() {
        return service_bill;
    }

    public void setService_bill(String service_bill) {
        this.service_bill = service_bill;
    }

    public String getSign_img_url() {
        return sign_img_url;
    }

    public void setSign_img_url(String sign_img_url) {
        this.sign_img_url = sign_img_url;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getSigned_contract() {
        return signed_contract;
    }

    public void setSigned_contract(String signed_contract) {
        this.signed_contract = signed_contract;
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
}
