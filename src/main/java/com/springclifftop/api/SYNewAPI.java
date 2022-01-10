package com.springclifftop.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springclifftop.domain.entity.siyuan.NoteBook;
import com.springclifftop.domain.entity.siyuan.SiYuanBlock;
import com.springclifftop.domain.model.SiYuanRespBlocks;
import com.springclifftop.domain.model.SiYuanRespMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Component
public class SYNewAPI {

    @Value("${siyuan.server}")
    private  String SY_SERVER;

    @Value("${siyuan.token}")
    private String SY_TOKEN;

    /**
     * 设置HttpEntity
     * @param request
     * @return
     */
    private HttpEntity setHttpEntity(Object request){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Token "+ SY_TOKEN );
        headers.add("Content-Type","application/json");
        HttpEntity httpEntity = new HttpEntity<>(request,headers);
        return httpEntity;
    }

    /**
     * 返回Boolean
     * @param responseEntity
     * @return
     */
    private boolean identifyCode(ResponseEntity<String> responseEntity){
        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        String code  = jsonObject.getString("code");
        return code.equals("0");
    }

    /**
     * 列出笔记本
     * /api/notebook/lsNotebooks
     * @return
     */
    public NoteBook[] lsNoteBooks(){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity httpEntity = setHttpEntity(null);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/notebook/lsNotebooks"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);
        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        JSONObject data = (JSONObject) jsonObject.get("data");
        JSONArray jsonArray = data.getJSONArray("notebooks");
        NoteBook[] noteBooks = jsonArray.toJavaObject( NoteBook[].class );
        return noteBooks;
    }

    /**
     * 打开笔记本
     * /api/notebook/openNotebook
     * @param id
     * @return
     */
    public boolean openNotebook(String id){
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("notebook",id);
        HttpEntity httpEntity = setHttpEntity(paramMap);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/notebook/openNotebook"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);
        return identifyCode(responseEntity);
    }

    /**
     * 关闭笔记本
     * /api/notebook/closeNotebook
     * @param id 笔记本id
     * @return
     */
    public boolean closeNotebook(String id){
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("notebook",id);
        HttpEntity httpEntity = setHttpEntity(paramMap);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/notebook/closeNotebook"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);
        return identifyCode(responseEntity);
    }

    /**
     * 重命名笔记本
     * /api/notebook/renameNotebook
     * @param id
     * @param name
     * @return
     */
    public boolean renameNotebook(String id,String name){
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("notebook",id);
        paramMap.put("name",name);
        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/notebook/renameNotebook"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);

        return identifyCode(responseEntity);
    }

    /**
     * 创建笔记本
     * /api/notebook/createNotebook
     * @param name
     * @return
     */
    public boolean createNoteBook(String name){
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("name",name);
        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/notebook/createNotebook"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);

        return identifyCode(responseEntity);
    }

    /**
     * 删除笔记本
     * /api/notebook/removeNotebook
     * @param id
     * @return
     */
    public boolean removeNotebook(String id){
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("notebook",id);
        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/notebook/removeNotebook"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);

        return identifyCode(responseEntity);
    }

    /**
     * 获取笔记本配置
     * /api/notebook/getNotebookConf
     * @param id
     * @return
     */
    public JSONObject getNotebookConf(String id){
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("notebook",id);
        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/notebook/getNotebookConf"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);

        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        JSONObject noteConf  = (JSONObject)jsonObject.get("data");
        return noteConf;
    }

    /**
     * 保存笔记本配置
     * /api/notebook/setNotebookConf
     * @param
     * @return
     */
    public JSONObject setNotebookConf(String id, Map conf){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("notebook",id);
        paramMap.put("conf",conf);
        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/notebook/setNotebookConf"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);

        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        JSONObject noteConf  = (JSONObject)jsonObject.get("data");
        return noteConf;
    }

    /**
     * 通过Markdown创建文档
     * /api/filetree/createDocWithMd
     * @param notebook
     * @param hpath
     * @param markdown
     * @return
     */
    public String createDocWithMd(String notebook, String hpath, String markdown){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("notebook",notebook);
        paramMap.put("path",hpath);
        paramMap.put("markdown",markdown);
        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/filetree/createDocWithMd"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);

        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        String docid  = jsonObject.getString("data");
        return docid;
    }

