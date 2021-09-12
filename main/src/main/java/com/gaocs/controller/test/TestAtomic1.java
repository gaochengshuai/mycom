package com.gaocs.controller.test;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description: long atomicLong、longAdder
 * @time:2020/8/31 18:09
 */
public class TestAtomic1 {
    static long countl = 0L;
    static AtomicLong countAl = new AtomicLong(0L);
    static LongAdder countLa = new LongAdder();

    public static void main(String[] args) throws Exception{

        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() ->{
                for (int j = 0; j < 100000; j++) {
                    countAl.incrementAndGet();
                }
            });
        }
        //Atomic
        long start = System.currentTimeMillis();
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        long end = System.currentTimeMillis();
        System.out.println("Atomic:"+ countAl.get() + " time:" + (end-start));
        System.out.println("=================================================");

        Object locko = new Object();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000000; j++) {
                        synchronized (locko){
                            countl++;
                        }
                    }
                }
            });
        }
        start = System.currentTimeMillis();
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        end = System.currentTimeMillis();
        System.out.println("sychronized:"+ countl + " time:" + (end-start));
        System.out.println("=================================================");

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j=0; j<100000; j++) countLa.increment();
            });
        }
        start = System.currentTimeMillis();
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        end = System.currentTimeMillis();
        System.out.println("LongAdder:"+ countLa.longValue() + " time:" + (end-start));
    }
}
