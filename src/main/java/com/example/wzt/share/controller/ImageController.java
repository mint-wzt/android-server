package com.example.wzt.share.controller;

import com.example.wzt.share.configure.ShareConfig;
import com.example.wzt.share.configure.UploadConfig;
import com.example.wzt.share.controller.Response.Result;
import com.example.wzt.share.model.ShareRecode;
import com.example.wzt.share.service.ShareService;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(path = "/share")
public class ImageController {

    @Autowired
    private ShareService shareService;
    @Autowired
    private ShareConfig shareConfig;

    @ApiOperation(value = "上传图片", notes = "上传图片")
    @PostMapping("/uploadImage")
    public Object uploadImg(@RequestBody String gson) {
        Gson gson1 = new Gson();
        UploadConfig uploadConfig = gson1.fromJson(gson, UploadConfig.class);
        ShareRecode shareRecode = uploadConfig.getShareRecode();
        int share_id = shareRecode.getShare_id();
        System.out.println("正在保存分享记录：share_id为："+share_id);
        byte[] bytes = uploadConfig.getImg();
        System.out.println("byte:" + bytes);

        //设置路径
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        String imageName = dateFormat.format(now);
        String basePath = System.getProperty("user.dir");
        String name = imageName + ".jpg";
        String path = basePath + "/target/classes/static/images/" + share_id + "/" + name;
        int ret = shareService.addImg(name, path, bytes.length, share_id);
        if (ret == -1) {
            return -1;
        }
        System.out.println("name:" + name);
        System.out.println("path:" + path);
        //将byte转成file类型
        boolean isOk = shareService.byteToFile(path, bytes);
        if (!isOk) {
            shareService.deleteRecord(share_id);
            shareService.deleteImages(share_id);
        }
        System.out.println("share_id:" + share_id);
        return share_id;
    }

    @ApiOperation(value = "获取share_id", notes = "获取share_id")
    @PostMapping("/getShareID")
    public Object getShareID(@RequestBody ShareRecode shareRecode) {
        String content = shareRecode.getContent();
        int user_id = shareRecode.getUser_id();
        System.out.println("user_id:"+user_id+":请求获取share_id");
        int share_id = shareService.addRecode(content, user_id);
        Gson gson = new Gson();
        ShareRecode recode = shareConfig.getShareRecode();
        recode.setShare_id(share_id);
        return gson.toJson(recode);
    }

    @ApiOperation(value = "下载图片", notes = "下载图片")
    @GetMapping("/downLoadImg")
    public String downLoadImg() {
        return shareService.downLoadImg();
    }

    @ApiOperation(value = "点赞+1", notes = "点赞+1")
    @PostMapping("/addThumbs")
    public void updateThumbs(@RequestParam("id") String id) {
        int share_id = Integer.parseInt(id);
        shareService.updateThumbs(share_id);
    }

    @ApiOperation(value = "点赞-1",notes = "点赞-1")
    @PostMapping("/subThumbs")
    public void subThumbs(@RequestParam("id")String id){
        int share_id = Integer.parseInt(id);
        shareService.subThumbs(share_id);
    }
}
