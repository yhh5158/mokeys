package com.room.mokeys.model;

/**
 * Created by yhh5158 on 2017/5/9.
 *
 *  "id": 2,
 "objectId": 1,
 "owerId": 2222,
 "type": 1,
 "status": 1,
 "createTime": 1494827951000,
 "activity": null,
 "column": null
 */

public class NeighbourhoodRoomInfo {
    int id;
    int type;
    String artspringuserId;
    String roomdetail;
    int status;
    long createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getArtspringuserId() {
        return artspringuserId;
    }

    public void setArtspringuserId(String artspringuserId) {
        this.artspringuserId = artspringuserId;
    }

    public String getRoomdetail() {
        return roomdetail;
    }

    public void setRoomdetail(String roomdetail) {
        this.roomdetail = roomdetail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


}
