package com.room.mokeys.model;

/**
 * Created by yhh5158 on 2017/5/31.
 ║                    "id": "1",
 ║                    "hid": "3570523470816859",
 ║                    "rid": "0",
 ║                    "x": "116.347513",
 ║                    "y": "40.020542",
 ║                    "latitude": "40.020542",
 ║                    "longtitude": "116.347513",
 ║                    "heid": "1395685229370468",
 ║                    "country": "中国",
 ║                    "province": "北京",
 ║                    "city": "北京市",
 ║                    "county": "海淀区",
 ║                    "floor": "5",
 ║                    "number": "501",
 ║                    "reserve_expire_time": "0",
 ║                    "status": "1",
 ║                    "create_time": "2017-07-10 15:10:17",
 ║                    "update_time": "2017-07-10 15:10:17
 */

public class MokeysHouseDetailInfo extends BaseBean {
    String id;
    String hid;
    String rid;
    double x;
    double y;
    double latitude;
    double longtitude;
    String heid;
    String country;
    String province;
    String city;
    String county;
    String floor;
    String number;
    String reserve_expire_time;
    String status;
    String create_time;
    String update_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getHeid() {
        return heid;
    }

    public void setHeid(String heid) {
        this.heid = heid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReserve_expire_time() {
        return reserve_expire_time;
    }

    public void setReserve_expire_time(String reserve_expire_time) {
        this.reserve_expire_time = reserve_expire_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