    /**
     * 重命名文档
     * /api/filetree/renameDoc
     * @param notebook
     * @param path /20210902210113-0avi12f.sy
     * @param title
     * @return
     */
    public boolean renameDoc(String notebook, String path, String title){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("notebook",notebook);
        paramMap.put("path",path);
        paramMap.put("title",title);
        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/filetree/renameDoc"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);

        return identifyCode(responseEntity);
    }

    /**
     * 删除文档
     * /api/filetree/removeDoc
     * @param notebook
     * @param path
     * @return
     */
    public boolean removeDoc(String notebook, String path){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("notebook",notebook);
        paramMap.put("path",path);
        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/filetree/removeDoc"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);

        return identifyCode(responseEntity);
    }


    /**
     * 移动文档
     * /api/filetree/moveDoc
     * @param fromNoteBook
     * @param fromPath
     * @param toNoteBook
     * @param toPath
     * @return
     */
    public boolean moveDoc(String fromNoteBook,String fromPath
            ,String toNoteBook,String toPath){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("fromNoteBook",fromNoteBook);
        paramMap.put("fromPath",fromPath);
        paramMap.put("toNoteBook",toNoteBook);
        paramMap.put("toPath",toPath);
        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/filetree/moveDoc"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);

        return identifyCode(responseEntity);
    }

    /**
     * 获取人类可读路径
     * /api/filetree/getHPathByPath
     * @param notebook
     * @param path
     * @return
     */
    public String getHPathByPath(String notebook, String path){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("notebook",notebook);
        paramMap.put("path",path);
        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/filetree/getHPathByPath"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);


        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        return jsonObject.getString("data");
    }

    /**
     * TODO
     * @return
     */
    public String upload(){
        return "";
    }

    /**
     * 插入块
     * /api/block/insertBlock
     * @param dataType
     * @param data
     * @param previousID
     * @return
     */
    public JSONObject insertBlock(String dataType,String data, String previousID){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("dataType",dataType);
        paramMap.put("data",data);
        paramMap.put("previousID",previousID);

        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/block/insertBlock"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);
        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        return (JSONObject) jsonObject.get("data");
    }

    /**
     * 更新块
     * /api/block/updateBlock
     * @param dataType
     * @param data
     * @param id
     * @return
     */
    public JSONObject updateBlock(String dataType,String data, String id){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("dataType",dataType);
        paramMap.put("data",data);
        paramMap.put("id",id);

        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/block/updateBlock"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);
        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        return (JSONObject) jsonObject.get("data");
    }

    /**
     * 删除块
     * /api/block/deleteBlock
     * @param id
     * @return
     */
    public JSONObject deleteBlock(String id){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("id",id);
        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/block/deleteBlock"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);
        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        return (JSONObject) jsonObject.get("data");
    }

    /**
     * 设置属性
     * /api/attr/setBlockAttrs
     * @param id
     * @param attrs
     * @return
     */
    public boolean setBlockAttrs(String id,Map attrs){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("id",id);
        paramMap.put("attrs",attrs);

        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/attr/setBlockAttrs"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);

        return identifyCode(responseEntity);
    }

    /**
     * 获取块属性
     * /api/attr/getBlockAttrs
     * @param id
     * @return
     */
    public HashMap<String,String> getBlockAttrs(String id){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("id",id);

        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SiYuanRespMap> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/attr/getBlockAttrs"
                ,HttpMethod.POST
                ,httpEntity
                ,SiYuanRespMap.class);

        //JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        //String data = jsonObject.getString("data");
        return responseEntity.getBody().getData();

    }

    /**
     *  SQL 查询
     * @param stmt
     * @return
     */
    public ArrayList<SiYuanBlock> sqlQuery(String stmt){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("stmt",stmt);
        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SiYuanRespBlocks> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/query/sql"
                ,HttpMethod.POST
                ,httpEntity
                ,SiYuanRespBlocks.class);

        //JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        //return (JSONObject) jsonObject.get("data");
        return responseEntity.getBody().getData();
    }

    /**
     * TODO 模板渲染
     */
    public void templateRender(){

    }

    /**
     * 道出Markdown文本
     * /api/export/exportMdContent
     * @param id
     * @return
     */
    public String exportMdContent(String id){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("id",id);
        HttpEntity httpEntity = setHttpEntity(paramMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(SY_SERVER+"/api/export/exportMdContent"
                ,HttpMethod.POST
                ,httpEntity
                ,String.class);

        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        JSONObject data = (JSONObject) jsonObject.get("data");

        return data.getString("content");
    }


    //TODO 系统
}
