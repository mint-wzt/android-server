package com.example.wzt.share.configure;

import com.example.wzt.share.model.ShareRecode;

public class UploadConfig {
    private byte[] img;
    private ShareRecode shareRecode;

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public ShareRecode getShareRecode() {
        return shareRecode;
    }

    public void setShareRecode(ShareRecode shareRecode) {
        this.shareRecode = shareRecode;
    }
}
