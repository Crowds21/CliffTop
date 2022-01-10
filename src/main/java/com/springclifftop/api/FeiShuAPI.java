package com.springclifftop.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.springclifftop.domain.entity.feishu.*;
import com.springclifftop.domain.entity.feishu.bitable.BiProjectFields;
import com.springclifftop.domain.entity.feishu.bitable.BitableApp;
import com.springclifftop.domain.entity.feishu.bitable.BiRecord;
import com.springclifftop.domain.entity.feishu.bitable.BitableData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@RestController
public class FeiShuAPI extends BaseAPI{

    @Value("${feishu.appID}")
    private String appID;

    @Value("${feishu.appSecret}")
    private String appSecret;

    @Value("${feishu.appToken}")
    private String appToken;

    @Value("${feishu.tableID}")
    private String tableID;

    public String getTenantAccessToken(){
        String url= "https://open.feishu.cn/open-apis/auth/v3/app_access_token/internal";
        HashMap<String,String> requestMap = new HashMap();
        requestMap.put("app_id",appID);
        requestMap.put("app_secret",appSecret);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AccessTokenResponse> responseEntity =
                restTemplate.postForEntity(url,requestMap, AccessTokenResponse.class);
        System.out.println(responseEntity.getBody().getTenant_access_token());
        return responseEntity.getBody().getTenant_access_token();
    }

    /**
     * get BiTable app_token
     * @param tenantAccessToken
     * @return
     */
    public BitableApp getBiTableAppToken(String tenantAccessToken){
        String url= "https://open.feishu.cn/open-apis/bitable/v1/apps/";

        //Head
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ tenantAccessToken );

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity httpEntity = new HttpEntity<>(null,headers);
        ResponseEntity<FeiShuBitableResponse> responseEntity = restTemplate.exchange(url+appToken
                , HttpMethod.GET
                ,httpEntity
                ,FeiShuBitableResponse.class);
        System.out.println(responseEntity.getBody().getData());
        BitableApp bitableApp = responseEntity.getBody().getData().getApp();

        System.out.println(bitableApp.getName());
        return bitableApp;
    }


    public BitableData listBitableRecords(String tenantAccessToken){
        String url = "https://open.feishu.cn/open-apis/bitable/v1/apps/" + appToken +
                "/tables/" + tableID +"/records";
        HttpEntity httpEntity = setHttpEntity(null,tenantAccessToken);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<FeiShuBitableResponse> responseEntity = restTemplate.exchange(url
                ,HttpMethod.GET
                ,httpEntity
                ,FeiShuBitableResponse.class);
        BitableData data = responseEntity.getBody().getData();
        return data;
    }

    public String addBitableRecord(BiProjectFields fields,String tenantAccessToken){
        String url = "https://open.feishu.cn/open-apis/bitable/v1/apps/"+ appToken
                + "/tables/" + tableID +"/records";
        HashMap<String,Object> record = new HashMap<>();
        record.put("fields",fields);
        HttpEntity httpEntity = setHttpEntity(record,tenantAccessToken);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);
        JSONObject json = JSONObject.parseObject(responseEntity.getBody());
        JSONObject data = (JSONObject) json.get("data");
        JSONObject getRecord = (JSONObject) data.get("record");
        String recordID = getRecord.getString("record_id");
        return recordID;
    }

    public boolean updateBitableRecord(BiProjectFields fields
            , String recordID, String tenantAccessToken){
        String url = "https://open.feishu.cn/open-apis/bitable/v1/apps/" + appToken +
                "/tables/"+ tableID + "/records/" + recordID;
        HashMap<String,Object> record = new HashMap<>();
        record.put("fields",fields);

        HttpEntity httpEntity = setHttpEntity(record,tenantAccessToken);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<FeiShuBitableResponse> responseEntity = restTemplate.exchange(url
                ,HttpMethod.PUT
                ,httpEntity
                ,FeiShuBitableResponse.class);
        // 我服了,文档是假的，命名返回的只有一个Success
        String code = responseEntity.getBody().getCode();
        return identifyCode(code);
    }

    /**
     * 设置HttpEntity
     * @param request
     * @return
     */
    private HttpEntity setHttpEntity(Object request,String tenantAccessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ tenantAccessToken);
        headers.add("Content-Type","application/json; charset=utf-8");
        HttpEntity httpEntity = new HttpEntity<>(request,headers);
        return httpEntity;
    }

    /**
     * 返回Boolean
     * @param code
     * @return
     */
    private boolean identifyCode(String code){
        return code.equals("0");
    }
}
