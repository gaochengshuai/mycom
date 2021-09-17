package com.gaocs.utils;

import java.util.Scanner;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.utils
 * @Description:
 * @time:2021/9/17 21:16
 */
public class Autosend {
    public void start(String[] args){
        String a = "";
        String b = "";
        if(args.length >= 2){
            a  = args[0];
            System.out.println("第一个参数：" + a );
            b  = args[1];
            System.out.println("第二个参数：" + b );
        }
        while (true){
            Scanner xx = new Scanner(System.in);
            System.out.println("想传什么值？");
            String out = xx.next();
            System.out.println("::" + out);
            if (out != null && out.equals("exit")){
                System.exit(0);
            }
        }
    }
}
