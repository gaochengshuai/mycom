package com.gaocs.controller.test;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description:
 * @time:2021/4/9 15:10
 */
public class TicketQueue {
    static Queue<String> ticket = new ConcurrentLinkedDeque <>();

    static {
        for (int i = 0; i < 100; i++) {
            ticket.add("票编号：" + i);
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                while (true){
                    String s  = ticket.poll();
                    if( s == null){
                        break;
                    } else {
                        System.out.println("销售了---" + s);
                    }
                }
            }).start();
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时："+(end-start));
    }
}
