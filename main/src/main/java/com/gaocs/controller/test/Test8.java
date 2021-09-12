package com.gaocs.controller.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description: jdk8 特性
 * @time:2020/8/10 10:58
 */
public class Test8 {
    private static Map <String, Integer> items = new HashMap <>();
    private static List <String> lists = new ArrayList <>();

    static {
        items.put("A", 10);
        items.put("B", 20);
        items.put("C", 30);
        items.put("D", 40);
        items.put("E", 50);
        items.put("F", 60);
    }

    static {
        lists.add("A");
        lists.add("BC");
        lists.add("C");
        lists.add("BD");
        lists.add("E");
        lists.add("A");
    }

    public static void main(String[] args) {
        items.forEach((k,v)-> System.out.println("key:"+ k +" value:"+v));
        lists.forEach(list->{
            if("C".equals(list)){
                System.out.println("-------" + list);
            }
        });
        //先过滤
        lists.stream().filter(s -> s.contains("B")).forEach(result-> System.out.println(result));
        lists.stream().distinct().forEach(result -> System.out.println("去重后的结果:" + result));
        Stream <String> a = lists.stream().filter(s -> Boolean.parseBoolean(s.trim())).sorted();
        System.out.println(a.toString());
    }
}
