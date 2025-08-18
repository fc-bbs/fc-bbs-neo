package com.bbs.njtech.storage.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class StorageService {
    @Value("${spring.tengxun.SecretId}")
    private String secretId;
    @Value("${spring.tengxun.SecretKey}")
    private String secretKey;

    @Value("${spring.tengxun.region}")
    private String region;

    @Value("${spring.tengxun.bucketName}")
    private String bucketName;

    @Value("${spring.tengxun.url}")
    private String path;

    @Autowired
    private COSClient cosClient;

    public String upload(MultipartFile file) {
//        ResponseDto responseDto = null;
        try {
            String originalfileName = file.getOriginalFilename();

            // 获得文件流
            InputStream inputStream = file.getInputStream();

            //设置文件key
            String filePath = getFileKey(originalfileName);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(inputStream.available());
            // 上传文件
            cosClient.putObject(new PutObjectRequest(bucketName, filePath, inputStream, metadata));
            cosClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            String url = path + "/" + filePath;
            Map<String, String> map = new HashMap<>();
            map.put("fileName", originalfileName);
            map.put("url", url);
            return map.get("url");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cosClient.shutdown();
        }
        return "www";
    }

    /**
     * 生成文件路径
     *
     * @return
     */
    private String getFileKey(String originalfileName) {
        String filePath = "test/";
        //1.获取后缀名 2.去除文件后缀 替换所有特殊字符
        String fileType = originalfileName.substring(originalfileName.lastIndexOf("."));
        String fileStr = StrUtil.removeSuffix(originalfileName, fileType).replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "_");
        filePath +=  new DateTime().toString("yyyyMMddHHmmss") + "_" + fileStr + fileType;
        return filePath;
    }
}
