package com.springclifftop.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.springclifftop.domain.entity.siyuan.NoteBook;
import com.springclifftop.domain.entity.siyuan.SiYuanBlock;
import com.springclifftop.domain.model.SiYuanRespBlocks;
import com.springclifftop.domain.model.SiYuanRespMap;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import static com.springclifftop.utils.StringUtil.isNotEmpty;

@Component
public class SiYuanAPI extends BaseAPI{
    private final  String SY_SERVER = "http://127.0.0.1:6806";

    /**
     * 获取笔记本信息
     * @return
     * @throws Exception
     */
    public  ArrayList<NoteBook>  getNotebooks() throws Exception {
        String result = sendGetRequest(SY_SERVER,"/api/notebook/lsNotebooks");
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
    public  Map<String,Object> getAttrsOfBlock(String id) {
        String data;
        Map<String,Object> siyuanRequest = new HashMap<>();
        siyuanRequest.put("id",id);
        data = toSiYuanJSON( siyuanRequest);

        String result = null;
        try {
            result = sendPostRequest(data,SY_SERVER, "/api/attr/getBlockAttrs");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
    public  String getValueOfSpecificAttr(String id, String attrName){
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
    public  String setValueOfBlockAttr(String id, String attrName,String attrValue){
        var siyuanRequest = new HashMap<String,Object>();
        var attrMap = new HashMap<>();
        attrMap.put(attrName, attrValue);

        siyuanRequest.put("id",id);
        siyuanRequest.put("attrs",attrMap);
        var data = toSiYuanJSON( siyuanRequest);
        String result = null;
        try {
            result = sendPostRequest(data,SY_SERVER, "/api/attr/setBlockAttrs");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //将JSON数据转化为 RequestResult 对象
        return result;
    }

    /**
     * 获取带有特定属性值的块
     * @param customAttr
     * @param customValue
     * @return
     * @throws Exception
     */
    public  ArrayList<SiYuanBlock> getBlocksWithAttr(String customAttr, String customValue){

        String query = "SELECT * FROM blocks WHERE id in (SELECT block_id FROM attributes AS a  WHERE " +
                "(a.name='custom-" + customAttr + "' AND a.value='" +customValue+"') GROUP BY block_id ) " +
                "ORDER BY created DESC";
        var blocksID = getBlocksOfSQLQuery(query);
        return blocksID;
    }

    /**
     * 获取带有特定是属性的块
     * @param customAttr
     * @return
     * @throws Exception
     */
    public  ArrayList<SiYuanBlock> getBlocksWithAttr(String customAttr){
        String query = "SELECT * FROM blocks WHERE id in (SELECT block_id FROM attributes AS a  WHERE " +
                "(a.name='custom-" + customAttr + "') GROUP BY block_id )";
        var blocks =  getBlocksOfSQLQuery(query);
        return blocks;
    }

    public  ArrayList<SiYuanBlock> getBlocksWithAttrInSpecificPath(String customAttr,String path){
        String query = "SELECT * FROM blocks WHERE " +
                "path LIKE '%" + path + "%'AND " +
                "id in (SELECT block_id FROM attributes AS a  WHERE " +
                "(a.name='custom-" + customAttr + "') GROUP BY block_id )";
        var blocks =  getBlocksOfSQLQuery(query);
        return blocks;
    }

    /**
     * 获取传入的块的子块
     * 对于空行则不会放入list中
     *
     * @param parentID
     */
    public  ArrayList<SiYuanBlock> getChildrenBlocks(String parentID){
        var noteBlocks = new ArrayList<SiYuanBlock>();
        String query = "SELECT * FROM blocks WHERE parent_id='" + parentID + "' ORDER BY created DESC";
        //获取其全部子块放入
        var temp = getBlocksOfSQLQuery(query);
        for (int j = 0; j < temp.size(); j++) {
            if (isNotEmpty(temp.get(j).getContent()))
                noteBlocks.add(temp.get(j));
        }
        return noteBlocks;
    }

    /**
     * 获取Markdown中包含特定内容的块
     * 该方法目前专供SiYuanController中的TaskView
     * @param md
     * @return
     */
    public  ArrayList<SiYuanBlock> getBlocksStartWithMD(String md){
        String query = "SELECT * FROM blocks WHERE type='p' AND markdown LIKE '" + md + "%' ORDER BY content DESC LIMIT -1";
        var blocks = getBlocksOfSQLQuery(query);
        return blocks;
    }

    /**
     * 根据Markdown创建笔记
     * @param notebook
     * @param path
     * @param markdown
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public  String createDocWithMd(String notebook,String path, String markdown){
        Map<String,Object> siyuanRequest = new HashMap<>();
        siyuanRequest.put("notebook",notebook);
        siyuanRequest.put("path",path);
        siyuanRequest.put("markdown",markdown);
        String data = toSiYuanJSON( siyuanRequest);
        String result = null;
        try {
            result = sendPostRequest(data,SY_SERVER, "/api/filetree/createDocWithMd");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 转换为Json对象
        JSONObject jsonObject=JSON.parseObject(result);
        return jsonObject.getString("data");
    }


    /**
     * 执行SQL查询 返回 BlockList
     * @param sqlQuery
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public  ArrayList<SiYuanBlock> getBlocksOfSQLQuery(String sqlQuery){
        Map<String,Object> siyuanRequest = new HashMap<>();
        siyuanRequest.put("stmt",sqlQuery);
        String data = toSiYuanJSON( siyuanRequest);

        String result = null;
        try {
            result = sendPostRequest(data,SY_SERVER, "/api/query/sql");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SiYuanRespBlocks blocks = JSON.parseObject(result, SiYuanRespBlocks.class);
        return blocks.getData();
    }


    /**
     * 将Map转换为JSON字符串
     * @param siyuanRequest
     * @return
     */
    private  String toSiYuanJSON( Map<String,Object> siyuanRequest ){
        JSONObject ankiRequestJSON = new JSONObject(siyuanRequest);
        String data = ankiRequestJSON.toJSONString();
        return data;
    }
}
