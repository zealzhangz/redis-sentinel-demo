package com.zealzhangz.redissentineldemo;

import com.zealzhangz.redissentineldemo.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created by zealzhangz.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/07/30 17:02:00<br/>
 */
@RestController
public class TestController {
    @Autowired
    private RedisService redisService;

    @GetMapping("/test")
    public String test(){
        redisService.save("aaaa","bbbb",60);
        return "success";
    }
}
