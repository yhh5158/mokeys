package com.room.mokeys.model;

import java.util.ArrayList;


public class MokeysunLocklInfo extends BaseBean {
    ArrayList<MokeysHouseDetailInfo>houses;
    public ArrayList<MokeysHouseDetailInfo> getHouses() {
        return houses;
    }

    public void setHouses(ArrayList<MokeysHouseDetailInfo> houses) {
        this.houses = houses;
    }
}
