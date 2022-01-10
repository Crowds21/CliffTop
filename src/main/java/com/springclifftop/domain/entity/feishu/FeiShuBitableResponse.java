package com.springclifftop.domain.entity.feishu;

import com.springclifftop.domain.entity.feishu.bitable.BitableData;

public class FeiShuBitableResponse {

    String msg;
    String code;
    BitableData data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BitableData getData() {
        return data;
    }

    public void setData(BitableData data) {
        this.data = data;
    }
}
