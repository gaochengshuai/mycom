package com.gaocs.controller.test;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description:
 * @time:2021/1/6 13:51
 */
public class ThreadLocalTest {
    public static void main(String[] args) {
        // 强引用 方法不结束，永远不回收
        Object o = new Object();
        // 软引用
        SoftReference <Object> sf = new SoftReference <Object>(o);
        System.out.println("软引用:" + sf.get());
        o = null;
        sf.get(); //sf是对o的一个软引用，当内存空间不足时，这个软引用会不存在
        System.out.println("软第二次:" + sf.get());
        //弱引用
        Object c = new Object();
        WeakReference <Object> wf = new WeakReference <>(c);
        System.out.println("弱引用:" + wf.get());
        o = null;
        wf.get(); //有时候为空，
        System.out.println("弱第二次:" + wf.get() + ",垃圾回收标记为:" + wf.isEnqueued());
        wf.isEnqueued(); //返回是否被垃圾回收器标记为即将回收的垃圾
        System.out.println("弱第三次:" + wf.get() + ",垃圾回收标记为:" + wf.isEnqueued());
        //虚引用
        Object obj = new Object();
        PhantomReference <Object> pr = new PhantomReference <Object>(obj,new ReferenceQueue <>());
        System.out.println("虚引用:" + pr.get());
        obj = null;
        pr.get();//永远返回null
        System.out.println("弱第二次:" + pr.get() + ",垃圾回收标记为:" + pr.isEnqueued());
        pr.isEnqueued();//返回内存中是否已经删除

    }

}
