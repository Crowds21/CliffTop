package com.springclifftop.service;

import com.springclifftop.api.FeiShuAPI;
import com.springclifftop.api.SYNewAPI;
import com.springclifftop.domain.entity.anki.Fields;
import com.springclifftop.domain.entity.feishu.bitable.BiProjectFields;
import com.springclifftop.domain.entity.feishu.bitable.BiRecord;
import com.springclifftop.domain.entity.feishu.bitable.BitableData;
import com.springclifftop.domain.entity.siyuan.SiYuanBlock;
import com.springclifftop.domain.model.SiYuanRespBlocks;
import com.springclifftop.utils.StringUtil;
import com.springclifftop.utils.TerminalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

@Service
public class FeiShuService {

    @Autowired
    private FeiShuAPI feiShuAPI;
    @Autowired
    private SYNewAPI siYuanAPI;

    @Value("${siyuan.notebook}")
    private String notebook;
    @Value("${siyuan.project}")
    private String project;



    /**
     * 同步多维表格中的Project
     */
    public void syncBitableProject() {
        //TODO 缺少一个一个专门用来处理 tenantToken的类或方法
        String tenantAccessToken = feiShuAPI.getTenantAccessToken();
        syncFeiShuToSiYuan(tenantAccessToken);
        TerminalUtils.terminalOutputWithGreen("FeiShu --> SiYuan. Finish.");
        syncSiYuanToFeishu(tenantAccessToken);
        TerminalUtils.terminalOutputWithGreen("SiYuan --> FeiShu. Finish.");
    }

    private void syncFeiShuToSiYuan(String tenantAccessToken) {
        //获取FeiShu记录
        BitableData bitableData = feiShuAPI.listBitableRecords(tenantAccessToken);
        ArrayList<BiRecord> records = bitableData.getItems();
        for (int i = 0; i < records.size(); i++) {
            var recordID = records.get(i).getId();
            var fields = records.get(i).getFields();
            if (StringUtil.isNotNull(fields.getSync()) && fields.getSync()) {
                String blockid = fields.getBlockid();
                //尚未填入blockid
                if (StringUtil.isEmpty(blockid)) {
                    //获取SiYuan路径
                    String sql = "SELECT * FROM blocks WHERE id='" + project + "'";
                    ArrayList<SiYuanBlock> blocks = siYuanAPI.sqlQuery(sql);
                    String hpath = blocks.get(0).getHpath();
                    // 创建文档
                    String docid = siYuanAPI.createDocWithMd(notebook
                            , hpath + "/" + fields.getName()
                            , "");
                    //TODO 让飞书设置DOCID. 后续可以实现重定向
                    fields.setBlockid(docid);
                    feiShuAPI.updateBitableRecord(fields, recordID, tenantAccessToken);
                    // 设置属性
                    SiYuanService service = new SiYuanService();
                    HashMap<String, String> attrs = new HashMap<>();
                    attrs.put("custom-fs-record-id", recordID);
                    siYuanAPI.setBlockAttrs(docid, attrs);
                    TerminalUtils.terminalOutputWithCyan("createDoc:" + fields.getName());
                    TerminalUtils.terminalOutputWithCyan("setAttrs:" + recordID);
                }
            }
        }
    }

    private void syncSiYuanToFeishu(String tenantAccessToken) {
        //获取SiYuan记录,并将其同步到飞书
        String sql = "SELECT * FROM blocks WHERE " +
                "path like '%/" + project + "/______________-_______.sy' " +
                "and type = 'd'";
        //TODO 多次重复的删除，创建，删除，创建，可能会导致数据库中的信息没有被删掉
        ArrayList<SiYuanBlock> blocks = siYuanAPI.sqlQuery(sql);
        for (int i = 0; i < blocks.size(); i++) {
            SiYuanBlock block = blocks.get(i);
            String docid = block.getId();
            HashMap<String, String> attributes = siYuanAPI.getBlockAttrs(docid);

            if (!attributes.containsKey("custom-fs-record-id")) {
                BiProjectFields fields = new BiProjectFields();
                fields.setName(block.getContent());
                fields.setBlockid(block.getId());
                String recordID = feiShuAPI.addBitableRecord(fields, tenantAccessToken);

                HashMap<String, String> attrs = new HashMap<>();
                attrs.put("custom-fs-record-id", recordID);
                siYuanAPI.setBlockAttrs(docid, attrs);

                TerminalUtils.terminalOutputWithCyan("addRecord:" + block.getContent());
                TerminalUtils.terminalOutputWithCyan("setAttrs:" + recordID);
            }
        }

    }

}