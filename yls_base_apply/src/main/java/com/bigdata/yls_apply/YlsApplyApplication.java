package com.bigdata.yls_apply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class YlsApplyApplication {

    public static void main(String[] args) {
        SpringApplication.run(YlsApplyApplication.class, args);
    }

    @RequestMapping("/hello")
    public String hello() {
        System.out.println("你好！");
        return "hello";
    }
}
