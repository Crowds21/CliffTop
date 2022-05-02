package com.springclifftop.controller;

import com.springclifftop.api.AnkiAPI;
import com.springclifftop.domain.entity.siyuan.SiYuanBlock;
import com.springclifftop.service.AnkiService;
import com.springclifftop.service.SiYuanService;
import com.springclifftop.utils.TerminalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ShellComponent
public class AnkiController {

    @Autowired
    AnkiService ankiService;

    @Autowired
    SiYuanService siYuanService;


    /**
     * 生成笔记
     * TODO 添加识别块引用的功能,直接在答案中生成链接
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    @ShellMethod(key ="cn",value = "Create Anki Note.")
    public String createNotes(){
        return ankiService.createNotes();
        //return "Anki Add Notes Done!";
    }


    /**
     * 获取Anki中的所有Model信息
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public String getModelInfo() throws URISyntaxException, IOException, InterruptedException {
        var result = ankiService.getModelNames();
        System.out.println(result);
        return null;
    }

    @ShellMethod(key ="anki init",value = "Init anki.")
    public void initAnki(){
        String names = ankiService.getModelNames();
        if (names.contains("CliffTopBasic") && names.contains("CliffTopCloze")){
            TerminalUtils.terminalOutputWithGreen("Already initialized.\n");
        }else{
            var basicMode = ankiService.createBasicModel("CliffTopBasic","");
            var clozeMode = ankiService.createClozeModel("CliffTopCloze","");
            TerminalUtils.terminalOutputWithGreen("basicMode:" + basicMode+ "\n");
            TerminalUtils.terminalOutputWithGreen("clozeMode:" + clozeMode + "\n");
            TerminalUtils.terminalOutputWithGreen("Initialization complete.\n");
        }
    }
}
