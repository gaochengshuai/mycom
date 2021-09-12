package com.gaocs.base;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.*;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.base
 * @Description:
 * @time:2021/9/12 22:31
 */
public class Mapzhuan {
    public static void main(String[] args) {
        List<String> listBase = new ArrayList <>();
        listBase.add("productNo");
        listBase.add("orgId");
        listBase.add("userId");
        Map<String,Object> map = new HashMap <>();
        for (int i = 0; i <5 ; i++) {
            if((i&1) ==1){
                map = resultRes("credit");
                System.out.println("奇数：" + map);
            } else {
                map = resultRes("apply");
                System.out.println("偶数：" + map);
            }
        }
    }
    public static Map<String,Object> resultRes(String eventCode){
        Map<String,Object> map = new HashMap <>();
        int eventType = 0;
        switch (eventCode){
            case "credit" : eventType =getRequest(eventCode,1); break;
            case "apply" : eventType = getRequest(eventCode,0); break;
        }
        map.put("ret",eventType);
        return map;
    }
    public static int getRequest(String eventCode,int flag){
        //如果https请求成功变量加1
        if(true&& "credit".equals(eventCode)){return flag+=1;} return flag;
    }
}
