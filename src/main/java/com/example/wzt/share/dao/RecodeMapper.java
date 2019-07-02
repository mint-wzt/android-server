package com.example.wzt.share.dao;

import com.example.wzt.share.model.ShareRecode;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface RecodeMapper {

    @Insert({"insert into share_recode(upload_time,content,user_id) values(#{upload_time},#{content},#{user_id})"})
    int insert(ShareRecode shareRecode);

    @Select({"select share_id from share_recode order by share_id desc limit 1"})
    int selectCurrentID();

    @Select({"select share_id,upload_time,content,thumbs,user_id from share_recode where share_id=#{share_id}"})
    ShareRecode selectByID(int share_id);

    @Select({"select count(*) from share_recode"})
    int selectCount();

    @Select({"select share_id,upload_time,content,thumbs,user_id from share_recode order by share_id desc limit 20"})
    List<ShareRecode> selectRefresh();

    @Delete({"delete from share_recode where share_id=#{share_id}"})
    int deleteByID(int share_id);

    @Update({"update share_recode set thumbs=thumbs+1 where share_id=#{share_id}"})
    int updateThumbs(int share_id);

    @Update({"update share_recode set thumbs=thumbs-1 where share_id=#{share_id}"})
    int subThumbs(int share_id);

}
