package xyz.vaith.weeblogbackend.enumerate;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.stereotype.Component;
import xyz.vaith.weeblogbackend.enumerate.image.ImageAccessType;
import xyz.vaith.weeblogbackend.enumerate.handler.UniversalTypeEnumHandler;
import xyz.vaith.weeblogbackend.enumerate.image.ImageBucketType;
import xyz.vaith.weeblogbackend.enumerate.image.ImageServerType;

@Component
@Log4j2
public class RegisterEnumHandlerConfig implements ConfigurationCustomizer {
    @Override
    public void customize(Configuration configuration) {
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        typeHandlerRegistry.register(ImageAccessType.class, UniversalTypeEnumHandler.class);
        typeHandlerRegistry.register(ImageBucketType.class, UniversalTypeEnumHandler.class);
        typeHandlerRegistry.register(ImageServerType.class, UniversalTypeEnumHandler.class);
    }
}
