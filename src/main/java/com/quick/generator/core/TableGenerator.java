package com.quick.generator.core;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.HashMap;
import java.util.Map;

public class TableGenerator {

    private ConfigBean config;

    public TableGenerator(ConfigBean config) {
        this.config = config;
    }

    public void execute(String... tables) {
        for (String table : tables) {
            execute(table, config.getTablePrefix());
        }
    }

    public void execute(String tableName, String tablePrefix) {
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(config.getRootPath());
        gc.setOpen(false);
        gc.setFileOverride(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(config.getDsUrl());
        dsc.setDriverName(config.getDriverName());
        dsc.setUsername(config.getUsername());
        dsc.setPassword(config.getPassword());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(config.getPackageName());
        mpg.setPackageInfo(pc);

        // 自定义注入配置
        TableConfig tableConfig = new TableConfig(tableName, tablePrefix);
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("serviceObjectName", tableConfig.getServiceObjectName());
                map.put("packageName", config.getPackageName());
                this.setMap(map);
            }
        };
        mpg.setCfg(cfg);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setInclude(tableName);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setRestControllerStyle(true);
        strategy.setTablePrefix(tablePrefix);
        mpg.setStrategy(strategy);
        mpg.execute();
    }
}
