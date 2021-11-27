package com.springclifftop.utils;

public class TerminalUtils {


    /**
     * 通过Format输出红色
     * @param content
     */
    public static void terminalOutputWithRed(String content){
        colorfulOutputInTerminal(content,31);
    }

    /**
     * 通过Format输出绿色
     * @param content
     */
    public static void terminalOutputWithGreen(String content){
        colorfulOutputInTerminal(content,32);
    }
    /**
     * 通过Format输出棕色
     * @param content
     */
    public static void terminalOutputWithBrown(String content){
        colorfulOutputInTerminal(content,33);
    }

    /**
     * 通过Format输出蓝色
     * @param content
     */
    public static void terminalOutputWithBlue(String content){
        colorfulOutputInTerminal(content,34);
    }

    /**
     * 通过Format输出紫色
     * @param content
     */
    public static void terminalOutputWithPurple(String content){
        colorfulOutputInTerminal(content,35);
    }

    /**
     * 通过Format输出Cyan色
     * @param content
     */
    public static void terminalOutputWithCyan(String content){
        colorfulOutputInTerminal(content,37);
    }

    /**
     * 根据front,将content彩色输出
     * @param content
     * @param front
     */
    public static void colorfulOutputInTerminal(String content,int front){
        System.out.printf("\33[%dm%s\33[0m",front, content);
    }

    /**
     * 根据front和back 将content彩色输出
     * @param content
     * @param front 文字颜色
     * @param back  背景色
     */
    public static void colorfulOutputInTerminal(String content, int front, int back){
        System.out.printf("\33[%d;%dm%s\33[0m",front, back, content);
    }

    /**
     * 输出带有删除线的文字
     * https://www.cnblogs.com/dqrcsc/p/12162080.html
     * @param content
     */
    public static void outputWithDeleteLine(String content){
        //"\033[9mhello\033[m"
        System.out.printf("\033[9mhello\033[0m",content);
    }

}
