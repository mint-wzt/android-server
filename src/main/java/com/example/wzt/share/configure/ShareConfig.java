package com.example.wzt.share.configure;

import com.example.wzt.share.model.Images;
import com.example.wzt.share.model.ShareRecode;
import com.example.wzt.share.model.Users;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShareConfig {
    @Bean
    public Images getImages(){
        return new Images();
    }
    @Bean
    public ShareRecode getShareRecode(){
        return new ShareRecode();
    }
    @Bean
    public Users getUsers(){
        return new Users();
    }
}
