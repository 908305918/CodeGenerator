package com.quick.generator.core;

import com.quick.generator.engine.VelocityTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrameGenerator {

    private ConfigBean config;
    private VelocityTemplateEngine engine;

    public FrameGenerator(ConfigBean config) {
        this.config = config;
        engine = new VelocityTemplateEngine();
    }

    public List<String> getTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add("templates/Application.java.vm");
        templates.add("templates/application.properties.vm");
        templates.add("templates/pom.xml.vm");
        templates.add("templates/SwaggerConfig.java.vm");
        templates.add("templates/UiResponse.java.vm");
        return templates;
    }

    public String getFileName(String template) {
        String mainPath = config.getRootPath() + "src" + File.separator + "main" + File.separator;
        String javaPath = "java" + File.separator + config.getPackageName().replace(".", File.separator) + File.separator;
        if (template.contains("Application.java.vm")) {
            return mainPath + javaPath + "Application.java";
        }
        if (template.contains("SwaggerConfig.java")) {
            return mainPath + javaPath + "config" + File.separator + "SwaggerConfig.java";
        }
        if (template.contains("UiResponse.java.vm")) {
            return mainPath + javaPath + "common" + File.separator + "UiResponse.java";
        }
        if (template.contains("application.properties.vm")) {
            return mainPath + "resources" + File.separator + "application.properties";
        }
        if (template.contains("pom.xml.vm")) {
            return config.getRootPath() + "pom.xml";
        }
        return null;
    }


    public void execute() throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("packageName", config.getPackageName());
        objectMap.put("projectName", config.getProjectName());
        String packageName = config.getPackageName();
        String groupId = packageName.substring(0, packageName.lastIndexOf("."));
        String artifactId = packageName.substring(packageName.lastIndexOf(".") + 1);
        objectMap.put("groupId", groupId);
        objectMap.put("artifactId", artifactId);

        Map<String, Object> ds = new HashMap<>();
        ds.put("url", config.getDsUrl());
        ds.put("username", config.getUsername());
        ds.put("password", config.getPassword());
        ds.put("driverName", config.getDriverName());

        objectMap.put("ds", ds);

        List<String> templates = getTemplates();
        for (String template : templates) {
            engine.writer(objectMap, template, getFileName(template));
        }
    }
}
