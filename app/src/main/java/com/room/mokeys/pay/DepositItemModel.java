package com.room.mokeys.pay;

import java.io.Serializable;

/**
 * Created by WangChang on 2016/4/1.
 */
public class DepositItemModel implements Serializable {

    public int amount;
    public int time;

    public DepositItemModel(int money, int time) {
        this.amount = money;
        this.time = time;
    }

    public int getMoney() {
        return amount;
    }

    public void setMoney(int money) {
        this.amount = money;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
