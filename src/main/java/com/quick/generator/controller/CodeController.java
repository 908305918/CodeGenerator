package com.quick.generator.controller;

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

    @GetMapping("/test")
    public void test(HttpServletResponse response) throws Exception {
        ConfigBean config = new ConfigBean();
        config.setDsUrl("jdbc:mysql://localhost:3306/spring?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&zeroDateTimeBehavior=convertToNull&useSSL=false");
        config.setDriverName("com.mysql.jdbc.Driver");
        config.setUsername("root");
        config.setPassword("123456");
        config.setRootPath(fileDir + "Code/");
        config.setPackageName("com.quick.meeting");
        config.setTablePrefix("m_");
        config.setProjectName("meeting");

        byte[] data = codeGenerator.execute(config, "m_meeting");

        response.setHeader("Content-Disposition", "attachment; filename=\"out.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

}
