package com.example.wzt.share.dao;

import com.example.wzt.share.model.Users;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UsersMapper {
    @Select({"select * from users where user_name=#{user_name} and password=#{password} and flag=0"})
    Users getUser(Users user);

    @Insert({"insert into users(user_name,password,email,address,age,sex,hobbies) values(#{user_name},#{password},#{email},#{address},#{age},#{sex},#{hobbies})"})
    int insert(Users user);

    @Select({"select * from users where user_id=#{user_id}"})
    Users selectByID(int user_id);

    @Update({"update users set flag=1 where user_id=#{user_id}"})
    int updateLogin(int user_id);

    @Select({"select * from users where user_name=#{user_name}"})
    Users selectByName(String user_name);

    @Update({"update users set flag=0 where user_id=#{user_id}"})
    int logout(int user_id);

    @Update({"update users set logo=#{logo} where user_id=#{user_id}"})
    int updateLogo(int user_id,String logo);
}
