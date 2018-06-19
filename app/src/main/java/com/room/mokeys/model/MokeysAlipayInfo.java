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

public class MokeysAlipayInfo extends BaseBean {
    String alipay;

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }
}
