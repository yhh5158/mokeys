package com.room.mokeys.model;

/**
 * Created by yhh5158 on 2017/5/31.
 * ║            "heid": "8822042186070165",
 ║            "count": "51",
 ║            "estate_name": "测试小区41"
            "x": "116.434",
 ║            "y": "39.9244"
 ║
 */

public class MokeysEstateInfo extends BaseBean {
    String heid;
    int  count;
    String estate_name;
    double x;
    double y;

    public String getHeid() {
        return heid;
    }

    public void setHeid(String heid) {
        this.heid = heid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getEstate_name() {
        return estate_name;
    }

    public void setEstate_name(String estate_name) {
        this.estate_name = estate_name;
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
}
