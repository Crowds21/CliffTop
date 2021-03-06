package main.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import main.entity.anki.AnkiRequest;
import main.entity.anki.AnkiResponse;
import main.entity.anki.params.Note;
import main.entity.anki.params.Params;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public static String deckNames() throws URISyntaxException, IOException, InterruptedException {
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
    public static void createDeck(Params ankiParams) throws URISyntaxException, IOException, InterruptedException {
        var result = createAnkiRequest(ankiParams,"createDeck");
    }

    /**
     * 获取模板列表
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public static String getModelNames() throws URISyntaxException, IOException, InterruptedException {
        var result = createAnkiRequest("modelNames");
        return result;
    }

    /**
     * 在Anki中创建新的Model
     * @param ankiParams
     */
    public static void createModel(Params ankiParams) throws URISyntaxException, IOException, InterruptedException {
        var result = createAnkiRequest(ankiParams,"createModel");
    }

    /**
     * 向Anki中添加笔记
     * @param ankiParams
     * @throws Exception
     */
    public static void addNote(Params ankiParams) throws Exception {
        var result = createAnkiRequest(ankiParams,"addNotes");
    }

    /**
     * 创建Request,并发送请求
     * @param ankiParams
     * @param action
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    private static String createAnkiRequest(Params ankiParams,String action) throws URISyntaxException, IOException, InterruptedException {
        var ankiRequest = new AnkiRequest(action,6);
        ankiRequest.setParams(ankiParams);
        String data = JSON.toJSONString(ankiRequest);
        var result = sendPostRequest(data, ANKI_SERVER, "");
        return result;
    }

    private static String createAnkiRequest(String action) throws URISyntaxException, IOException, InterruptedException {
        var ankiRequest = new AnkiRequest(action,6);
        String data = JSON.toJSONString(ankiRequest);
        var result = sendPostRequest(data, ANKI_SERVER, "");
        return result;
    }
}
