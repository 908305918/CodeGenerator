package com.quick.generator.controller;

import com.quick.generator.core.AllCodeGenerator;
import com.quick.generator.core.CodeGenerator;
import com.quick.generator.core.ConfigBean;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@RestController
public class CodeController {

    @Value("${upload.file.path}")
    private String fileDir;

    @Autowired
    private CodeGenerator codeGenerator;

    @Autowired
    private AllCodeGenerator allCodeGenerator;

    @GetMapping("/initCode")
    public void init(String dsUrl, String dsUsername, String dsPassword, String prefix,
                     String packageName, String projectName, String[] tables,
                     HttpServletResponse response) throws Exception {
        ConfigBean config = new ConfigBean();
        config.setDsUrl(dsUrl);
        config.setDriverName("com.mysql.jdbc.Driver");
        config.setUsername(dsUsername);
        config.setPassword(dsPassword);
        config.setRootPath(fileDir + "/Code/");
        config.setPackageName(packageName);
        config.setTablePrefix(prefix);
        config.setProjectName(projectName);

        byte[] data = codeGenerator.execute(config, tables);

        response.setHeader("Content-Disposition", "attachment; filename=\"out.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

    @GetMapping("/initAllCode")
    public void initAllCode(int databaseType, String dsUrl, String dsUsername, String dsPassword,
                            String prefix, String packageName, String projectName,
                            HttpServletResponse response) {
        try {
            String driveName = "com.mysql.jdbc.Driver";
            if (databaseType == 1) { //mysql
                driveName = "com.mysql.jdbc.Driver";
            } else if (databaseType == 2) { //oracle
                driveName = "oracle.jdbc.driver.OracleDriver";
            }
            byte[] data = allCodeGenerator.execute(dsUrl, driveName, dsUsername, dsPassword, packageName, prefix, projectName);

            response.setHeader("Content-Disposition", "attachment; filename=\"out.zip\"");
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream; charset=UTF-8");

            IOUtils.write(data, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
