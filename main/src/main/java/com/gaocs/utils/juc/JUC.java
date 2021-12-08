package com.gaocs.utils.juc;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.utils
 * @Description: java.util.concurrent 说明
 * 包含原子类、锁类、工具类（线程同步结构、线程池等等）
 * 1、线程状态
 *  boolean status;
 * 2、线程怎样保证完全性
 *  native CAS   c++中的 lock cmpxchg
 * 3、如何处理获取不到锁的线程？
 *  自旋（线程少，并发小）、阻塞（当自旋锁的时间大于线程切换时间用阻塞） 自旋+阻塞
 * 4、如何释放锁
 *  自旋：抢锁
 *  阻塞：唤醒
 * @time:2021/12/8 22:23
 */
public class JUC {

}
