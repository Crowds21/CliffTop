package main.service;

import main.api.AnkiAPI;
import main.entity.anki.params.Note;
import main.entity.anki.params.Params;
import main.entity.siyuan.SiYuanBlock;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

public class AnkiService {

    /**
     * 根据属性的类型,向Anki发送数据
     * TODO 对于不同类型的模板进行提示
     * @param attrMap
     * @param childBlocks
     */
    public static void addAnkiNotesBasedOnAttr(Map attrMap, ArrayList<SiYuanBlock> childBlocks) throws Exception {
        if (attrMap.containsKey("deck") && attrMap.containsKey("model") && attrMap.containsKey("tags")) {
            //处理tags
            String[] tags = attrMap.get("tags").toString().split(",");
            addNotes(childBlocks, attrMap.get("deck").toString(), attrMap.get("model").toString(), tags);
        } else if (attrMap.containsKey("deck") && attrMap.containsKey("model")) {
            addNotes(childBlocks, attrMap.get("deck").toString(), attrMap.get("model").toString());
        } else if (attrMap.containsKey("deck") && attrMap.containsKey("tags")) {
            String[] tags = attrMap.get("tags").toString().split(",");
            addNotes(childBlocks, attrMap.get("deck").toString(), tags);
        }
    }

    /**
     * 向Anki中添加Notes
     *
     * @param siyuanBlocks
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    private static void addNotes(ArrayList<SiYuanBlock> siyuanBlocks) throws Exception {
        addNotes(siyuanBlocks, "default", "Basic", new String[]{});
    }

    private static void addNotes(ArrayList<SiYuanBlock> siyuanBlocks, String deck) throws Exception {
        addNotes(siyuanBlocks, deck, "Basic", new String[]{});
    }

    private static void addNotes(ArrayList<SiYuanBlock> siyuanBlocks, String deck, String model) throws Exception {
        addNotes(siyuanBlocks, deck, model, new String[]{});
    }

    private static void addNotes(ArrayList<SiYuanBlock> siyuanBlocks, String deck, String[] tags) throws Exception {
        addNotes(siyuanBlocks, deck, "Basic", tags);
    }

    private static void addNotes(ArrayList<SiYuanBlock> siyuanBlocks, String deck, String model, String[] tags) throws Exception {
        var ankiParams = convertSiYuanToAnki(siyuanBlocks, deck, tags, model);
        AnkiAPI.addNote(ankiParams);
        System.out.println("There are " + siyuanBlocks.size() + " pieces of data.");
        System.out.println("ParentID: " + siyuanBlocks.get(1).getParent_id());
        System.out.println("AnkiController addNotes Done!");
    }

    /**
     * 将SiYuanBlocks 转换为 AnkiNotes
     * @param siyuanBlocks
     * @param deckName
     * @param tags
     * @param modelName
     * @return
     */
    private static Params convertSiYuanToAnki(ArrayList<SiYuanBlock> siyuanBlocks, String deckName, String[] tags, String modelName) {
        var params = new Params();
        // GetModelInfo
        if(modelName.contains("Cloze") || modelName.contains("cloze") ){
            //判断该模板是否存在
            params =  converToCloze(siyuanBlocks,deckName,tags,modelName);
        }else{
            // 非Close模板
            params = converToBasic(siyuanBlocks,deckName,tags,modelName);
        }
        return params;
    }

    /**
     * 给Note设置基本信息
     *
     * @param deckName
     * @param tags
     * @param modelName
     * @return
     */
    private static Note setNoteInfo(String deckName, String[] tags, String modelName) {
        var tempNote = new Note();
        for (int j = 0; j < tags.length; j++) {
            tempNote.setTags(tags[j]);
        }
        tempNote.setDeckName(deckName);
        tempNote.setModelName(modelName);
        return tempNote;
    }

    /**
     * 将所谓的SiYuanBlockList转换为Cloze格式的卡片
     * @param siyuanBlocks
     * @param deckName
     * @param tags
     * @param modelName
     * @return
     */
    private static Params converToCloze(ArrayList<SiYuanBlock> siyuanBlocks, String deckName, String[] tags, String modelName){
        var params = new Params();
        var tempBlock = new SiYuanBlock();
        var tempStringList = new String[2];
        var notes = params.getNotes();
        String backup;
        for (int i = 0; i < siyuanBlocks.size(); i++) {
            var tempNote = setNoteInfo(deckName, tags, modelName);
            var tempFields = tempNote.getFields();
            tempBlock = siyuanBlocks.get(i);
            //[0] 作为clz内容, [1] 作为extra内容
            tempStringList = tempBlock.getMarkdown().split("\\n{1}");
            //设置Backup字段,特殊用途.
            backup = tempStringList[0].replaceAll("\\*\\*","");
            tempFields.setBackup(backup);
            while(tempStringList[0].contains("**")){
                tempStringList[0] = tempStringList[0].replaceFirst("\\*\\*","{{c1::");
                tempStringList[0] = tempStringList[0].replaceFirst("\\*\\*","}}");
            }
            try {
                tempFields.setFront(tempStringList[0]);
                tempFields.setBack(tempStringList[1]);
            }catch (ArrayIndexOutOfBoundsException e){ System.out.println("A note without back,ID:"+tempBlock.getId()); }
            notes.add(tempNote);
        }
        return params;
    }

    /**
     * 转换为基本格式的AnkiNote Front Back
     * @param siyuanBlocks
     * @param deckName
     * @param tags
     * @param modelName
     * @return
     */
    private static Params converToBasic(ArrayList<SiYuanBlock> siyuanBlocks, String deckName, String[] tags, String modelName){
            var params = new Params();
            var tempBlock = new SiYuanBlock();
            var tempStringList = new String[2];
            var notes = params.getNotes();
            for (int i = 0; i < siyuanBlocks.size(); i++) {
                var tempNote = setNoteInfo(deckName, tags, modelName);
                var tempFields = tempNote.getFields();
                //处理思源文本
                tempBlock = (SiYuanBlock) siyuanBlocks.get(i);
                tempStringList = tempBlock.getContent().split("\\n{1}");
                try {
                    tempFields.setFront(tempStringList[0]);
                    tempFields.setBack(tempStringList[1]);
                    notes.add(tempNote);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Format error, please check the block: " + tempBlock.getId() +
                            "\n Block content: " + tempStringList[0]);
                    System.exit(0);
                }
            }
            return params;
        }


}
