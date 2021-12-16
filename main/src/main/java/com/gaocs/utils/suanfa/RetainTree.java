package com.gaocs.utils.suanfa;


import com.graphbuilder.struc.LinkedList;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.utils.suanfa
 * @Description: 找出一棵树上黑色节点，最后只包含黑色节点的树
 * @time:2021/12/16 21:20
 */
public class RetainTree {
    public static class Node {
        public int value; //值
        public boolean retain; //是否保留
        public List<Node> nexts; //下级节点

        public Node(int value, boolean retain) {
            this.value = value;
            this.retain = retain;
            this.nexts = new ArrayList <>();
        }
    }

    /**
     * 1、给定一棵树的头节点head
     * 2、按照题义，保留节点，否则删除节点
     * 3、树调整完之后，返回头节点
     * */
    public static Node retain(Node x){
        //先断路
        if(x.nexts.isEmpty()){
            return x.retain ? x : null;
        }
        //x下层有节点
        List<Node> newNexts = new ArrayList <>();
        for (Node next : x.nexts) {
            //如果树下有其它节点
            Node newNode = retain(next);
            if(newNode != null) {
                newNexts.add(next);
            }
        }
        //x.nexts 老的链表，下级节点
        //newNexts 新的链表,只有保留的在里面
        if(!newNexts.isEmpty() || x.retain){
            x.nexts = newNexts;
            return x;
        }
        return null;
    }

}
