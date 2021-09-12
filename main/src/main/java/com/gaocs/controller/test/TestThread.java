package com.gaocs.controller.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description: 测试线程
 * @time:2020/8/10 10:12
 */
public class TestThread {
    static class myThread extends Thread {
        @Override
        public void run() {
            System.out.println("myThread!");
        }
    }
    static class myRun implements Runnable {
        @Override
        public void run() {
            System.out.println("myRun!  实现Runnable.run()方法");
        }
    }
    static class myCall implements Callable<String>{

        @Override
        public String call() throws Exception {
            System.out.println("myCall!");
            return "success";
        }
    }

    public static void main(String[] args) {
        //5种线程启动方式

        System.out.println("----------"+System.currentTimeMillis());
        new myThread().start();
        System.out.println("---第一种---"+System.currentTimeMillis());
        new Thread(new myRun()).start();
        System.out.println("---第二种---"+System.currentTimeMillis());
        new Thread(()->{
            System.out.println("lambda 打印！"+System.currentTimeMillis());
        }).start();//用lambda来启动线程时间比较耗时
        Thread t = new Thread(new FutureTask<String>(new myCall()));// 实际是实现RunnableFuture（继承Runnable）接口run方法
        t.start();
        System.out.println("--第四种--" + System.currentTimeMillis());
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(()-> System.out.println("lambda threadPool --" + System.currentTimeMillis()));
        es.shutdown();
    }
}
