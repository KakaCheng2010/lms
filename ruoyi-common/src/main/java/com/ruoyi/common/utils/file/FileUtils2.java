package com.ruoyi.common.utils.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletOutputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * 文件上传工具类 新版
 */
public class FileUtils2 {

    /**
     * 文件上传
     * @param baseDir
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public static String upload(String baseDir,MultipartFile multipartFile) throws IOException {
        String fileName = generateUniqueFile(baseDir,multipartFile.getOriginalFilename());
        multipartFile.transferTo(new File(fileName));
        return Paths.get(baseDir).relativize(Paths.get(fileName)).toString();
    }


    public static void read(ServletOutputStream out,String baseDir,String filePath) throws IOException {
        BufferedInputStream in = FileUtil.getInputStream(Paths.get(baseDir,filePath).toFile().getAbsolutePath());
        IoUtil.copy(in, out);
        IoUtil.close(out);
    }


    /**
     * 根据当前日期生成文件路径，若文件名冲突则自动递增后缀
     * @param baseDir      基础目录，如："/data/files" 或 "D:/files"
     * @param originalName 原始文件名，如："report.txt"
     * @return 唯一的文件路径
     * @throws IOException 如果创建目录或访问失败
     */
    public static String generateUniqueFile(String baseDir, String originalName) throws IOException {
        // 获取当前日期
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();

        // 格式化路径：baseDir/year/month/day/
        Path dateDir = Paths.get(baseDir,String.valueOf(year), String.format("%02d", month), String.format("%02d", day));
        Files.createDirectories(dateDir);

        // 分离文件名和扩展名
        String nameWithoutExt = originalName;
        String ext = "";
        int lastDotIndex = originalName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            nameWithoutExt = originalName.substring(0, lastDotIndex);
            ext = originalName.substring(lastDotIndex); // 包括 "."
        }

        // 检查文件是否存在，若存在则加 -1, -2, ...
        Path targetFile = dateDir.resolve(originalName);
        int suffix = 1;
        while (Files.exists(targetFile)) {
            String newName = nameWithoutExt + "-" + suffix + ext;
            targetFile = dateDir.resolve(newName);
            suffix++;
        }

        return targetFile.toFile().getAbsolutePath();
    }

    public static void main(String[] args) throws IOException {
       String path= generateUniqueFile("D:/opt","sss.docx");
       System.out.println(path);
    }
}
