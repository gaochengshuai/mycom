package com.gaocs.controller.test;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description: 同步锁测试类
 * @time:2020/8/10 15:57
 */
public class TestSynchronized {

    synchronized void m1(){  // 等同于 synchronized void m()
        System.out.println("m1 start!!!");
    }
    synchronized void m2(){
        System.out.println("");
    }
}
