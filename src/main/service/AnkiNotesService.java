package main.service;

import main.api.AnkiAPI;
import main.api.SiYuanAPI;
import main.entity.anki.params.Note;
import main.entity.anki.params.Params;
import main.entity.siyuan.SiYuanBlock;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static main.utils.StringUtil.isNotEmpty;

/**
 * TODO Service 因该对应Controller 涉及数据读写的内容应该放入DAO
 */
public class AnkiNotesService {

    /**
     * 获取带有特定属性的块ID
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public static ArrayList<String> getBlocksWithAttr(String customAttr) throws Exception {
        String query = "SELECT * FROM blocks WHERE id in (SELECT block_id FROM attributes AS a  WHERE " +
                "(a.name='custom-" + customAttr + "') GROUP BY block_id )";
        var blocksID = getIDsFromQuery(query);
        return blocksID;
    }

    /**
     * 获取带有特定自定义属性和值的块ID
     * @param customAttr
     * @param customValue
     * @return
     * @throws Exception
     */
    public static ArrayList<String> getBlocksWithAttr(String customAttr,String customValue) throws Exception {
        String query = "SELECT * FROM blocks WHERE id in (SELECT block_id FROM attributes AS a  WHERE " +
                "(a.name='custom-" + customAttr + "' AND a.value='" +customValue+"') GROUP BY block_id ) " +
                "ORDER BY created DESC";
        var blocksID = getIDsFromQuery(query);
        return blocksID;
    }

    /**
     * 执行思源SQLQuery,返回的ID List
     * @param query
     * @return
     * @throws Exception
     */
    private static ArrayList<String> getIDsFromQuery(String query) throws Exception {
        String tempID;
        var blocksID = new ArrayList<String>();
        var blocks = SiYuanAPI.getBlocksOfSQLQuery(query);
        for (int i = 0; i < blocks.size(); i++) {
            tempID = blocks.get(i).getId();
            blocksID.add(tempID);
        }
        System.out.println("BlockID 获取完毕");
        return blocksID;
    }

    /**
     * 获取 ankiinfo 属性的Value 并对其进行处理
     *
     * @param id
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public static Map getValueOfBlocksWithAnki(String id) throws Exception {
        String value = SiYuanAPI.getValueOfSpecificAttr(id, "custom-ankiinfo");
        String[] templist = value.split("\\n");
        var attrMap = new HashMap<String, String>();
        for (int i = 0; i < templist.length; i++) {
            String[] temp = templist[i].split("=");
            attrMap.put(temp[0], temp[1]);
        }
        return attrMap;
    }



}
