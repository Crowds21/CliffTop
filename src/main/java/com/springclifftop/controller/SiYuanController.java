package com.springclifftop.controller;

import com.springclifftop.common.Constant;
import com.springclifftop.common.cacheManager.CacheManager;
import com.springclifftop.domain.vo.TablePrinter;
import com.springclifftop.service.SiYuanService;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.springclifftop.utils.TerminalUtils.terminalOutputWithGreen;

@ShellComponent
@ShellCommandGroup("siyuan")
public class SiYuanController {

    @Autowired
    SiYuanService siYuanService;

    @Autowired
    CacheManager cacheManager;

    @ShellMethod(key ="sy -ls",value = "ls notebooks.")
    public void  lsNotebooks(){
        siYuanService.lsNoteBooks();
    }

    /**
     * 创建随手记
     * @throws Exception
     */
    public void createShortNote(){
        Scanner input = new Scanner(System.in);//Scanner的实例化
        terminalOutputWithGreen("Please Input Title/Tag");
        String title = input.nextLine();
        terminalOutputWithGreen("Please Input Content:");
        String markdown = input.nextLine();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");//设置日期格式
        String docName = df.format(new Date());

        // 这个可以封装到Service或其他层里,而不是放在Controller层中
        String docID = siYuanService.createDocWithMd("20210814102920-cidbif7", "/InBox/FromTerminal/" + docName, markdown);

        terminalOutputWithGreen("CreateSuccess, DocID:" + docID + "\nPlease summarize and delete in time.");
    }

    /**
     * ProjectView
     * TODO Need to be constructed
     */
    @ShellMethod(key ="pv",value = "Show project data view.")
    public void projectView() {
        // 表头信息
        ArrayList<String> tableHead = new ArrayList<>();
        tableHead.add(Constant.NAME);
        tableHead.add(Constant.PRIORITY);
        tableHead.add(Constant.STATE);
        tableHead.add(Constant.DEADLINE);
        tableHead.add(Constant.ID);
        // 获取可以被作为DataView输出的块属性
        var blocksWithAttrs = siYuanService.getProjects();
        var tablePrinter = new TablePrinter(tableHead,blocksWithAttrs);
        tablePrinter.printTable(true);
        cacheManager.putCache(tablePrinter);
    }

    /**
     * Inbox View
     */
    //@ShellMethod(key ="bv",value = "Show Inbox data view.")
    public void inBoxView() {
        // 表头信息
        ArrayList<String> tableHead = new ArrayList<>();
        tableHead.add(Constant.NAME);
        tableHead.add(Constant.PRIORITY);
        tableHead.add(Constant.STATE);
        tableHead.add(Constant.DEADLINE);
        tableHead.add(Constant.ID);

        var blocksWithAttrs = siYuanService.getInbox();
        var tablePrinter = new TablePrinter(tableHead,blocksWithAttrs);
        tablePrinter.printTable(true);
        cacheManager.putCache(tablePrinter);
    }

    /**
     *  打开特定ID的块
     *  TODO 这个方法是否应该属于Controller层,应该可以再封装一个放在其他层
     * @param
     */
    @ShellMethod(key ="open",value = "open block.")
    public void openBlock(int num){
       var tablePrinter = cacheManager.getCache();
       var row = tablePrinter.getRow(num);
       siYuanService.openSiYuan(row.get(Constant.ID).toString());
    }

    /**
     *  打开特定ID的块
     *  TODO 这个方法是否应该属于Controller层,应该可以再封装一个放在其他层
     * @param
     */
    @ShellMethod(key ="id",value = "open block")
    public void openBlock(String id){
        siYuanService.openSiYuan(id);
    }


    /**
     * 随机打开笔记块
     */
    @ShellMethod(key ="random",value = "Open random page from T/P.")
    public void randomBlock(){
        var tablePrinter = cacheManager.getCache();
        var tableSize = tablePrinter.getSize();

        Random random = new Random();
        var rowNum =random.nextInt(tableSize);

        var row = tablePrinter.getRow(rowNum);
        System.out.println(row.get(Constant.NAME).toString());
        siYuanService.openSiYuan(row.get(Constant.ID).toString());
    }

    /**
     * 获取TaskView
     */
    @ShellMethod(key ="tv",value = "Show tasks data view.")
    public void taskView(){
        ArrayList<String> tableHead = new ArrayList<>();
        //表头
        //tableHead.add(Constant.NAME);
        tableHead.add(Constant.ID);
        tableHead.add(Constant.NAME);
        //获取表体
        var tableRows = siYuanService.getTasks(Constant.NAME);
        var tablePrinter = new TablePrinter(tableHead,tableRows);
        tablePrinter.printTable(true);
        cacheManager.putCache(tablePrinter);
    }

}
