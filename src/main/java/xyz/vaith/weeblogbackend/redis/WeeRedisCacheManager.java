package xyz.vaith.weeblogbackend.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ReflectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;

@Log4j2
public class WeeRedisCacheManager extends RedisCacheManager implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    //配置列表
    private Map<String, RedisCacheConfiguration> initialCacheConfiguration = new LinkedHashMap<>();

    public static final StringRedisSerializer STRING_REDIS_SERIALIZER = new StringRedisSerializer();

    public static final GenericFastJsonRedisSerializer GENERIC_FAST_JSON_REDIS_SERIALIZER = new GenericFastJsonRedisSerializer();

    public static final RedisSerializationContext.SerializationPair<String> STRING_SERIALIZATION_PAIR = RedisSerializationContext.SerializationPair.fromSerializer(STRING_REDIS_SERIALIZER);

    public static final RedisSerializationContext.SerializationPair<Object> OBJECT_SERIALIZATION_PAIR = RedisSerializationContext.SerializationPair.fromSerializer(GENERIC_FAST_JSON_REDIS_SERIALIZER);

    public WeeRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = super.getCache(name);
        return new RedisCacheWrapper(cache);
    }

    @Override
    public void afterPropertiesSet() {
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        for (String beanName : beanNames) {
            final Class clazz = applicationContext.getType(beanName);
            add(clazz);
        }
        super.afterPropertiesSet();
    }

    @Override
    protected Collection<RedisCache> loadCaches() {
        List<RedisCache> caches = new LinkedList<>();
        for (Map.Entry<String, RedisCacheConfiguration> entry : initialCacheConfiguration.entrySet()) {
            caches.add(super.createRedisCache(entry.getKey(), entry.getValue()));
        }
        return caches;
    }

    private void add(final Class clazz) {
        ReflectionUtils.doWithMethods(clazz, method -> {
            ReflectionUtils.makeAccessible(method);
            WeeCacheExpire cacheExpire = AnnotationUtils.findAnnotation(method, WeeCacheExpire.class);
            if (cacheExpire == null) {
                return;
            }
            Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
            if (cacheable != null) {
                add(cacheable.cacheNames(), cacheExpire);
                return;
            }
            Caching caching = AnnotationUtils.findAnnotation(method, Caching.class);
            if (caching != null) {
                Cacheable[] cs = caching.cacheable();
                if (cs.length > 0) {
                    for (Cacheable c : cs) {
                        if (c != null) {
                            add(c.cacheNames(), cacheExpire);
                        }
                    }
                }
            } else  {
                CacheConfig cacheConfig = AnnotationUtils.findAnnotation(clazz, CacheConfig.class);
                if (cacheConfig != null) {
                    add(cacheConfig.cacheNames(), cacheExpire);
                }
            }
        }, method -> null != AnnotationUtils.findAnnotation(method, WeeCacheExpire.class));
    }

    private void add(String[] cacheNames, WeeCacheExpire cacheExpire) {
        for (String cacheName : cacheNames) {
            if (cacheName == null || "".equals(cacheName.trim())) {
                continue;
            }
            long expire = cacheExpire.expire();
            log.info("cacheName: {}, expire: {}", cacheNames, expire);
            if (expire >= 0) {
                RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofSeconds(expire))
                        .disableCachingNullValues()
                        .serializeKeysWith(STRING_SERIALIZATION_PAIR)
                        .serializeValuesWith(OBJECT_SERIALIZATION_PAIR);
                initialCacheConfiguration.put(cacheName, configuration);
            } else  {
                log.warn("{} use default expiration.", cacheName);
            }
        }
    }

    protected static class RedisCacheWrapper implements Cache {
        private final Cache cache;

        public RedisCacheWrapper(Cache cache) {
            this.cache = cache;
        }

        @Override
        public String getName() {
            try {
                return cache.getName();
            } catch (Exception e) {
                log.error(e);
                return null;
            }
        }


        @Override
        public Object getNativeCache() {
            try {
                return cache.getNativeCache();
            } catch (Exception e) {
                log.error(e);
                return null;
            }
        }

        @Override
        public ValueWrapper get(Object o) {
            try {
               return cache.get(o);
            } catch (Exception e) {
                log.error(e);
                return null;
            }
        }

        @Override
        public <T> T get(Object o, Class<T> aClass) {
            try {
                return cache.get(o, aClass);
            } catch (Exception e) {
                log.error(e);
                return null;
            }
        }

        @Override
        public <T> T get(Object o, Callable<T> callable) {
            try {
                return cache.get(o, callable);
            } catch (Exception e) {
                log.error(e);
                return null;
            }
        }

        @Override
        public void put(Object o, Object o1) {
            try {
                cache.put(o, o1);
            } catch (Exception e) {
                log.error(e);
            }
        }

        @Override
        public void evict(Object o) {
            try {
                cache.evict(o);
            } catch (Exception e) {
                log.error(e);
            }
        }

        @Override
        public void clear() {
            try {
                cache.clear();
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

}
