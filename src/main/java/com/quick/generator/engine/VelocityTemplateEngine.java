package com.quick.generator.engine;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;

public class VelocityTemplateEngine {
    private VelocityEngine velocityEngine;

    public VelocityTemplateEngine() {
        Properties p = new Properties();
        p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        p.setProperty("file.resource.loader.path", "");
        p.setProperty("UTF-8", ConstVal.UTF8);
        p.setProperty("input.encoding", ConstVal.UTF8);
        p.setProperty("file.resource.loader.unicode", "true");
        this.velocityEngine = new VelocityEngine(p);
    }

    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        FileOutputStream fos = null;
        OutputStreamWriter ow = null;
        BufferedWriter writer = null;
        try {
            File file = new File(outputFile);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            Template template = this.velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
            fos = new FileOutputStream(outputFile);
            ow = new OutputStreamWriter(fos, ConstVal.UTF8);
            writer = new BufferedWriter(ow);
            template.merge(new VelocityContext(objectMap), writer);
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(ow);
            IOUtils.closeQuietly(fos);
        }
    }

}
