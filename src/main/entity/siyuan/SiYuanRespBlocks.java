package main.entity.siyuan;

import com.alibaba.fastjson.annotation.JSONField;
import main.entity.siyuan.SiYuanBlock;

import java.util.ArrayList;

public class SiYuanRespBlocks {

    @JSONField(name = "code")
    private int code;
    @JSONField(name = "msg")
    private String msg;
    @JSONField(name = "data")
    private ArrayList<SiYuanBlock> data;


    @Override
    public String toString() {
        return "main.entity.RequestResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
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

    public ArrayList<SiYuanBlock> getData() {
        return data;
    }

    public void setData(ArrayList<SiYuanBlock>   data) {
        this.data = data;
    }
}
