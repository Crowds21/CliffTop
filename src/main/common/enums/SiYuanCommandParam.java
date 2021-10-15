package main.common.enums;

import main.controller.SiYuanDataViewController;

public enum SiYuanCommandParam implements TermianlArgsEnum {
    /**
     * 输出DataView
     */
    dataView() {
        @Override
        public String operation(String args) {
            try {
                SiYuanDataViewController.getDataView(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    },
    /**
     * 创建短笔记
     */
    createNote() {
        @Override
        public String operation(String args) {
            try {
                SiYuanDataViewController.createShortNote();
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    };


}

