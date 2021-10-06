package main.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import main.entity.siyuan.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SiYuanAPI extends BaseAPI{
    private final static String SY_SERVER = "http://127.0.0.1:6806";

    /**
     * 获取笔记本信息
     * @return
     * @throws Exception
     */
    public static ArrayList<NoteBook>  getNotebooks() throws Exception {
        String result = sendGetRequest(SY_SERVER,"/api/notebook/lsNotebooks");

        //ArrayList<NoteBook> noteBooks = JSON.parseObject(result,SiYuanResp.class).getData().getNoteBooks();
        //return noteBooks;
        return  null;
    }

    /**
     * 获取块的属性
     * @param id
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public static Map<String,Object> getAttrsOfBlock(String id) throws URISyntaxException, IOException, InterruptedException {
        String data;
        Map<String,Object> siyuanRequest = new HashMap<>();
        siyuanRequest.put("id",id);
        data = toSiYuanJSON( siyuanRequest);

        String result = sendPostRequest(data,SY_SERVER, "/api/attr/getBlockAttrs");
        //将JSON数据转化为 RequestResult 对象
        SiYuanRespMap blocks = JSON.parseObject(result, SiYuanRespMap.class);
        return blocks.getData();
    }

    /**
     * 获取块的指定属性的值
     * @param id
     * @param attrName
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public static String getValueOfSpecificAttr(String id, String attrName) throws Exception {
        Map<String,Object> siyuanAttrs = getAttrsOfBlock(id);
        return siyuanAttrs.get(attrName).toString();
    }

    /**
     * 给特定块设置属性
     * @param id
     * @param attrName
     * @param attrValue
     * @return
     * @throws Exception
     */
    public static String setValueOfBlockAttr(String id, String attrName,String attrValue) throws Exception {
        var siyuanRequest = new HashMap<String,Object>();
        var attrMap = new HashMap<>();
        attrMap.put(attrName, attrValue);

        siyuanRequest.put("id",id);
        siyuanRequest.put("attrs",attrMap);
        var data = toSiYuanJSON( siyuanRequest);
        String result = sendPostRequest(data,SY_SERVER, "/api/attr/setBlockAttrs");
        //将JSON数据转化为 RequestResult 对象
        return result;
    }

    /**
     * 执行SQL查询
     * /main.api/query/sql
     * @param sqlQuery
     */
    public static ArrayList<SiYuanBlock> getBlocksOfSQLQuery(String sqlQuery)
            throws URISyntaxException, IOException, InterruptedException {
        Map<String,Object> siyuanRequest = new HashMap<>();
        siyuanRequest.put("stmt",sqlQuery);
        String data = toSiYuanJSON( siyuanRequest);

        String result = sendPostRequest(data,SY_SERVER, "/api/query/sql");
        SiYuanRespBlocks blocks = JSON.parseObject(result, SiYuanRespBlocks.class);
        return blocks.getData();
    }


    /**
     * 将Map转换为JSON字符串
     * @param siyuanRequest
     * @return
     */
    private static String toSiYuanJSON( Map<String,Object> siyuanRequest ){
        JSONObject ankiRequestJSON = new JSONObject(siyuanRequest);
        String data = ankiRequestJSON.toJSONString();
        return data;
    }
}
