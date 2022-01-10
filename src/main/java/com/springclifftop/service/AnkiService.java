package com.springclifftop.service;

import com.springclifftop.api.AnkiAPI;
import com.springclifftop.domain.entity.anki.AnkiTemplate;
import com.springclifftop.domain.entity.anki.Note;
import com.springclifftop.domain.entity.anki.Params;
import com.springclifftop.domain.entity.siyuan.SiYuanBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

@Component
public class AnkiService {

    @Autowired
    AnkiAPI ankiAPI;

    /**
     * 根据属性的类型,向Anki发送数据
     *
     * @param attrMap
     * @param childBlocks
     */
    public void addAnkiNotesBasedOnAttr(Map attrMap, ArrayList<SiYuanBlock> childBlocks) {
        if (attrMap.containsKey("deck") && attrMap.containsKey("mode") && attrMap.containsKey("tags")) {
            //处理tags
            String[] tags = attrMap.get("tags").toString().split(",");
            addNotes(childBlocks, attrMap.get("deck").toString(), attrMap.get("mode").toString(), tags);
        } else if (attrMap.containsKey("deck") && attrMap.containsKey("mode")) {
            addNotes(childBlocks, attrMap.get("deck").toString(), attrMap.get("mode").toString());
        }
    }

    /**
     * 重载 addNotes
     *
     * @param siyuanBlocks
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    private void addNotes(ArrayList<SiYuanBlock> siyuanBlocks, String deck, String mode) {
        addNotes(siyuanBlocks, deck, mode, new String[]{});
    }

    /**
     * 向Anki中添加Notes
     *
     * @param siyuanBlocks
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    private void addNotes(ArrayList<SiYuanBlock> siyuanBlocks, String deck, String mode, String[] tags) {
        var ankiParams = processMdToGenerateParams(siyuanBlocks, deck, tags, mode);
        ankiAPI.addNote(ankiParams);
        System.out.println("There are " + siyuanBlocks.size() + " pieces of data.");
        System.out.println("ParentID: " + siyuanBlocks.get(1).getParent_id());
        System.out.println("AnkiController addNotes Done!");
    }

    /**
     * 处理Markdown并生成Params
     *
     * @param siyuanBlocks
     * @param deckName
     * @param tags
     * @param mode
     * @return
     */
    private static Params processMdToGenerateParams(ArrayList<SiYuanBlock> siyuanBlocks, String deckName, String[] tags, String mode) {
        var params = new Params();
        var notes = new ArrayList<Note>();
        Note note = new Note();
        //处理文本
        String tempMarkdown;
        for (int i = 0; i < siyuanBlocks.size(); i++) {
            var block = siyuanBlocks.get(i);
            tempMarkdown = block.getMarkdown();
            // 去除删除线包含的文字
            if (tempMarkdown.contains("~~")) {
                tempMarkdown = tempMarkdown.replaceAll("~~\\S~~", "");
                siyuanBlocks.get(i).setMarkdown(tempMarkdown);
            }
            // 去除高亮线
            if (tempMarkdown.contains("==")) {
                tempMarkdown = tempMarkdown.replaceAll("==", "");
                siyuanBlocks.get(i).setMarkdown(tempMarkdown);
            }
            //是否为填空
            if (tempMarkdown.contains("**") && mode.equals("type")) {
                //转换Markdown
                var clozeMarkdown = generateClozeMarkdown(block.getMarkdown());
                block.setMarkdown(clozeMarkdown);
                note = generateNote(block, deckName, tags, "SYClozeType");
            } else if (tempMarkdown.contains("**") && mode.equals("basic")) {
                var clozeMarkdown = generateClozeMarkdown(block.getMarkdown());
                block.setMarkdown(clozeMarkdown);
                note = generateNote(block, deckName, tags, "SYCloze");
            } else if (mode.equals("type")) {
                note = generateNote(block, deckName, tags, "SYBasicType");
            } else if (mode.equals("basic")) {
                note = generateNote(block, deckName, tags, "SYBasic");
            }
            notes.add(note);
        }
        params.setNotes(notes);
        return params;
    }

    /**
     * 将Markdown中的加粗文本,转换成Anki中的Cloze文本
     *
     * @param markdown
     * @return
     */
    private static String generateClozeMarkdown(String markdown) {
        while (markdown.contains("**")) {
            markdown = markdown.replaceFirst("\\*\\*", "{{c1::");
            markdown = markdown.replaceFirst("\\*\\*", "}}");
        }
        return markdown;
    }

