package com.example.wzt.share.configure;

import com.example.wzt.share.model.Users;

public class UserConfig {
    private byte[] logoBytes;
    private Users users;

    public byte[] getLogoBytes() {
        return logoBytes;
    }

    public void setLogoBytes(byte[] logoBytes) {
        this.logoBytes = logoBytes;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
