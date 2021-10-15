package main.controller;

import main.api.AnkiAPI;
import main.api.SiYuanAPI;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static main.service.AnkiNotesService.*;
import static main.service.AnkiService.addAnkiNotesBasedOnAttr;

public class AnkiNotesController {

    /**
     * 生成笔记
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public static Boolean createNotes() throws Exception {
        //获取带有Anki属性且为True
        //获取父块,处理其属性,处理完毕后更改属性Anki改为false
        //获取下一个父块,处理属性
        ArrayList<String> blocksID = getBlocksWithAttr("anki","true");
        for (int i = 0; i < blocksID.size(); i++) {
            String parentID = blocksID.get(i);
            //获取父块的其他属性
            var attrMap = getValueOfBlocksWithAnki(parentID);
            //获取子块列表
            var childBlocks = SiYuanAPI.getChildrenBlocks(parentID );
            //基于属性,发送请求
            addAnkiNotesBasedOnAttr(attrMap, childBlocks);
            //设置完毕更改状态
            SiYuanAPI.setValueOfBlockAttr(parentID,"custom-anki","false");
        }
        return true;
    }

    /**
     * 获取Anki中的所有Model信息
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public static String getModelInfo() throws URISyntaxException, IOException, InterruptedException {
        var result = AnkiAPI.getModelNames();
        System.out.println(result);
        return null;
    }
}
