package xyz.vaith.weeblogbackend.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:config/qiniu.properties"}, ignoreResourceNotFound = false, encoding = "UTF-8")
@ConfigurationProperties(prefix = "qiniu")
@Data
public class QiniuToken {
    private String accessKey;
    private String secretKey;
}
