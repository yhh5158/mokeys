package com.room.mokeys.model;

import java.util.ArrayList;

/**
 * Created by yhh5158 on 2017/5/31.
 *
 ║    "code": 0,
 ║    "msg": "success",
 ║    "data": [
 ║        {
 ║            "houses": [
 ║                {
 ║                    "house_id": "2294465049640145",
 ║                    "room_id": "0",
 ║                    "floor": "11",
 ║                    "number": "1102",
 ║                    "x": "116.434",
 ║                    "y": "39.9244"
 ║                }
 ║            ],
 ║            "estate_name": "测试小区41",
 ║            "estate_x": "116.434",
 ║            "estate_y": "39.9244",
 ║            "heid": "3658003547422549"
 ║        }
 ║    ]
 ║}
 */

public class MokeysEstateDetailInfo extends BaseBean {
    String heid;
    String estate_name;
    double x;
    double y;
    ArrayList<MokeysHouseInfo>houses;

    public String getHeid() {
        return heid;
    }

    public void setHeid(String heid) {
        this.heid = heid;
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

    public ArrayList<MokeysHouseInfo> getHouses() {
        return houses;
    }

    public void setHouses(ArrayList<MokeysHouseInfo> houses) {
        this.houses = houses;
    }
}
