package xyz.vaith.weeblogbackend.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.File;
import java.util.UUID;

public class QiniuUtil {
    private static Auth auth;
    private static UploadManager uploadManager;
    private static String bucket = "images";

    private static String mini = "imageView2/1/w/50/h/50/q/75|imageslim";
    private static String preview = "imageView2/1/w/100/h/100/q/75|imageslim";

    static {
        auth = auth.create("", "");
        Configuration configuration = new Configuration(Zone.zone0());
        uploadManager = new UploadManager(configuration);
    }

    public static String uploadFile(File file) throws QiniuException {
        String token = auth.uploadToken(bucket);
        String uuid = UUID.randomUUID().toString().replace(" ", "").toLowerCase();

        Response response = uploadManager.put(file, uuid, token);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return putRet.key;
    }

    public static String getUrl(String key) {
        return auth.privateDownloadUrl("http://image.vaith.xyz/" + key + "?" + preview);
    }
}
