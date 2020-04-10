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
import xyz.vaith.weeblogbackend.enumerate.ImageAccessType;

import java.io.File;
import java.util.UUID;

@Log4j2
public class QiniuUtil {
    private Auth auth;
    private UploadManager uploadManager;
    private BucketManager bucketManager;


    private static String private_bucket = "images";
    private static String markdown_bucket = "wee-markdown";
    public static String mini = "imageView2/1/w/50/h/50/q/75|imageslim";
    public static String preview = "imageView2/0/w/200/h/200/q/75|imageslim";

    private static QiniuUtil _instance;

    public static QiniuUtil defaultUtil() {
        if (_instance == null) {
            _instance = new QiniuUtil();
        }
        return _instance;
    }

    QiniuUtil() {
        QiniuToken token = SpringContextUtil.getBean(QiniuToken.class);
        auth = Auth.create(token.getAccessKey(), token.getSecretKey());
        Configuration configuration = new Configuration(Zone.zone0());
        uploadManager = new UploadManager(configuration);
        bucketManager = new BucketManager(auth, configuration);
    }

    public String uploadFile(File file, Integer type) throws QiniuException {
        String bucket = private_bucket;
        if (type == 1) {
            bucket = markdown_bucket;
        }
        String token = auth.uploadToken(bucket);
        String uuid = UUID.randomUUID().toString().replace(" ", "").toLowerCase();

        Response response = uploadManager.put(file, uuid, token);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return putRet.key;
    }

    public void delete(String key, ImageAccessType type) throws Exception {
        bucketManager.delete(private_bucket, key);
    }

    public String getOriginalURL(String key) {
        return auth.privateDownloadUrl("http://image.vaith.xyz/" + key);
    }

    public String getLimitURL(String key, String limit) {
        return auth.privateDownloadUrl("http://image.vaith.xyz/" + key + "?" + limit, 120);
    }
}
