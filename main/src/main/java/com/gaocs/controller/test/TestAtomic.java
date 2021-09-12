package com.gaocs.controller.test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description: Atomic类原子性
 * @time:2020/8/31 14:17
 */
public class TestAtomic {
    /*volatile*/ //int count1 = 0;
    AtomicInteger count = new AtomicInteger(0);
    /*synchronized*/void m(){
        for (int i=0;i<10000;i++){
            count.incrementAndGet(); //count1++
        }
    }

    public static void main(String[] args) throws Exception {
       /* TestAtomic t = new TestAtomic();
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    t.m();
                }
            }, "thread" + i));
        }
        threads.forEach((o) -> o.start());
        threads.forEach((o) -> {
            try{
                o.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });
        System.out.println(t.count);*/

        AtomicInteger count = new AtomicInteger(0);
        count.incrementAndGet();
        if(count.compareAndSet(1,1)){
            System.out.println("======="+count);
            count.set(0);
        }
        System.out.println("count:"+count);

    }
}
