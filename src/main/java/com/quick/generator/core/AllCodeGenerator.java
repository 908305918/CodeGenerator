package com.quick.generator.core;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.zip.ZipOutputStream;

@Component
public class AllCodeGenerator {

    @Value("${upload.file.path}")
    private String fileDir;

    public static void main(String[] args) {
        execute();
    }


    public static void execute() {
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir("/Users/Workspace/Image/rescueSystem");
        gc.setOpen(false);
        gc.setFileOverride(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.1.22:3306/rescueSystem");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("apollo");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.iflytek");//包名
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setRestControllerStyle(true);
        //strategy.setTablePrefix("");
        mpg.setStrategy(strategy);
        mpg.execute();
    }

    public byte[] execute(String dsUrl, String driverName, String username, String password,
                          String parent, String tablePrefix,String projectName) throws Exception {
        AutoGenerator mpg = new AutoGenerator();
        String filePath = fileDir + "/Code/";
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(filePath);
        gc.setOpen(false);
        gc.setFileOverride(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(dsUrl);
        dsc.setDriverName(driverName);
        dsc.setUsername(username);
        dsc.setPassword(password);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(parent);//包名
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setRestControllerStyle(true);
        if (tablePrefix != null && tablePrefix.length() > 0) {
            strategy.setTablePrefix(tablePrefix);
        }
        mpg.setStrategy(strategy);
        mpg.execute();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        File codeDir = new File(filePath);
        ZipUtils.compress(codeDir, zip, projectName, true);
        IOUtils.closeQuietly(zip);
        FileUtils.deleteDirectory(codeDir);
        return outputStream.toByteArray();
    }

}
