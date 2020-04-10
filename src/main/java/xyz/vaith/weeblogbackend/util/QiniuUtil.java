package xyz.vaith.weeblogbackend.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.log4j.Log4j2;
import xyz.vaith.weeblogbackend.enumerate.image.ImageAccessType;

import java.io.File;
import java.util.UUID;

@Log4j2
public class QiniuUtil {
    public static String private_bucket_url = "http://image.vaith.xyz/";
    public static String markdown_bucket_url = "http://mk.vaith.xyz/";
    public static String mini = "imageView2/1/w/50/h/50/q/75|imageslim";
    public static String preview = "imageView2/0/w/200/h/200/q/75|imageslim";
    private static String private_bucket = "images";
    private static String markdown_bucket = "wee-markdown";
    private static QiniuUtil _instance;
    private Auth auth;
    private UploadManager uploadManager;
    private BucketManager bucketManager;

    QiniuUtil() {
        QiniuToken token = SpringContextUtil.getBean(QiniuToken.class);
        auth = Auth.create(token.getAccessKey(), token.getSecretKey());
        Configuration configuration = new Configuration(Zone.zone0());
        uploadManager = new UploadManager(configuration);
        bucketManager = new BucketManager(auth, configuration);
    }

    public static QiniuUtil defaultUtil() {
        if (_instance == null) {
            _instance = new QiniuUtil();
        }
        return _instance;
    }

    public String uploadFile(File file, ImageAccessType type) throws QiniuException {
        String bucket = private_bucket;
       switch (type) {
           case PUBLIC:
               bucket = markdown_bucket;
               break;
           case PRIVATE:
               bucket = private_bucket;
               break;
       }
        String token = auth.uploadToken(bucket);
        String uuid = UUID.randomUUID().toString().replace(" ", "").toLowerCase();

        Response response = uploadManager.put(file, uuid, token);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return putRet.key;
    }

    public void delete(String key, ImageAccessType type) throws Exception {
        switch (type) {
            case PUBLIC:
                bucketManager.delete(markdown_bucket, key);
                break;
            case PRIVATE:
                bucketManager.delete(private_bucket, key);
                break;
        }

    }

    public String getOriginalURL(String key, ImageAccessType type) {
        switch (type) {
            case PUBLIC:
                return markdown_bucket_url + key;
            case PRIVATE:
                return auth.privateDownloadUrl(private_bucket_url + key);
        }
        return null;
    }

    public String getLimitURL(String key, String limit, ImageAccessType type) {
        switch (type) {

            case PUBLIC:
                return markdown_bucket_url + key + "?" + limit;
            case PRIVATE:
                return auth.privateDownloadUrl(private_bucket_url + key + "?" + limit, 120);
        }
        return null;

    }
}
