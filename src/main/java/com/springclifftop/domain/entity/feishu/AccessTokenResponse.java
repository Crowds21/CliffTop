package com.springclifftop.domain.entity.feishu;

public class AccessTokenResponse {
    String code;
    String msg;
    String tenant_access_token;
    String expire;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTenant_access_token() {
        return tenant_access_token;
    }

    public void setTenant_access_token(String tenant_access_token) {
        this.tenant_access_token = tenant_access_token;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

}
