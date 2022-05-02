package com.springclifftop.api;

import com.alibaba.fastjson.JSON;
import com.springclifftop.domain.entity.anki.Params;
import com.springclifftop.domain.model.AnkiRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class AnkiAPI extends BaseAPI {
    /**
     * Anki的端口
     */
    private final static String ANKI_SERVER = "http://127.0.0.1:8765";

    /**
     * 获取牌组名清单
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public String deckNames(){
        var result = createAnkiRequest("deckNames");
        return result;
    }

    /**
     * 创建牌组
     * @param ankiParams
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public void createDeck(Params ankiParams){
        var result = createAnkiRequest(ankiParams,"createDeck");
    }

    /**
     * 获取模板列表
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public String getModelNames() {
        var result = createAnkiRequest("modelNames");
        return result;
    }

    /**
     * 在Anki中创建新的Model
     * @param ankiParams
     */
    public String createModel(Params ankiParams){
       return createAnkiRequest(ankiParams,"createModel");
    }

    /**
     * 向Anki中添加笔记
     * @param ankiParams
     * @throws Exception
     */
    public String addNote(Params ankiParams){
        return createAnkiRequest(ankiParams,"addNotes");
    }

    /**
     * 创建Request,并发送请求
     * 懒得改了,就先用baseAPI吧.
     * @param ankiParams
     * @param action
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    private String createAnkiRequest(Params ankiParams,String action) {
        var ankiRequest = new AnkiRequest(action,6);
        ankiRequest.setParams(ankiParams);
        String data = JSON.toJSONString(ankiRequest);
        String result = null;
        try {
            result = sendPostRequest(data, ANKI_SERVER, "");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }


    private String createAnkiRequest(String action){
        var ankiRequest = new AnkiRequest(action,6);
        String data = JSON.toJSONString(ankiRequest);
        String result = null;
        try {
            result = sendPostRequest(data, ANKI_SERVER, "");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
