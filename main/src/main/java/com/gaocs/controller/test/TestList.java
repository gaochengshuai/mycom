package com.gaocs.controller.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description:
 * @time:2021/6/9 17:36
 */
public class TestList {
    public static void main(String[] args) {
        // 库位码排序
        List <String> list=new ArrayList <>();
        list.add("M04-002-001");
        list.add("M3-602-001");
        list.add("M104-003-001");
        list.add("M103-002-11");
        list.add("M18-002-001");
        list.add("J01-002-001");
        list.add("A101-052-001");
        // output:
        // A101-052-001
        // J01-002-001
        // M3-602-001
        // M04-002-001
        // M18-002-001
        // M103-002-11
        // M104-003-001

    }

}
