package com.quick.generator.core;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    private static int BUFFER_SIZE = 2 * 1024;

    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param outputStream     zip输出流
     * @param name             压缩后的名称
     * @param keepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    public static void compress(File sourceFile, ZipOutputStream outputStream, String name,
                                boolean keepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            outputStream.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (keepDirStructure) {
                    // 空文件夹的处理
                    outputStream.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    outputStream.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (keepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, outputStream, name + "/" + file.getName(), keepDirStructure);
                    } else {
                        compress(file, outputStream, file.getName(), keepDirStructure);
                    }
                }
            }
        }
    }

    /**
     * 解压
     *
     * @param src
     * @param destDir
     * @throws IOException
     */
    public static void unzip(File src, String destDir) throws IOException {
        if (!src.exists()) {
            throw new RuntimeException(src.getPath() + "文件不存在");
        }
        // 开始解压
        ZipFile zipFile = new ZipFile(src);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            System.out.println("解压：" + entry.getName());
            if (entry.getName().contains("MACOSX")
                    || entry.getName().contains("DS_Store")) {
                continue;
            }
            // 如果是文件夹，就创建个文件夹
            if (entry.isDirectory()) {
                String dirPath = destDir + "/" + entry.getName();
                File dir = new File(dirPath);
                dir.mkdirs();
            } else {
                // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                File targetFile = new File(destDir + "/" + entry.getName());
                // 保证这个文件的父文件夹必须要存在
                File parent = targetFile.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                targetFile.createNewFile();
                // 将压缩文件内容写入到这个文件中
                InputStream is = zipFile.getInputStream(entry);
                FileOutputStream fos = new FileOutputStream(targetFile);
                int len;
                byte[] buf = new byte[BUFFER_SIZE];
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                // 关流顺序，先打开的后关闭
                fos.close();
                is.close();
            }
        }
    }
}
