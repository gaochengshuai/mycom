package com.gaocs.controller.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description:  reentrankLock 可重入锁及锁当前all还可以同样对这把锁再锁一下
 * @time:2020/9/9 15:38
 */
public class TestReentrantLock {
    synchronized void m1(){
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("第:"+i+"次");
            if(i==2) m2();
        }
    }

    synchronized void m2() {
        System.out.println("m2----");
    }
//   m3/m4用于测试reentrankLock也是可重入锁
    Lock lock = new ReentrantLock();
    void m3(){
        lock.lock(); // 等同于synchronized(this)

        try {
            for (int i = 0; i < 10; i++) {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("m3 第:" + i + "次");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//手动解锁
        }
    }
    void m4(){
        try {
            lock.lock();
            System.out.println("m4-------");
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        TestReentrantLock t = new TestReentrantLock();
//        new Thread(t::m1).start();
//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //new Thread(t::m2).start();
        new Thread(t::m3).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(t::m4).start();
    }
}
