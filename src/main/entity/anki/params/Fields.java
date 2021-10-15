package main.entity.anki.params;

import com.alibaba.fastjson.annotation.JSONField;

public class Fields {
    @JSONField(name="Front",ordinal = 1)
    String front;
    @JSONField(name="Back",ordinal = 2)
    String back;
    @JSONField(name="Backup",ordinal = 3)
    String backup;


    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }
}
