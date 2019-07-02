package com.example.wzt.share.dao;

import com.example.wzt.share.model.Images;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ImagesMapper {

    @Select({"select image_id,image_name,path,size,share_id from images where image_id=#{image_id}"})
    Images selectByID(int image_id);

    @Insert({"insert into images(image_name,path,size,share_id) values(#{image_name},#{path},#{size},#{share_id})"})
    int insert(Images images);

    @Select({"select image_name from images where share_id=#{share_id}"})
    List<String> getPaths(int share_id);

    @Delete({"delete from images where share_id=#{share_id}"})
    int delete(int share_id);

}
