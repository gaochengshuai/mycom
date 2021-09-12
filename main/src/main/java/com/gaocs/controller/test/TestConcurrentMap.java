package com.gaocs.controller.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description:
 * @time:2021/4/9 17:56
 */
public class TestConcurrentMap {
    public static void main(String[] args) {
//        Map<String,String> map = new ConcurrentHashMap <>();
//        Map<String,String> map = new ConcurrentSkipListMap <>();//高并发且排序

//        Map<String,String> map = new Hashtable <>();
        Map<String,String> map = new HashMap <>();
        Random r = new Random();
        Thread[] threads = new Thread[100];
        CountDownLatch latch = new CountDownLatch(threads.length);
        long start = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                for (int j = 0; j < 10000; j++) {
                    map.put("a" + r.nextInt(100000),"a" + r.nextInt(100000));
                }
                latch.countDown();
            });
        }
        Arrays.asList(threads).forEach(t->t.start());
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        System.out.println(map.size());
    }
}
