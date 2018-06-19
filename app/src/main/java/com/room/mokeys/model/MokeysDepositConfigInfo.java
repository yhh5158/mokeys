package com.room.mokeys.model;

import com.room.mokeys.pay.DepositItemModel;

import java.util.ArrayList;


public class MokeysDepositConfigInfo extends BaseBean {
    ArrayList<DepositItemModel>deposits;

    public ArrayList<DepositItemModel> getDeposits() {
        return deposits;
    }

    public void setDeposits(ArrayList<DepositItemModel> deposits) {
        this.deposits = deposits;
    }
}
