package com.springclifftop.service;

import com.springclifftop.api.AnkiAPI;
import com.springclifftop.api.SYNewAPI;
import com.springclifftop.domain.entity.anki.AnkiTemplate;
import com.springclifftop.domain.entity.anki.Fields;
import com.springclifftop.domain.entity.anki.Note;
import com.springclifftop.domain.entity.anki.Params;
import com.springclifftop.domain.entity.siyuan.SiYuanBlock;
import com.springclifftop.utils.StringUtil;
import com.springclifftop.utils.TerminalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class AnkiService {

    @Autowired
    AnkiAPI ankiAPI;

    @Autowired
    SiYuanService siYuanService;

    @Autowired
    SYNewAPI siyuanAPI;

    /**
     * 创建笔记
     */
    public String createNotes() {
        var noteList = getBlocksWithAnkiAttributes();
        var params = generateAnkiParams(noteList);
        var result = ankiAPI.addNote(params);
        setAttrAfterAddingNotes(noteList);
        System.out.println("There are " + noteList.size() + " pieces of data.");
        return result;
    }
    
    /**
     * 获取所有需要被转换为卡片的SiYuan块
     * @return
     */
    private ArrayList<HashMap<String,String>> getBlocksWithAnkiAttributes(){
        ArrayList<HashMap<String,String>> noteList = new ArrayList<>();
        ArrayList<SiYuanBlock> parentBlocks = siYuanService.getBlocksWithAttr("anki","parent");
        for (int i = 0; i < parentBlocks.size(); i++) {
            String parentID = parentBlocks.get(i).getId();
            var attrMap = siYuanService.getBlockAttrs(parentID);
            var childBlocks = siYuanService.getChildrenBlocks(parentID );
            for (int j = 0 ; j<childBlocks.size();j++){
                HashMap<String,String> temp = createNoteMap(childBlocks.get(j),attrMap);
                noteList.add(temp);
            }
        }
        ArrayList<SiYuanBlock> childBlockList = siYuanService.getBlocksWithAttr("anki","block");
        for (int i = 0 ; i < childBlockList.size() ; i++){
            String id = childBlockList.get(i).getId();
            var attrMap = siYuanService.getBlockAttrs(id);
            HashMap<String,String> temp = createNoteMap(childBlockList.get(i),attrMap);
            noteList.add(temp);
        }
        System.out.println("Get All Blocks Successfully.");
        return noteList;
    }
    /**
     * 以Map 的形式返回笔记,每个笔记都包含了
     * @param block
     * @param blockAttr
     * @return
     */
    private HashMap<String,String> createNoteMap(SiYuanBlock block,HashMap<String,String> blockAttr){
        HashMap<String,String> noteMap= new HashMap<>();
        noteMap.put("markdown",block.getMarkdown());
        noteMap.put("deckName",blockAttr.get("custom-deck"));
        noteMap.put("tags",blockAttr.get("custom-tags"));
        noteMap.put("SiyuanID",block.getId());
        return noteMap;
    }

    /**
     * 生成 AnkiParams
     * @param noteList
     * @return
     */
    private Params generateAnkiParams(ArrayList<HashMap<String,String>> noteList){
        var params = new Params();
        var notes = new ArrayList<Note>();
        for (int i= 0 ; i<noteList.size();i++){
            Note note = new Note();
            var temp = noteList.get(i);
            temp.put("markdown",processSiYuanMarkdown(temp.get("markdown")));
            if ( isCloze(temp.get("markdown")) ) {
                var clozeMarkdown = convertToCloze(temp.get("markdown"));
                temp.put("markdown",clozeMarkdown);
                note = generateAnkiNote(temp, "CliffTopCloze");
            } else {
                note = generateAnkiNote(temp, "CliffTopBasic");
            }
            notes.add(note);
        }
        params.setNotes(notes);
        return params;
    }

    /**
     * 处理 SiYuan 中的 Markdown 格式
     * @param markdown
     * @return
     */
    private String processSiYuanMarkdown(String markdown){
        // 处理 Latex
        markdown = convertLatex(markdown);
        // TODO 处理 图片
        // TODO 处理 删除线
        if (markdown.contains("~~")) {
            markdown = markdown.replaceAll("~~\\S~~", "");
        }
        // TODO 处理 斜体
        return markdown;
    }

    /**
     * 转换数学公式
     * @param markdown
     * @return
     */
    public static String convertLatex(String markdown){
        while (markdown.contains("$")) {
            markdown = markdown.replaceFirst("\\$", "\\\\[");
            markdown = markdown.replaceFirst("\\$", "\\\\]");
        }
        return markdown;
    }

    /**
     * 判断是否为 Cloze 类型
     * @param markdown
     * @return
     */
    private boolean isCloze(String markdown){
        if (markdown.contains("**")) return true;
        else return false;
    }

    /**
     * 将Markdown中的加粗文本,转换成Anki中的Cloze文本
     * @param markdown
     * @return
     */
    private static String convertToCloze(String markdown) {
        int num = 1;
        while (markdown.contains("**")) {
            markdown = markdown.replaceFirst("\\*\\*", "{{c" + num +"::");
            markdown = markdown.replaceFirst("\\*\\*", "}}");
            num ++;
        }
        return markdown;
    }

    /**
     * 生成 Note
     * @param noteMap
     * @param modelName
     * @return
     */
    private Note generateAnkiNote(HashMap<String,String> noteMap, String modelName){
        try {
            String deckName = noteMap.get("deckName").trim();
            String[] tags = convertStringTagsToArray(noteMap.get("tags").trim());
            var note = setNoteInfo(deckName, tags, modelName);
            var fields = note.getFields();
            // 提前先设置了 SiyuanID
            splitMarkdown(noteMap,fields);
            return note;
        }catch ( NullPointerException e){
            throw new RuntimeException("Please Check :" + noteMap.get("SiyuanID"));
        }
    }
    
    private String[] convertStringTagsToArray(String tag){
        String[] tags = tag.split(",");
        return tags;
    }


    /**
     * 解析 Makrodwn 中的字段
     * @param noteMap
     * @param fields
     * @return
     */
    private Fields splitMarkdown(HashMap<String,String> noteMap, Fields fields){
        //id 字段
        fields.setSiyuanID( noteMap.get("SiyuanID") );
        var markdown = noteMap.get("markdown");
        var processedMarkdown = markdown.split("\\n");

        //其他字段
        for (int i=0 ; i < processedMarkdown.length; i++){
            if (processedMarkdown[i].contains("@Front:")){
                String front = processedMarkdown[i].replaceFirst("@Front:","");
                if (StringUtil.isNotEmpty(front)){
                    fields.setFront(front);
                }
            }
            else if (processedMarkdown[i].contains("@Back:") ){
                String back = processedMarkdown[i].replaceFirst("@Back:","");
                if (StringUtil.isNotEmpty(back)){
                    fields.setBack(back);
                }
            }
            else if (processedMarkdown[i].contains("@Extra:") ){
                String extra = processedMarkdown[i].replaceFirst("@Extra:","");
                if (StringUtil.isNotEmpty(extra)){
                    fields.setExtra(extra);
                }
            }
            else if (processedMarkdown[i].contains("@Backup:") ){
                String backup = processedMarkdown[i].replaceFirst("@Backup:","");
                if (StringUtil.isNotEmpty(backup)){
                    fields.setBackup(backup);
                }
            }
        }
        return fields;
    }
    
    /**
     * 给Note设置基本信息
     * @param deckName
     * @param tags
     * @param modelName
     * @return
     */
    private Note setNoteInfo(String deckName, String[] tags, String modelName) {
        var ankiNote = new Note();
        for (int j = 0; j < tags.length; j++) {
            ankiNote.setTags(tags[j]);
        }
        ankiNote.setDeckName(deckName);
        ankiNote.setModelName(modelName);
        return ankiNote;
    }

    /**
     * 对 anki 属性设置时间戳
     * @param noteList
     */
    private void setAttrAfterAddingNotes(ArrayList<HashMap<String,String>> noteList){
        for (int i = 0 ; i < noteList.size() ; i++){
            var blockID = noteList.get(i).get("SiyuanID");
            LocalDateTime today = LocalDateTime.now();
            var formatTimeString = today.format(DateTimeFormatter.BASIC_ISO_DATE);
            if( siYuanService.setValueOfBlockAttr(blockID,"custom-anki",formatTimeString) )
                System.out.println(blockID+" Done!" + formatTimeString);
            else System.out.println(blockID+" Error!");
        }
    }

    public String getModelNames() {
        return ankiAPI.getModelNames();
    }

    /**
     * 创建模板
     * 如果需要输入,则需要参数 type
     * @param name 模板的名称
     * @param type Anki 中的 Type 字段
     * @return
     */
    public String createBasicModel(String name, String type) {
        // Anki SYBasic
        var params = new Params();
        params.setModelName(name);
        //params.setIsCloze("False");
        var inOrderFields = new ArrayList<String>();
        inOrderFields.add("Front");
        inOrderFields.add("Back");
        inOrderFields.add("Extra");
        inOrderFields.add("Backup");
        inOrderFields.add("SiyuanID");
        params.setInOrderFields(inOrderFields);
        params.setCss(".card { font-family: arial; font-size: 20px; text-align:left; color: black; background-color: white;}" +
                ".layout{\tdisplay: flex;\tflex-direction:column;}" +
                ".extra{\tfont-size: 15px;}.front{\tmargin-top:10px;\tmargin-left:10px;}" +
                ".back{\t\tmargin-top:10px;\tmargin-left:10px;}a{\tfont-size: 15px;\tborder-bottom: 2.5px dashed rgb(122,148,146);}\n");
        var cardTemplates = new ArrayList<AnkiTemplate>();
        var ankiTemplate = new AnkiTemplate();
        ankiTemplate.setName("Card1");
        ankiTemplate.setFront("<div class = front>{{Front}}</div>" + type);
        ankiTemplate.setBack("<div class = layout>{{FrontSide}}<hr id=answer><div class=back>{{Back}}</div><br>" +
                "<div class = extra>{{Extra}}</div><br>" +
                "<a href=\"siyuan://blocks/{{SiyuanID}}\">Open in SiYuan</a><br><div>");
        cardTemplates.add(ankiTemplate);
        params.setCardTemplates(cardTemplates);
        return ankiAPI.createModel(params);
    }

    /**
     * 创建填空模板
     * @param name 模板的名称
     * @param type Anki 中的 Type 字段
     * @return
     */
    public String createClozeModel(String name, String type) {
        // Anki SYBasic
        var params = new Params();
        params.setModelName(name);
        params.setIsCloze("True");
        var inOrderFields = new ArrayList<String>();
        inOrderFields.add("Front");
        inOrderFields.add("Back");
        inOrderFields.add("Extra");
        inOrderFields.add("Backup");
        inOrderFields.add("SiyuanID");
        params.setInOrderFields(inOrderFields);
        params.setCss(".card { font-family: arial; font-size: 20px; text-align:left; color: black; background-color: white;}" +
                ".layout{\tdisplay: flex;\tflex-direction:column;}" +
                ".extra{\tfont-size: 15px;}.front{\tmargin-top:10px;\tmargin-left:10px;}" +
                ".back{\t\tmargin-top:10px;\tmargin-left:10px;}a{\tfont-size: 15px;\tborder-bottom: 2.5px dashed rgb(122,148,146);}\n");
        var cardTemplates = new ArrayList<AnkiTemplate>();
        var ankiTemplate = new AnkiTemplate();
        ankiTemplate.setName("Card1");
        ankiTemplate.setFront("<div class = front>{{cloze:Front}}</div>" + type);
        ankiTemplate.setBack("<div class = layout>{{cloze:Front}}<hr id=answer><div class=back>{{Back}}</div><br>" +
                "<div class = extra>{{Extra}}</div><br>" +
                "<a href=\"siyuan://blocks/{{SiyuanID}}\">Open in SiYuan</a><br><div>");
        cardTemplates.add(ankiTemplate);
        params.setCardTemplates(cardTemplates);
        return ankiAPI.createModel(params);
    }
}

