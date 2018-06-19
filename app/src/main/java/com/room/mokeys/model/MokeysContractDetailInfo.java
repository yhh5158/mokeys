package com.room.mokeys.model;

/**
 * Created by yhh5158 on 2017/5/31.
 * "contract": {
 ║            "id": "2",
 ║            "cid": "9462696335768538",
 ║            "uid": "0",
 ║            "hid": "3110999576107449",
 ║            "rid": "1",
 ║            "type": "1",
 ║            "status": "1",
 ║            "amount": "2510",
 ║            "house_pledge": "2510",
 ║            "service_bill": "200",
 ║            "sign_img_url": "",
 ║            "create_time": "2017-07-10 18:02:22"
 ║        },
 ║ "contract_url": "http:\/\/baidu.com"
 */

public class MokeysContractDetailInfo extends BaseBean {
    MokeysContractInfo contract;
    String contract_url;

    public MokeysContractInfo getContract() {
        return contract;
    }

    public void setContract(MokeysContractInfo contract) {
        this.contract = contract;
    }

    public String getContract_url() {
        return contract_url;
    }

    public void setContract_url(String contract_url) {
        this.contract_url = contract_url;
    }
}
