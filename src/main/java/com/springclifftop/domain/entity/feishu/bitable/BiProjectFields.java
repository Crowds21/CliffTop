package com.springclifftop.domain.entity.feishu.bitable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public class BiProjectFields {
    //TODO 传入多种对象，如何区分。
    // 直接写死
    String name;
    Boolean sync;
    String state;
    String priority;
    Long deadLine;
    String blockid;
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm")
    String area;
    String memo;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSync() {
        return sync;
    }
    public void setSync(Boolean sync) {
        this.sync = sync;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getBlockid() {
        return blockid;
    }

    public void setBlockid(String blockid) {
        this.blockid = blockid;
    }

    public Long getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Long deadLine) {
        this.deadLine = deadLine;
    }

    public String getObjective() {
        return area;
    }

    public void setObjective(String area) {
        this.area = area;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
