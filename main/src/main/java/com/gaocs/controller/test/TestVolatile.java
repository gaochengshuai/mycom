package com.gaocs.controller.test;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description: 测试volatile的特性
 *  volatile 是为了保证变量在多线程都可见，但是不能保证多个线程同时修改 runing变量时所带来的一致性问题，
 *  也就是说不能替代synchronized
 * @time:2020/8/31 10:21
 */
public class TestVolatile {
    private static /*volatile*/ TestVolatile INSTANCE;

    public TestVolatile() {
    }
    public static TestVolatile getInstance(){
        if(INSTANCE == null){
            //双重检查
            synchronized (TestVolatile.class){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            INSTANCE = new TestVolatile();
        }
        return INSTANCE;
    }

    public static void main(String[] args) {
        for (int i=0; i<100;i++){
            new Thread(()-> System.out.println(TestVolatile.getInstance().hashCode())).start();
        }
    }

}
