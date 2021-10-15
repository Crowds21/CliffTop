package main.common.enums;

/**
 * 对工厂模式的联系
 */
public enum TerminalCommand implements TermianlArgsEnum {

    // anki 相关操作
    anki{
        @Override
        public String operation(String args) {
            //AnkiCommandParam();
            return "anki";
        }
    },
    // siyuan 相关操作
    siyuan{
        @Override
        public String operation(String args) {
            String[] parameter = args.split(" ");
            if (parameter.length < 2) {
                String param = parameter[0].trim();
                SiYuanCommandParam.valueOf(param).operation(null);
            }else{
                String param = parameter[0].trim();
                String paramValue = parameter[1].trim();
                SiYuanCommandParam.valueOf(param).operation(paramValue);
            }

            return " siyuan";
        }
    },
    //语雀相关操作
    yuque{
        @Override
        public String operation(String args) {
            return "yuque";
        }
    };


}