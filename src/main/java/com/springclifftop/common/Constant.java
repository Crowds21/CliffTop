package com.springclifftop.common;

import org.springframework.beans.factory.annotation.Value;

public class Constant {

    // View
    /**
     * DataView列- Number
     */
    final public static String NUM="num";
    /**
     * DataView列-Project Name
     */
    final public static String NAME    = "name";
    /**
     * DataView列- Project priority
     */
    final public static String PRIORITY= "custom-priority";
    /**
     * DataView列- Project deadline
     */
    final public static String DEADLINE= "custom-deadline";
    /**
     * DataView列- Project state
     */
    final public static String STATE= "custom-state";
    /**
     * DataView列- Project ID
     */
    final public static String ID= "id";
    /**
     * DataView列- task
     */
    final public static String TASK="task";

    /**
     * DAILY_NOTE的ID
     * TODO 后续将其设置为从文件中读取.
     */
    final public static String DAILY_NOTE = "20210810095346-ln1yxi7";


    public static String projectID;

    @Value("${siyuan.project}")
    public void setAddress(String param){
        projectID = param;
    }
}
