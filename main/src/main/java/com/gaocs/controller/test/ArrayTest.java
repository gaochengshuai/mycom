package com.gaocs.controller.test;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.controller.test
 * @Description:
 * @time:2021/6/8 17:27
 */
public class ArrayTest {
    public static void main(String[] args) {
        System.out.println(reStr("aaabb"));
        System.out.println(isS("abcba"));
        System.out.println(isS("a"));
        System.out.println(isS(""));
    }
    /**
     * 字符串压缩，进行输出
     * */
    private static String reStr(String str){
        String result = "";
        char[] c1 = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            int count = 1;
            if(i != str.length()-1){
                while (c1[i] == c1[i+count]){
                    count++;
                    if(i+count == c1.length){
                        break;
                    }
                }
            }
            result = result + c1[i] +count;
            i = i+count-1;
        }
        return result.length() < str.length() ? result : str;
    }

    /**
     * 判断字符串是否对称
     * */
    private static boolean isS(String str){
        if(str.length() == 0){
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i) != str.charAt(str.length() - i -1)){
                return false;
            }
        }
        return true;
    };
}
