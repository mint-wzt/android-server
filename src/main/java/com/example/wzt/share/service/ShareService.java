package com.example.wzt.share.service;

import com.example.wzt.share.configure.ShareConfig;
import com.example.wzt.share.dao.ImagesMapper;
import com.example.wzt.share.dao.RecodeMapper;
import com.example.wzt.share.dao.UsersMapper;
import com.example.wzt.share.model.Images;
import com.example.wzt.share.model.Recode;
import com.example.wzt.share.model.ShareRecode;
import com.example.wzt.share.model.Users;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ShareService {

    @Autowired
    private ImagesMapper imagesMapper;

    @Autowired
    private RecodeMapper recodeMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private ShareConfig shareConfig;


    private List<Recode> recodeList;

    /**
     * 插入一条记录，并返回最新记录的id
     *
     * @param content
     * @param user_id
     * @return
     */
    public int addRecode(String content, int user_id) {
        ShareRecode shareRecode = shareConfig.getShareRecode();
        Date getDate = Calendar.getInstance().getTime();
        String uploadTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(getDate);
        shareRecode.setUpload_time(uploadTime);
        shareRecode.setContent(content);
        shareRecode.setUser_id(user_id);
        int res = recodeMapper.insert(shareRecode);
        if (res <= 0) {
            return -1;
        }
        return recodeMapper.selectCurrentID();
    }

    /**
     * 如果保存图片失败，则把分享记录删除
     *
     * @param share_id
     * @return
     */
    public int deleteRecord(int share_id) {
        return recodeMapper.deleteByID(share_id);
    }

    public int deleteImages(int share_id) {
        return imagesMapper.delete(share_id);
    }

//    /**
//     * 插入一条分享记录后就将图片信息插入图片表中
//     *
//     * @param imageFile
//     * @param share_id
//     * @return
//     * @throws IOException
//     */
//    public int addImage(MultipartFile imageFile, int share_id) throws IOException {
//        Images images = shareConfig.getImages();
//        images.setImage_name(imageFile.getOriginalFilename());
//        String basePath = System.getProperty("user.dir");
//        String path = basePath + "/target/classes/static/images/" + share_id + "/" + imageFile.getOriginalFilename();
//        images.setPath(path);
//        images.setSize((int) imageFile.getSize());
//        images.setShare_id(share_id);
//        int res = imagesMapper.insert(images);
//
//        //如果插入图片失败，则把分享记录删除
//        if (res <= 0) {
//            recodeMapper.deleteByID(share_id);
//            return -1;
//        }
//        File uploadFile = new File(path);
//        if (!uploadFile.getParentFile().exists()) {
//            uploadFile.getParentFile().mkdir();
//        }
//        imageFile.transferTo(uploadFile);
//        return res;
//    }

    /**
     * 返回最新分享的记录
     *
     * @return
     */
    public String downLoadImg() {
        List<ShareRecode> newSharedList = recodeMapper.selectRefresh();
        if (newSharedList.size() == 0) {
            return "No picture！";
        } else {
            recodeList = new ArrayList<>();
            for (ShareRecode shareRecode : newSharedList) {
                Recode recode = new Recode();
                recode.setShareId(shareRecode.getShare_id());
                recode.setUploadTime(shareRecode.getUpload_time());
                Users user = usersMapper.selectByID(shareRecode.getUser_id());
                if (user == null){
                    continue;
                }
                String userName = user.getUser_name();
                recode.setUserName(userName);
                recode.setUser_id(user.getUser_id());
                recode.setLogo(user.getLogo());
                recode.setContent(shareRecode.getContent());
                recode.setThumbs(shareRecode.getThumbs());
                List<String> paths = imagesMapper.getPaths(shareRecode.getShare_id());
                recode.setImagePaths(paths);
                recodeList.add(recode);
            }
            Gson gson = new Gson();
            return gson.toJson(recodeList);
        }
    }

    /**
     * 处理用户点赞数量加1
     *
     * @param share_id
     * @return
     */
    public int updateThumbs(int share_id) {
        return recodeMapper.updateThumbs(share_id);
    }

    /**
     * 用户点赞减1
     *
     * @param share_id
     * @return
     */
    public int subThumbs(int share_id) {
        return recodeMapper.subThumbs(share_id);
    }

    /**
     * 将byte[]数组转化为file
     *
     * @param path
     * @param bytes
     * @return
     */
    public boolean byteToFile(String path, byte[] bytes) {
        boolean isSuccess = true;
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        BufferedOutputStream stream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fileOutputStream);
            stream.write(bytes);
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return isSuccess;
    }

    /**
     * 插入一条分享记录后就将图片信息插入图片表中
     *
     * @param name
     * @param path
     * @param size
     * @param share_id
     * @return
     */
    public int addImg(String name, String path, int size, int share_id) {
        Images images = shareConfig.getImages();
        images.setImage_name(name);
        images.setPath(path);
        images.setSize(size);
        images.setShare_id(share_id);
        int res = imagesMapper.insert(images);
        if (res <= 0) {
            recodeMapper.deleteByID(share_id);
            return -1;
        }
        return 1;
    }

    /**
     * 图片到byte数组
     *
     * @param file
     * @return
     */
    public byte[] image2byte(File file) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(file);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

}
