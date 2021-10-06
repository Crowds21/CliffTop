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

public class AnkiNotesService {

    /**
     * 获取带有Anki属性的块id
     *
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public static ArrayList<String> getBlocksWithAttr(String customAttr) throws URISyntaxException, IOException, InterruptedException {
        String tempID;
        String query = "SELECT * FROM blocks WHERE id in (SELECT block_id FROM attributes AS a  WHERE " +
                "(a.name='custom-" + customAttr + "' AND a.value='true') GROUP BY block_id )";
        var blocksID = new ArrayList<String>();
        var blocks = SiYuanAPI.getBlocksOfSQLQuery(query);
        for (int i = 0; i < blocks.size(); i++) {
            tempID = blocks.get(i).getId();
            blocksID.add(tempID);
        }
        System.out.println("parent_id 获取完毕");
        return blocksID;
    }

    /**
     * 根据属性的类型,向Anki发送数据
     *
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
     * 获取这些id的子块
     * 对于空行则不会放入list中
     *
     * @param parentID
     */
    public static ArrayList<SiYuanBlock> getChildrenBlocks(String parentID) throws URISyntaxException, IOException, InterruptedException {
        var noteBlocks = new ArrayList<SiYuanBlock>();
        String query = "SELECT * FROM blocks WHERE parent_id='" + parentID + "'";
        //获取其全部子块放入
        var temp = SiYuanAPI.getBlocksOfSQLQuery(query);
        for (int j = 0; j < temp.size(); j++) {
            if (isNotEmpty(temp.get(j).getContent()))
                noteBlocks.add(temp.get(j));
        }
        return noteBlocks;
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
        System.out.println("There are " + siyuanBlocks.size() + "pieces of data.");
        System.out.println("ParentID: " + siyuanBlocks.get(1).getParent_id());
        System.out.println("AnkiController addNotes Done!");
    }

    /**
     * 将SiYuanBlocks 转换为 AnkiNotes
     */
    private static Params convertSiYuanToAnki(ArrayList<SiYuanBlock> siyuanBlocks, String deckName, String[] tags, String modelName) {
        var params = new Params();
        var notes = params.getNotes();
        var tempBlock = new SiYuanBlock();
        var tempStringList = new String[2];
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


}
