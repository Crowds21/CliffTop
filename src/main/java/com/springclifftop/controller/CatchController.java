package com.springclifftop.controller;

import com.springclifftop.common.Constant;
import com.springclifftop.common.cacheManager.CacheManager;
import com.springclifftop.domain.vo.TablePrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.ArrayList;

@ShellComponent
@ShellCommandGroup("cache")
public class CatchController {

    @Autowired
    CacheManager cacheManager;

    @ShellMethod(key ="sc",value = "Show datas in cache .")
    public void showCache(){
        var tablePrinter = cacheManager.getCache();
        tablePrinter.printTable(true);
    }

}
