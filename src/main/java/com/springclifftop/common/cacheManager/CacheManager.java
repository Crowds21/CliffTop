package com.springclifftop.common.cacheManager;

import com.springclifftop.domain.vo.TablePrinter;
import com.springclifftop.utils.TerminalUtils;
import org.springframework.stereotype.Component;

@Component
public class CacheManager {

    private TablePrinter cache;
    private boolean store = false;

    public void putCache(TablePrinter cache) {
        this.cache = cache;
        store = true;
    }

    public TablePrinter getCache() {
        if (store) {
            return cache;
        }else {
            TerminalUtils.terminalOutputWithRed("Empty!\n");
            return null;
        }
    }


}
