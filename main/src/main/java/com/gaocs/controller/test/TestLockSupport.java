package com.gaocs.controller.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description: LockSupport park()和unpark()
 * @time:2021/4/12 10:39
 */
public class TestLockSupport {
    volatile List lists = new ArrayList();
    public void add(Object o){
        lists.add(o);
    }
    public int size(){
        return lists.size();
    }
    static Thread t1;
    static Thread t2;

    public static void main(String[] args) {
        TestLockSupport c = new TestLockSupport();
        t2 = new Thread(()->{
            System.out.println("t2启动");
            if(c.size() != 5){
                LockSupport.park();
            }
            System.out.println("t2结束");
            LockSupport.unpark(t1);
        },"t2");

        t1 = new Thread(()->{
            System.out.println("t1启动");
            for (int i = 0; i < 10; i++) {
                c.add(new Object());
                System.out.println("add " + i);
                if (c.size() == 5){
                    LockSupport.unpark(t2);
                    LockSupport.park();
                }
            }
            System.out.println("t1 结束");
        },"t1");

        t2.start();
        t1.start();
    }
}
