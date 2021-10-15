package main.controller;

import main.api.SiYuanAPI;
import main.common.Constant;
import main.common.exceptions.BaseException;
import main.entity.siyuan.SiYuanBlock;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.System.exit;
import static main.service.AnkiNotesService.getBlocksWithAttr;
import static main.utils.TerminalUtils.*;
import static main.utils.TerminalUtils.terminalOutputWithGreen;

public class SiYuanDataViewController {
    /**
     * Tab的空格数
     * 在命令行终端中,一个Tab默认是8个空格
     * 而在IDEAJ的终端中,一个Tab是4个空格
     */
    static int TAB_LENGTH = 8;

    /**
     * 创建随手记
     *
     * @throws Exception
     */
    public static void createShortNote() throws Exception {

        Scanner input = new Scanner(System.in);//Scanner的实例化
        terminalOutputWithGreen("Please Input Title/Tag");
        String title = input.nextLine();
        terminalOutputWithGreen("Please Input Content:");
        String markdown = input.nextLine();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");//设置日期格式
        String docName = df.format(new Date());

        String docID = SiYuanAPI.createDocWithMd("20210814102920-cidbif7", "/InBox/FromTerminal/" + docName, markdown);

        terminalOutputWithGreen("CreateSuccess, DocID:" + docID + "\nPlease summarize and delete in time.");
    }

    /**
     * 获取 Proejct DataView
     * TODO getDataView,应该是通过工厂方法,来获取TableHead,输出什么形式的TableHead应该统一又工厂来管理
     * @throws Exception
     */
    public static void getDataView(String alias) throws Exception {
        // 获取可以被作为DataView输出的块属性
        var blocksID = getBlocksWithAttr("dataview");
        var blocksWithAttrs = new ArrayList<Map>();
        // 表头信息
        ArrayList<String> tableHead = new ArrayList<>();
        tableHead.add(Constant.NAME);
        tableHead.add(Constant.PRIORITY);
        tableHead.add(Constant.STATE);
        tableHead.add(Constant.DEADLINE);
        tableHead.add(Constant.ID);

        //获取块属性
        for (int i = 0; i < blocksID.size(); i++) {
            blocksWithAttrs.add(SiYuanAPI.getAttrsOfBlock(blocksID.get(i)));
        }
        // TODO 按照
        // 每列的宽度
        var tabCounter = getTabCounter(blocksWithAttrs);
        //输出表格
        tableOutPut(tabCounter, blocksWithAttrs, tableHead);
    }
    /**
     * 获取每一列的宽度信息,保存在Map中
     * @param blocksWithAttrs
     * @return
     * @throws Exception
     */
    private static Map<String, Integer> getTabCounter(ArrayList<Map> blocksWithAttrs) throws Exception {
        Map<String, Object> tempBlockAttr;
        Map<String, Integer> tabCounter = new HashMap<>();
        Boolean first = true;
        // 将各块放入ArrayList ,并获取最长的Tab长度
        for (int i = 0; i < blocksWithAttrs.size(); i++) {
            tempBlockAttr = blocksWithAttrs.get(i);
            if (first) {
                tabCounter = initTabCounter(tempBlockAttr, tabCounter);
                first = false;
            } else tabCounter = calMaxOfTab(tempBlockAttr, tabCounter);
        }
        // 计算总长
        return tabCounter;
    }
    /**
     * 初始化TabCounter
     * @param tempBlockAttr
     * @param tabCounter
     * @return
     */
    private static Map<String, Integer> initTabCounter(Map<String, Object> tempBlockAttr, Map<String, Integer> tabCounter) {
        // TODO 可能存在属性不一样导致的读取错误.
        int tempValueLength;
        for (String key : tempBlockAttr.keySet()) {
            tempValueLength = tempBlockAttr.get(key).toString().length() / TAB_LENGTH + 1;
            if (tempValueLength < 3) tabCounter.put(key, 3);
            else tabCounter.put(key, tempValueLength);
        }
        return tabCounter;
    }
    /**
     * 计算合适的Tab长度
     * @param tempBlockAttr
     * @param tabCounter
     * @return
     */
    private static Map<String, Integer> calMaxOfTab(Map<String, Object> tempBlockAttr, Map<String, Integer> tabCounter) {
        int tempValueLength;
        for (String key : tempBlockAttr.keySet()) {
            tempValueLength = tempBlockAttr.get(key).toString().length() / TAB_LENGTH + 1;
            try{
                if (tabCounter.get(key) < tempValueLength) {
                    tabCounter.put(key, tempValueLength);
                }
            }catch (NullPointerException e){
                if (tempValueLength < 3) tabCounter.put(key, 3);
            }

        }
        return tabCounter;
    }
    /**
     * 输出表格
     * TODO 更改后更为 简洁,但是无法控制不同颜色
     * @param tabCounter
     * @param blocksWithAttrs
     */
    private static void tableOutPut(Map<String, Integer> tabCounter, ArrayList<Map> blocksWithAttrs, ArrayList<String> tableHead) {
        //表头
        int tabSum = 0;
        for (int i = 0; i < tableHead.size(); i++) {
            terminalOutputWithGreen(tableHead.get(i).replace("custom-",""));
            int temp = tabCounter.get(tableHead.get(i)) - tableHead.get(i).replace("custom-","").length() / TAB_LENGTH;
            tabSum = tabSum + tabCounter.get(tableHead.get(i));
            for (int j = 0; j < temp; j++) System.out.print("\t");
            //System.out.print("|");
        }
        System.out.print("\n");
        for (int i = 0 ; i < tabSum; i++) System.out.print("----");
        System.out.print("\n");

        //表体
        for (int i = 0; i < blocksWithAttrs.size(); i++) {
            var tempblock = blocksWithAttrs.get(i);
            for (int j = 0; j < tableHead.size(); j++) {
                var tempArrt = tempblock.get(tableHead.get(j));
                System.out.print(tempArrt.toString().trim());
                //terminalOutputWithGreen(tempArrt.toString());
                int temp = tabCounter.get(tableHead.get(j)) - tempArrt.toString().length() / TAB_LENGTH;
                for (int p = 0; p < temp; p++) System.out.print("\t");
                //System.out.print("|");
            }
            System.out.print("\n");
        }

    }
}
