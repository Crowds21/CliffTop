package com.springclifftop.utils;

import com.springclifftop.domain.entity.siyuan.SiYuanBlock;

/**
 * 对思源中的Markdown和Content进行处理
 */
public class SiYuanUtils {


    public SiYuanBlock processDeleteLine(SiYuanBlock block){
        String tempMarkdown = block.getMarkdown();
        if (hasDeleteLine(tempMarkdown)) {
            tempMarkdown = tempMarkdown.replaceAll("~~\\S~~", "");
            block.setMarkdown(tempMarkdown);
        }
        return block;
    }

    public boolean hasDeleteLine(String markdown){
        return markdown.contains("~~");
    }

}
