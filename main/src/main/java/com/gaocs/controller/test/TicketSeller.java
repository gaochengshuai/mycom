package com.gaocs.controller.test;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description:
 * @time:2021/4/7 14:12
 */
public class TicketSeller {
//    static List <String> tickets = new ArrayList <>();
    static Vector <String> tickets = new Vector <>();//vector 中有很多方法加了synchronized, size() 和 remove中间没有加锁
    static {
        for (int i = 0; i < 100; i++) {
            tickets.add("票号:" + i);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                while (tickets.size() > 0){
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("售了："+ tickets.remove(0));
                }
            }).start();
        }
    }
}
