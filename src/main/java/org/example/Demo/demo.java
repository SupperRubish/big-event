package org.example.Demo;

import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import com.qiniu.util.Auth;
import com.qiniu.http.Response;
import com.qiniu.storage.model.DefaultPutRet;
import com.alibaba.fastjson.JSON;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

class QiniuUtils {

    public static final String url = "https://sdypv6kff.hd-bkt.clouddn.com/";

    @Value("${61mpxHu1WmGcRXngRBfpEHnarI-s7dToeA0UD4Op}")
    private String accessKey;
    @Value("${dGlTmnXGu1H_0arlYsZywGtCFPjefRdBv4CGQRGL}")
    private String accessSecretKey;

    public  boolean upload(MultipartFile file,String fileName){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huabei());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String bucket = "big-event-picture";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, accessSecretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(uploadBytes, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
public class demo {
    public static void main(String[] args) {
//        QiniuUtils qiniuUtils = new QiniuUtils();
//        qiniuUtils.upload(new Mu("/Users/chengmouren/GIT/files/3.png"),"3.png");
    }
}
