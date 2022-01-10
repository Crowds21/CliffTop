package com.springclifftop.service;

import com.springclifftop.api.SYNewAPI;
import com.springclifftop.api.SiYuanAPI;
import com.springclifftop.domain.entity.siyuan.SiYuanBlock;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.springclifftop.utils.StringUtil.isNotEmpty;

@Component
public class SiYuanService {

    @Autowired
    private SYNewAPI siYuanAPI;

    @Value("${siyuan.notebook}")
    private String notebook;
    @Value("${siyuan.project}")
    private String project;




    public Object[] lsNoteBooks() {
        siYuanAPI.lsNoteBooks();
        return null;
    }

    /**
     * 返回带有特定K-V的块
     * @param customAttr
     * @param customValue
     * @return
     */
    public ArrayList<SiYuanBlock> getBlocksWithAttr(String customAttr, String customValue) {
        String query = "SELECT * FROM blocks WHERE id in (SELECT block_id FROM attributes AS a  WHERE " +
                "(a.name='custom-" + customAttr + "' AND a.value='" +customValue+"') GROUP BY block_id ) " +
                "ORDER BY created DESC";
        var blocks = siYuanAPI.sqlQuery(query);
        return blocks;
    }

    public ArrayList<SiYuanBlock> getChildrenBlocks(String parentID) {
        var noteBlocks = new ArrayList<SiYuanBlock>();
        String query = "SELECT * FROM blocks WHERE parent_id='" + parentID + "' ORDER BY created DESC";
        //获取其全部子块放入
        var temp = siYuanAPI.sqlQuery(query);
        for (int j = 0; j < temp.size(); j++) {
            if (isNotEmpty(temp.get(j).getContent()))
                noteBlocks.add(temp.get(j));
        }
        return noteBlocks;
    }

    public boolean setValueOfBlockAttr(String id, String attrName, String attrValue) {
        var siyuanRequest = new HashMap<String,Object>();
        var attrMap = new HashMap<>();
        attrMap.put(attrName, attrValue);
        if (siYuanAPI.setBlockAttrs(id,attrMap))
            return true;
        return false;
    }

    public String getValueOfSpecificAttr(String id, String attrName) {
        Map<String,String> siyuanAttrs = siYuanAPI.getBlockAttrs(id);
        return siyuanAttrs.get(attrName);
    }

    public String createDocWithMd(String notebook, String path, String markdown) {
        return siYuanAPI.createDocWithMd(notebook, path, markdown);
    }

    /**
     * 获取Project数据
     * @return
     */
    public ArrayList<Map> getProjects() {
//        String sql = "SELECT * FROM blocks WHERE " +
//                "path like '%/" + project + "/______________-_______.sy' " +
//                "and type = 'd'";
//        ArrayList<SiYuanBlock> blocks = siYuanAPI.sqlQuery(sql);
        return null;
    }

    public ArrayList<Map> getInbox() {
//        var blocksWithAttrs = new ArrayList<Map>();
//        var blocks = siYuanAPI
//                .getBlocksWithAttrInSpecificPath("dataview", "20210911211409-pdpsdws");
//        //获取块属性
//        for (int i = 0; i < blocks.size(); i++) {
//            blocksWithAttrs.add(siYuanAPI.getAttrsOfBlock(blocks.get(i).getId()));
//        }
//        return blocksWithAttrs;
        return null;
    }

    /**
     * 获取Task标签,并对数据进行处理
     * TODO 加入文档名
     * TODO 可以再拆分,拆分成获取属性表和获取内容表
     *
     * @return
     */
    public ArrayList<Map> getTasks(String columName) {
        var tableRows = new ArrayList<Map>();
        var blocks = getBlocksStartWithMD("#TODO%#",project);
        for (int i = 0; i < blocks.size(); i++) {
            var tempMap = new HashMap<String, String>();
            String taskContent = blocks.get(i).getContent();
            tempMap.put("id", blocks.get(i).getId());

            if (!taskContent.equals("#TODO#") && taskContent.contains("\n")) {
                String[] contentList = taskContent.split("\n");
                tempMap.put(columName, contentList[0]);
                tableRows.add(tempMap);
            } else if (!taskContent.equals("#TODO#") && !taskContent.contains("\n")) {
                tempMap.put(columName, taskContent);
                tableRows.add(tempMap);
            }
        }
        return tableRows;
    }

    /**
     * 获取Markdown中包含特定内容的块
     * 该方法目前专供SiYuanController中的TaskView
     * @param md
     * @return
     */
    private  ArrayList<SiYuanBlock> getBlocksStartWithMD(String md,String pathID){
        String query = "SELECT * FROM blocks WHERE type='p' AND markdown LIKE '" + md + "%' " +
                "and path LIKE'%" + pathID + "%' " +
                "ORDER BY content DESC LIMIT -1";
        var blocks = siYuanAPI.sqlQuery(query);
        return blocks;
    }

    /**
     * 打开文档
     *
     * @param id
     */
    public void openSiYuan(String id) {
        CommandLine cmdLine = new CommandLine("open");
        cmdLine.addArgument("${blockLink}");
        Map map = new HashMap();
        map.put("blockLink", "siyuan://blocks/" + id);
        cmdLine.setSubstitutionMap(map);
        // Handler 用于实现异步操作??
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        //ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
        DefaultExecutor executor = new DefaultExecutor();

        //executor.setWatchdog(watchdog);
        executor.setExitValue(0);
        try {
            executor.execute(cmdLine, resultHandler);
            //resultHandler.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
