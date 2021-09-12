package com.gaocs.controller.test;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description:
 * @time:2021/4/14 15:37
 */
public class TLinkedBlockingQueue {
    static BlockingQueue<String> strs = new LinkedBlockingDeque <>();
    static Random r = new Random();
    public static void main(String[] args) {
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    strs.put("a" + i);
                    TimeUnit.MICROSECONDS.sleep(r.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"p1").start();
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                for (;;){
                    try {
                        System.out.println(Thread.currentThread().getName() + "take - " + strs.take());//如果空了就等待
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },"c"+i).start();
        }
    }
}
