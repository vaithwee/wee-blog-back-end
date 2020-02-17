package xyz.vaith.weeblogbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("xyz.vaith.weeblogbackend.mapper")
public class WeeBlogBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeeBlogBackEndApplication.class, args);
    }

}
