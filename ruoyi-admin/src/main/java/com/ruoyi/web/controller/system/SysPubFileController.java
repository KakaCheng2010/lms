package com.ruoyi.web.controller.system;

import cn.hutool.core.util.IdUtil;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.file.FileUtils2;
import com.ruoyi.system.domain.SysPubFile;
import com.ruoyi.system.service.impl.SysPubFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/system/pub_file")
public class SysPubFileController extends BaseController {
    @Autowired
    private SysPubFileService sysFileService;

    @PostMapping("upload")
    public AjaxResult upload(MultipartFile file) throws IOException {
        //相对路径
        String filePath = FileUtils2.upload(RuoYiConfig.getUploadPath() + "/file", file);

        Long id=IdUtil.getSnowflake().nextId();
        SysPubFile sysFile = new SysPubFile();
        sysFile.setId(id);
        sysFile.setName(file.getOriginalFilename());
        sysFile.setType(file.getContentType());
        sysFile.setSize(file.getSize());
        sysFile.setPath(filePath);

        sysFileService.save(sysFile);
        return AjaxResult.success(String.valueOf(id));
    }

    @GetMapping("download/{fileId}")
    public void download(@PathVariable("fileId") String fileId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        SysPubFile sysFile =sysFileService.getById(fileId);
        response.setContentType(sysFile.getType());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + sysFile.getName() + "\"");
        FileUtils2.read(response.getOutputStream(),RuoYiConfig.getUploadPath() + "/file",sysFile.getPath());
    }
}
