package com.gaocs.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller
 * @Description:
 * @time:2020/1/7 11:13
 */
@RestController
public class LoginController {
    @RequestMapping("/")
    public String index(@RequestParam String sss){
        System.out.println("请求参数："+ sss);
        return "index";
    }

    public static void main(String[] args) {
        Map<String,Integer> a = new HashMap <>();
        a.put(null,1);
        System.out.println(a.get(null));
    }
}
