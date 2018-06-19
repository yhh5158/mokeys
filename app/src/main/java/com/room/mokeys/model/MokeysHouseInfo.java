package com.room.mokeys.model;

/**
 * Created by yhh5158 on 2017/5/31.
 * houses": [
 ║                {
 ║                    "house_id": "2294465049640145",
 ║                    "room_id": "0",
 ║                    "floor": "11",
 ║                    "number": "1102",
 ║                    "x": "116.434",
 ║                    "y": "39.9244"
 ║                }
 */

public class MokeysHouseInfo extends BaseBean {
    String house_id;
    String room_id;
    String floor;
    String number;
    String rent_type;
    double x;

    public String getRent_type() {
        return rent_type;
    }

    public void setRent_type(String rent_type) {
        this.rent_type = rent_type;
    }

    double y;

    public String getHouse_id() {
        return house_id;
    }

    public void setHouse_id(String house_id) {
        this.house_id = house_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
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
