package com.springclifftop.domain.vo;

import com.springclifftop.utils.TerminalUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 该类通过表头和表身,来输出表格
 */
public class TablePrinter {
    private ArrayList<String> tableHead;
    private ArrayList<Map> tableRows;
    private int TAB_LENGTH = 8;
    private Map<String, Integer> tabCounter = new HashMap<>();

    /**
     * TODO 后续可以支持,手动设置 TAB_LENGTH
     * @param tableHead
     * @param tableRows
     */
    public TablePrinter(ArrayList<String> tableHead, ArrayList<Map> tableRows) {
        this.tableHead = tableHead;
        this.tableRows = tableRows;
    }

    public Map getRow(int num){
        return tableRows.get(num);
    }
    public int getSize(){
        return tableRows.size();
    }

    /**
     * 输出表格
     */
    public void printTable(Boolean NEED_NUM) {
        calTabCounter();
        //表头
        int tabSum = 0;

        if(NEED_NUM){
            TerminalUtils.terminalOutputWithGreen("Num");
            System.out.print("\t");
        }

        for (int i = 0; i < tableHead.size(); i++) {
            TerminalUtils.terminalOutputWithGreen(tableHead.get(i).replace("custom-",""));
            int temp = tabCounter.get(tableHead.get(i)) - tableHead.get(i).replace("custom-","").length() / TAB_LENGTH;
            tabSum = tabSum + tabCounter.get(tableHead.get(i));
            for (int j = 0; j < temp; j++) System.out.print("\t");
            //System.out.print("|");
        }
        System.out.print("\n");
        //表体
        for (int i = 0; i < tableRows.size(); i++) {
            var tempblock = tableRows.get(i);
            //每一列
            if(NEED_NUM){
                TerminalUtils.terminalOutputWithGreen(i+"");
                System.out.print("\t");
            }
            for (int j = 0; j < tableHead.size(); j++) {
                var tempblockCol = tempblock.get(tableHead.get(j));
                System.out.print(tempblockCol.toString().trim());
                int temp = tabCounter.get(tableHead.get(j)) - tempblockCol.toString().length() / TAB_LENGTH;
                for (int p = 0; p < temp; p++) System.out.print("\t");
            }
            System.out.print("\n");
        }

    }
    /**
     * 输出带有序号标注的表格
     */
    public void printTable(){
        printTable(false);
    }
    /**
     * 计算tabCounter
     */
    private void calTabCounter(){
        Map<String, Object> tempBlockAttr;
        Boolean first = true;
        // 将各块放入ArrayList ,并获取最长的Tab长度
        for (int i = 0; i < tableRows.size(); i++) {
            tempBlockAttr = tableRows.get(i);
            if (first) {
                initTabCounter(tempBlockAttr);
                first = false;
            } else calMaxOfTab(tempBlockAttr);
        }
        // 计算总长
    }
    /**
     * 初始化TabCounter
     * @param tempBlockAttr
     */
    private void initTabCounter(Map<String, Object> tempBlockAttr) {
        // TODO 可能存在属性不一样导致的读取错误.
        int tempValueLength;
        for (String key : tempBlockAttr.keySet()) {
            tempValueLength = tempBlockAttr.get(key).toString().length() / this.TAB_LENGTH + 1;
            if (tempValueLength < 3) tabCounter.put(key, 3);
            else tabCounter.put(key, tempValueLength);
        }
    }
    /**
     * 保存最大值
     * @param tempBlockAttr
     */
    private void calMaxOfTab(Map<String, Object> tempBlockAttr) {
        int tempValueLength;
        for (String key : tempBlockAttr.keySet()) {
            tempValueLength = tempBlockAttr.get(key).toString().length() / TAB_LENGTH + 1;
            try{
                if (this.tabCounter.get(key) < tempValueLength) {
                    tabCounter.put(key, tempValueLength);
                }
            }catch (NullPointerException e){
                if (tempValueLength < 3) tabCounter.put(key, 3);
            }
        }
    }



}
