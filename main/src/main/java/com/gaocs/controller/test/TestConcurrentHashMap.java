package com.gaocs.controller.test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description: 测试并发多线程效率
 * @time:2021/4/7 11:21
 */
public class
TestConcurrentHashMap {
    static final int count = 10000000;//测试个数
    static final int thredCount = 100;//测试线程个数
    static Map<UUID,UUID> m = new ConcurrentHashMap <>();//测试的容器
    static UUID[] keys = new UUID[count]; //并发线程key
    static UUID[] values = new UUID[count]; //并发线程values

    //给K,V赋值
    static {
        for (int i = 0; i < count; i++) {
            keys[i] = UUID.randomUUID();
            values[i] = UUID.randomUUID();
        }
    }

    static class MyThread extends Thread {
        int start;
        int gap = count/thredCount;

        public MyThread(int start){
            this.start = start;
        }

        @Override
        public void run() {
            for (int i = start; i < start+gap; i++) {
                m.put(keys[i],values[i]);
            }
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Thread[] threads = new Thread[thredCount];

        for (int i = 0; i < threads.length ; i++) {
            threads[i] = new MyThread(i * (count/thredCount));
        }

        for (Thread r : threads) {
            r.start();
        }

        for (Thread r : threads) {
            try {
                r.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("总耗时：" + (end - start));
        System.out.println("总个数：" + m.size());

        start = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                for (int j = 0; j < 1000000; j++) {
                    m.get(keys[10]);
                }
            });
        }
        for (Thread r : threads) {
            r.start();
        }

        for (Thread r : threads) {
            try {
                r.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
