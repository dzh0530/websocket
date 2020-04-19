package com.example.demo.websocket;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ip {
    public static int num;

    //提前五分钟转换拨号服务器
    @Async
    @Scheduled(cron = "55 * * * * *")
    public void ip() {
        System.out.println("一号");
        num = 1;
    }


    @Async
    @Scheduled(cron = "25 * * * * *")
    public void ip2() {
        System.out.println("二号");
        num = 2;
    }
}
