package xyz.vaith.weeblogbackend.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:config/security.properties"}, ignoreResourceNotFound = false, encoding = "UTF-8")
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityHttpConfig {
    private String accessKey;
    private String securityKey;
}
