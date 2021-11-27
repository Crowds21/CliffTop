package com.springclifftop.service;

import com.springclifftop.api.SiYuanAPI;
import com.springclifftop.common.Constant;
import com.springclifftop.domain.entity.siyuan.SiYuanBlock;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class SiYuanService {

    @Autowired
    private SiYuanAPI siYuanAPI;

    public ArrayList<SiYuanBlock> getBlocksWithAttr(String customAttr, String customValue) {
        return siYuanAPI.getBlocksWithAttr(customAttr, customValue);
    }

    public ArrayList<SiYuanBlock> getChildrenBlocks(String parentID) {
        return siYuanAPI.getChildrenBlocks(parentID);
    }

    public void setValueOfBlockAttr(String id, String attrName,String attrValue){
        siYuanAPI.setValueOfBlockAttr(id,"custom-"+attrName,attrValue);
    }

    public  String getValueOfSpecificAttr(String id, String attrName){
        return siYuanAPI.getValueOfSpecificAttr(id,attrName);
    }

    public  String createDocWithMd(String notebook,String path, String markdown){
        return siYuanAPI.createDocWithMd(notebook,path,markdown);
    }

    /**
     * 获取Project数据
     *
     * @return
     */
    public ArrayList<Map> getProjects() {
        var blocksWithAttrs = new ArrayList<Map>();
        var blocks = siYuanAPI
                .getBlocksWithAttrInSpecificPath("dataview","20210911205530-1dxsxsh");
        //获取块属性
        for (int i = 0; i < blocks.size(); i++) {
            blocksWithAttrs.add(siYuanAPI.getAttrsOfBlock( blocks.get(i).getId() ));
        }
        return blocksWithAttrs;
    }

    public ArrayList<Map> getInbox(){
        var blocksWithAttrs = new ArrayList<Map>();
        var blocks = siYuanAPI
                .getBlocksWithAttrInSpecificPath("dataview","20210911211409-pdpsdws");
        //获取块属性
        for (int i = 0; i < blocks.size(); i++) {
            blocksWithAttrs.add(siYuanAPI.getAttrsOfBlock( blocks.get(i).getId() ));
        }
        return blocksWithAttrs;
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
        var blocks = siYuanAPI.getBlocksStartWithMD("#TODO%#");
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
     * 打开文档
     * @param id
     */
    public void openSiYuan(String id){
        CommandLine cmdLine = new CommandLine("siyuan");
        cmdLine.addArgument("${blockLink}");
        Map map = new HashMap();
        map.put("blockLink","siyuan://blocks/"+id);
        cmdLine.setSubstitutionMap(map);
        // Handler 用于实现异步操作??
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        //ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
        DefaultExecutor executor = new DefaultExecutor();

        //executor.setWatchdog(watchdog);
        executor.setExitValue(0);
        try {
            executor.execute(cmdLine,resultHandler);
            //resultHandler.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