    /**
     * 生成Note
     *
     * @param block
     * @param deckName
     * @param tags
     * @param modelName
     * @return
     */
    private static Note generateNote(SiYuanBlock block, String deckName, String[] tags, String modelName) {
        var note = setNoteInfo(deckName, tags, modelName);
        var fields = note.getFields();
        var markdown = block.getMarkdown();
        var content = block.getContent();
        int frontIndex=0;
        String front="";
        try {
            frontIndex = markdown.indexOf('\n');
            front = markdown.substring(0, frontIndex);
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("Format error:"+block.getId());
        }
        int backupIndex = content.indexOf('\n');
        String backup = content.substring(0, backupIndex);
        String backAndExtra = markdown.substring(frontIndex + 1);
        int backIndex = backAndExtra.indexOf('\n');
        String back;
        String extra;
        if (backIndex != -1) {
            back = backAndExtra.substring(0, backIndex);
            extra = backAndExtra.substring(backIndex + 1);
        } else {
            back = backAndExtra;
            extra = "";
        }
        fields.setSiyuanID(block.getId());
        fields.setFront(front);
        fields.setBackup(backup);
        if (back.length() != 0) {
            fields.setBack(back);
        } else {
            System.out.println("Format error, please check the block: " + block.getId() +
                    "\nBlock content: " + backup);
            System.exit(0);
            //throw
        }
        if (extra.length() != 0) {
            fields.setExtra(extra);
        }
        return note;
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

    public String getModelNames() {
        return ankiAPI.getModelNames();
    }

    /**
     * 初始化AnkiModel
     */
    public void initAnki() {
        var result1 = createBasicModel("CTBasic", "");
        var result2 = createBasicModel("CTBasicType", "{{type:Front}}");

        var result3 = createClozeModel("CTCloze", "");
        var result4 = createClozeModel("CTClozeType", "{{type:cloze:Front}}");
        System.out.println("1");
    }

    private String createBasicModel(String name, String type) {
        // Anki SYBasic
        var params = new Params();
        params.setModelName(name);
        var inOrderFields = new ArrayList<String>();
        inOrderFields.add("Front");
        inOrderFields.add("Back");
        inOrderFields.add("Extra");
        inOrderFields.add("Backup");
        inOrderFields.add("SiyuanID");
        params.setInOrderFields(inOrderFields);
        params.setCss(".card { font-family: arial;font-size: 20px;text-align: center;color: black;background-color: white;}" +
                ".Extra{font-size: 15px;text-align: left;margin-top:10px;margin-left:10px;}");
        params.setCloze(false);
        var cardTemplates = new ArrayList<AnkiTemplate>();
        var ankiTemplate = new AnkiTemplate();
        ankiTemplate.setName("Card1");
        ankiTemplate.setFront("{{Front}}" + type);
        ankiTemplate.setBack("{{FrontSide}}<hr id=answer>{{Back}}<br><div class = Extra>{{Extra}}</div><br>" +
                "<a ref='siyuan://blocks/{{SiyuanID}}'>Open in SiYuan</a><br>");
        cardTemplates.add(ankiTemplate);
        params.setCardTemplates(cardTemplates);

        //AnkiAPI anki = new AnkiAPI();
        //return anki.createModel(params);
        return ankiAPI.createModel(params);
    }

    private String createClozeModel(String name, String type) {
        // Anki SYBasic
        var params = new Params();
        params.setModelName(name);
        var inOrderFields = new ArrayList<String>();
        inOrderFields.add("Front");
        inOrderFields.add("Back");
        inOrderFields.add("Extra");
        inOrderFields.add("Backup");
        inOrderFields.add("SiyuanID");
        params.setInOrderFields(inOrderFields);
        params.setCss(".card { font-family: arial;font-size: 20px;text-align: center;color: black;background-color: white;}" +
                ".Extra{font-size: 15px;text-align: left;margin-top:10px;margin-left:10px;}" +
                ".cloze {font-weight: bold;color: blue;}");
        params.setCloze(true);
        var cardTemplates = new ArrayList<AnkiTemplate>();
        var ankiTemplate = new AnkiTemplate();
        ankiTemplate.setName("Card1");
        ankiTemplate.setFront("{{cloze:Front}}" + type);
        ankiTemplate.setBack("{{cloze:Front}}" + type + "<br>{{Back}}<br><div class = Extra>{{Extra}}</div><br>" +
                "<a ref='siyuan://blocks/{{SiyuanID}}'>Open in SiYuan</a><br>");
        cardTemplates.add(ankiTemplate);
        params.setCardTemplates(cardTemplates);
        return ankiAPI.createModel(params);
    }
}
