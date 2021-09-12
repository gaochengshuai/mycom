package com.gaocs.controller.test;

import java.util.concurrent.Exchanger;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description: 两线程之间的交换器
 * @time:2020/11/10 16:23
 */
public class ThreadExchanger {
    private static Exchanger<String> exchanger = new Exchanger <>();

    public static void main(String[] args) {
        new Thread(()->{
            String s = "t1";
            try {
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " " + s);
        },"第一个线程").start();

        new Thread(()->{
            String s = "t2";
            try {
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " " + s);
        },"第二个线程").start();
    }
}
