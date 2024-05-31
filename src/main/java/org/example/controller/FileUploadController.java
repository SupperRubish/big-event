package org.example.controller;

import org.example.pojo.Result;
import org.example.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@CrossOrigin//支持跨域
public class FileUploadController {
    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        //保证文件名字唯一，防止文件覆盖，使用uuid
        String newfileName = UUID.randomUUID().toString()+fileName.substring(fileName.lastIndexOf("."));
//        file.transferTo(new File("/Users/chengmouren/GIT/files/"+newfileName));
        boolean Bupload = qiniuUtils.upload(file,newfileName);
        return Result.success(QiniuUtils.url+newfileName);

    }
}
