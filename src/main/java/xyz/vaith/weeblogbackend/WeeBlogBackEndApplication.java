package xyz.vaith.weeblogbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("xyz.vaith.weeblogbackend.mapper")
@EnableTransactionManagement
public class WeeBlogBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeeBlogBackEndApplication.class, args);
    }

}
