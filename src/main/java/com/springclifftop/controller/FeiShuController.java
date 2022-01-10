package com.springclifftop.controller;

import com.springclifftop.api.FeiShuAPI;
import com.springclifftop.domain.entity.feishu.bitable.BiProjectFields;
import com.springclifftop.domain.entity.feishu.bitable.BiRecord;
import com.springclifftop.domain.entity.feishu.bitable.BitableApp;
import com.springclifftop.service.FeiShuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@ShellComponent
@ShellCommandGroup("feishu")


public class FeiShuController {

    @Autowired
    FeiShuService feiShuService;

    @ShellMethod(key ="fs -sync",value = "Data synchronization between Siyuan and Feishu")
    public void feiShuSync(){
        feiShuService.syncBitableProject();
    }


//    @RequestMapping("/login")
//    public void feiShuLogin(String code) {
//        System.out.println(code);
//
//    }



}
