package com.example.wzt.share.service;

import com.example.wzt.share.configure.ShareConfig;
import com.example.wzt.share.controller.Response.NotFoundException;
import com.example.wzt.share.controller.Response.Result;
import com.example.wzt.share.dao.UsersMapper;
import com.example.wzt.share.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private ShareConfig shareConfig;


    /**
     * 用户登录
     * @param users
     * @return
     */
    public Users getUser(Users users){
        Users userInstance = usersMapper.getUser(users);
        if (userInstance == null) {
            userInstance = shareConfig.getUsers();
            userInstance.setMessage("False");
        }else {
            usersMapper.updateLogin(userInstance.getUser_id());
            userInstance.setMessage("True");
        }
        return userInstance;
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    public Result addUser(Users user){
        Users users = usersMapper.selectByName(user.getUser_name());
        if (users == null){
            int res = usersMapper.insert(user);
            if (res <= 0){
                return new Result("Register Failed!",Result.ErrorCode.USER_NOT_FOUND.getCode());
            }
            return new Result("Register success!",200);
        }
        return new Result("Register Failed! This username already exist!",400);
    }

    public int logout(int user_id){
        return usersMapper.logout(user_id);
    }

    /**
     * 更新用户的头像
     * @param user_id
     * @param logo
     * @return
     */
    public int updateLogo(int user_id,String logo){
       return usersMapper.updateLogo(user_id,logo);
    }
}
