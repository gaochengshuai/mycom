package com.gaocs.controller.test;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.fasterxml.jackson.databind.deser.std.ArrayBlockingQueueDeserializer;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description:
 * @time:2021/4/14 14:12
 */
public class TConcurrentQueue {
    static BlockingQueue<String> q1 = new ArrayBlockingQueue<String>(1);
    static BlockingQueue<String> q2 = new ArrayBlockingQueue<String>(1);
    static Thread t1 = null, t2 = null;

    public static void main(String[] args) {
//        Queue<String> strs = new ConcurrentLinkedDeque <>();
//        for (int i = 0; i < 10;i++) {
//            strs.offer("+" + i);
//        }
//        System.out.println(strs);
//        System.out.println(strs.size());
//
//        System.out.println("poll:" + strs.poll()); //取数据并且remove掉
//        System.out.println("大小" + strs.size());
//
//        System.out.println(strs.peek()); //只取数据
//        System.out.println(strs.size());
        char[] ai = "1234567".toCharArray();
        char[] ac = "ABCDEF".toCharArray();
        t1 = new Thread(() ->{
            for(char a : ac){
                System.out.println(a);
                try {
                    q1.put("ok");
                    q2.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t1");
        t2 = new Thread(() ->{
            for(char b : ai){
                System.out.println(b);
                try {
                    q2.put("ok");
                    q1.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t2");
        t1.start();
        t2.start();
    }
}
