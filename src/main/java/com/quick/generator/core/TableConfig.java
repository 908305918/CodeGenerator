package com.quick.generator.core;

import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang3.StringUtils;

public class TableConfig {

    private String tableName;
    private String tablePrefix;

    public TableConfig(String tableName) {
        this.tableName = tableName;
    }

    public TableConfig(String tableName, String tablePrefix) {
        this.tableName = tableName;
        this.tablePrefix = tablePrefix;
    }

    public String getServiceObjectName() {
        String name = tableName;
        if (StringUtils.isNotBlank(tablePrefix)) {
            name = NamingStrategy.removePrefix(tableName, tablePrefix);
        }
        return NamingStrategy.underlineToCamel(name) + "Service";
    }

}
