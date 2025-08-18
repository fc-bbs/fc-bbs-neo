package com.bbs.njtech.storage.controller;

import cn.hutool.core.util.ArrayUtil;
import com.bbs.njtech.common.exception.BizException;
import com.bbs.njtech.common.vo.Result;
import com.bbs.njtech.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    StorageService storageService;
    @PostMapping("/upload")
    @ResponseBody
    public Result<List<String>> upload(@RequestParam("file_data") MultipartFile[] files) throws IOException {
        System.out.println("文件api成功调用");
        if (ArrayUtil.isEmpty(files)) {
            throw new BizException("请选择要上传的图片");
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
//            String filename = file.getOriginalFilename();

            String imageUrl = storageService.upload(file);
            imageUrls.add(imageUrl);
            System.out.println(file);
        }
        return Result.success(imageUrls);
    }
}
