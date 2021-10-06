package main.entity.siyuan;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SiYuanRespMap {
    @JSONField(name = "code")
    private int code;
    @JSONField(name = "msg")
    private String msg;
    @JSONField(name = "data")
    private Map<String,Object> data;

    public SiYuanRespMap() {
        data = new HashMap();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
