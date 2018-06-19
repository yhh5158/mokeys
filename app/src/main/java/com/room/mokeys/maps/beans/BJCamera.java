package com.room.mokeys.maps.beans;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yhh5158 on 2017/4/1.
 */

@Entity(nameInDb  = "BJCamera")
public class BJCamera extends BaseBean {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longtitude;
    private String direction;

    public BJCamera() {
    }

    @Generated(hash = 1850170789)
    public BJCamera(Long id, String name, String address, Double latitude, Double longtitude,
            String direction) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.direction = direction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
