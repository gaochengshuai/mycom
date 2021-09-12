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
public class TestReentrantLock2 {

    Lock lock = new ReentrantLock();
    void m3(){
        lock.lock(); // 等同于synchronized(this)
        try {
            for (int i = 0; i < 3; i++) {
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
        boolean flag = false;
        try {
            flag = lock.tryLock(2,TimeUnit.SECONDS);
            System.out.println("m4---" + flag);
        } catch (Exception e ){
            e.printStackTrace();
        } finally {
            if (flag)lock.unlock();
        }
    }

    public static void main(String[] args) {
        TestReentrantLock2 t = new TestReentrantLock2();
        new Thread(t::m3).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(t::m4).start();
    }
}
