package com.example.wzt.share.controller;

import com.example.wzt.share.configure.UserConfig;
import com.example.wzt.share.controller.Response.NotFoundException;
import com.example.wzt.share.controller.Response.Result;
import com.example.wzt.share.model.Users;
import com.example.wzt.share.service.ShareService;
import com.example.wzt.share.service.UsersService;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(path = "/user")
public class LoginController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ShareService shareService;

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public Object checkUser(@RequestBody String userInfo) {
        Gson gson = new Gson();
        UserConfig userConfig = gson.fromJson(userInfo,UserConfig.class);
        Users user = userConfig.getUsers();
        System.out.println(user.getUser_name()+":用户请求登录!");
        byte[] logoBytes = userConfig.getLogoBytes();
        Users instance = usersService.getUser(user);
        if (instance.getMessage().equals("False")){
            return gson.toJson(instance);
        }

        int user_id = instance.getUser_id();
        System.out.println(user_id+"：请求登录");
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        String imageName = dateFormat.format(now);
        String name = imageName + ".jpg";
        String basePath = System.getProperty("user.dir");
        String path = basePath + "/target/classes/static/logo/" + user_id + "/" + name;
        System.out.println("logoBytes:"+logoBytes);
        System.out.println("Size:"+logoBytes.length);
        boolean isOk = shareService.byteToFile(path, logoBytes);
        if (!isOk){
            instance.setMessage("failed!");
        }else {
            int res = usersService.updateLogo(user_id,name);
            if (res <= 0){
                instance.setMessage("failed!");
            }
        }
        return gson.toJson(instance);
    }

    @ApiOperation(value = "用户注册", notes = "用户注册")
    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Object register(@RequestBody Users user) {
        System.out.println(user.getUser_name()+":请求注册");
        Gson gson = new Gson();
        return gson.toJson(usersService.addUser(user));
    }


    @ApiOperation(value = "退出登录", notes = "退出登录")
    @PostMapping(value = "/logout")
    public Object logout(@RequestParam("id") String id) {
        System.out.println("id:"+id+" 退出登录");
        int user_id = Integer.parseInt(id);
        usersService.logout(user_id);
        Gson gson = new Gson();
        return gson.toJson(new Result("logout success!", 200));
    }
}
