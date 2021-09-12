package com.gaocs.controller.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description: 门闩
 * @time:2021/4/12 9:57
 */
public class TestCountDownLatch {
    // 添加volatile为了让t2能收到通知
    volatile List lists = new ArrayList();
    public void add(Object o){
        lists.add(o);
    }
    public int size(){
        return lists.size();
    }

    public static void main(String[] args) {
        TestCountDownLatch tc =  new TestCountDownLatch();
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(()->{
            System.out.println("开始t2线程启动");
            if(tc.size() != 5){
                try {
                    System.out.println("---------------------");
                    latch.await();
                    System.out.println("+++++++++++");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("t2结束！");
        },"t2").start();

//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        new Thread(()->{
            System.out.println("启动t1线程");
            for (int i = 0; i < 10; i++) {
                tc.add(new Object());
                System.out.println("添加第几个对象："+i);
            }
            if(tc.size() == 5){
                //把门闩打开，让t2执行
                latch.countDown();
                System.out.println("把线程门闩打开");
                try {
                    // 给t1上门闩，让t2有机会执行
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            try {
//                TimeUnit.SECONDS.sleep(5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        },"t1").start();
    }
}
