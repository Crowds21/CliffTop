package main.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import main.entity.siyuan.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static main.utils.StringUtil.isNotEmpty;

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
     * 获取带有特定属性值的块ID
     * @param customAttr
     * @param customValue
     * @return
     * @throws Exception
     */
    public static ArrayList<SiYuanBlock> getBlocksWithAttr(String customAttr,String customValue) throws Exception {

        String query = "SELECT * FROM blocks WHERE id in (SELECT block_id FROM attributes AS a  WHERE " +
                "(a.name='custom-" + customAttr + "' AND a.value='" +customValue+"') GROUP BY block_id ) " +
                "ORDER BY created DESC";
        var blocksID = getBlocksOfSQLQuery(query);
        return blocksID;
    }

    /**
     * 获取带有特定是属性的块的ID
     * @param customAttr
     * @return
     * @throws Exception
     */
    public static ArrayList<SiYuanBlock> getBlocksWithAttr(String customAttr) throws Exception {
        String query = "SELECT * FROM blocks WHERE id in (SELECT block_id FROM attributes AS a  WHERE " +
                "(a.name='custom-" + customAttr + "') GROUP BY block_id )";
        var blocksID =  getBlocksOfSQLQuery(query);
        return blocksID;
    }

    /**
     * 获取传入的块的子块
     * 对于空行则不会放入list中
     *
     * @param parentID
     */
    public static ArrayList<SiYuanBlock> getChildrenBlocks(String parentID) throws URISyntaxException, IOException, InterruptedException {
        var noteBlocks = new ArrayList<SiYuanBlock>();
        String query = "SELECT * FROM blocks WHERE parent_id='" + parentID + "' ORDER BY created DESC";
        //获取其全部子块放入
        var temp = SiYuanAPI.getBlocksOfSQLQuery(query);
        for (int j = 0; j < temp.size(); j++) {
            if (isNotEmpty(temp.get(j).getContent()))
                noteBlocks.add(temp.get(j));
        }
        return noteBlocks;
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
    public static String createDocWithMd(String notebook,String path, String markdown) throws URISyntaxException, IOException, InterruptedException {
        Map<String,Object> siyuanRequest = new HashMap<>();
        siyuanRequest.put("notebook",notebook);
        siyuanRequest.put("path",path);
        siyuanRequest.put("markdown",markdown);
        String data = toSiYuanJSON( siyuanRequest);
        String result = sendPostRequest(data,SY_SERVER, "/api/filetree/createDocWithMd");
        // 转换为Json对象
        JSONObject jsonObject=JSON.parseObject(result);
        return jsonObject.getString("data");
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
