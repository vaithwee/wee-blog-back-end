package xyz.vaith.weeblogbackend.security;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

public class SecurityUtil {

    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    public static String encrypt(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        byte[] b = cipher.doFinal(content.getBytes("utf-8"));
        return Base64.encodeBase64String(b);
    }

    public static String decrypt(String encryptStr, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        // 采用base64算法进行转码,避免出现中文乱码
        byte[] encryptBytes = Base64.decodeBase64(encryptStr);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    public static String MD5(String content) {
         return DigestUtils.md5DigestAsHex(content.getBytes());
    }
}
