package com.quick.generator.core;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.zip.ZipOutputStream;

@Component
public class CodeGenerator {

    public byte[] execute(ConfigBean config, String... tables) throws Exception {
        TableGenerator tg = new TableGenerator(config);
        tg.execute(tables);

        //FrameGenerator fg = new FrameGenerator(config);
        //fg.execute();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        File codeDir = new File(config.getRootPath());
        ZipUtils.compress(codeDir, zip, config.getProjectName(), true);
        IOUtils.closeQuietly(zip);
        FileUtils.deleteDirectory(codeDir);
        return outputStream.toByteArray();
    }
}
