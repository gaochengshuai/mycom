package com.gaocs.controller.test;

import java.util.concurrent.TimeUnit;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description:
 * @time:2021/4/7 10:40
 */
public class ThreadLocal1 {
    // volatile static Person person = new Person();
    static ThreadLocal<Person> persont = new ThreadLocal <>();
    public static void main(String[] args) {
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(persont.get());
        }).start();

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // person.name = "lisi";
            persont.set(new Person());
            System.out.println(persont.get()+"-------");
        }).start();
    }
    static class Person{
        String name = "zhangsan";
    }
}
