package com.springclifftop.controller;

import com.springclifftop.api.AnkiAPI;
import com.springclifftop.domain.entity.siyuan.SiYuanBlock;
import com.springclifftop.service.AnkiService;
import com.springclifftop.service.SiYuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ShellComponent
public class AnkiController {

    @Autowired
    AnkiService ankiService;

    @Autowired
    SiYuanService siYuanService;


    /**
     * 生成笔记
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    @ShellMethod(key ="cn",value = "Create Anki Note.")
    public Boolean createNotes(){
        //获取带有Anki属性且为True
        ArrayList<SiYuanBlock> blocks = siYuanService.getBlocksWithAttr("anki","true");
        for (int i = 0; i < blocks.size(); i++) {
            String parentID = blocks.get(i).getId();
            var attrMap = getValueOfBlocksWithAnki(parentID);
            var childBlocks = siYuanService.getChildrenBlocks(parentID );
            // TODO 此处应该返回成功添加的数据数,并在循环末尾一起输出
            ankiService.addAnkiNotesBasedOnAttr(attrMap, childBlocks);
            //设置完毕更改状态

            LocalDateTime today = LocalDateTime.now();
            var formatTimeString = today.format(DateTimeFormatter.BASIC_ISO_DATE);
            siYuanService.setValueOfBlockAttr(parentID,"anki",formatTimeString);
        }
        return true;
    }

    /**
     * 获取 ankiinfo 属性的Value 并对其进行处理
     * TODO 处理有多行值的属性,可能在以后需要放入siYuanAPI中
     * @param id
     * @return
     * @throws Exception
     */
    public Map getValueOfBlocksWithAnki(String id){
        String value = siYuanService.getValueOfSpecificAttr(id, "custom-ankiinfo");
        String[] templist = value.split("\\n");
        var attrMap = new HashMap<String, String>();
        for (int i = 0; i < templist.length; i++) {
            String[] temp = templist[i].split("=");
            attrMap.put(temp[0], temp[1]);
        }
        return attrMap;
    }

    /**
     * 获取Anki中的所有Model信息
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public String getModelInfo() throws URISyntaxException, IOException, InterruptedException {
        var result = ankiService.getModelNames();
        System.out.println(result);
        return null;
    }

}
