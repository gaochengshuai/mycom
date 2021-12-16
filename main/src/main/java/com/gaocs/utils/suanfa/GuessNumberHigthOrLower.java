package com.gaocs.utils.suanfa;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.utils.suanfa
 * @Description:
 *  1、猜大小，并且错误给大小提示，错误扣除对应的金额
 *  2、永远最倒霉的情况下，也能赢得胜利，所需要的最少钱数
 * @time:2021/12/16 22:06
 */
public class GuessNumberHigthOrLower {
    //正确的数字，在1-n之间
    //每次猜错，花费就是猜错的数字
    //返回：永远最倒霉的情况下，也能赢得胜利，所需要的最少钱数
    public static int minAmt(int n){
        return zuo(1,n);
    }
    //目前锁定了范围 L-R,除了这个范围都不可能了
    public static int zuo(int L,int R){
        //终止条件
        if(L == R){//如果猜对不付钱
            return 0;
        }
        if(L == R-1){ // 3,4 6,7
            return L;
        }
        //L...R 不止两个数
        //只猜L
        int p1 = L + zuo(L+1,R);
        //只猜R
        int p2 = L + zuo(L,R-1);
        int min = Math.min(p1,p2);
        //L...R
        for (int i = L + 1; i < R; i++) {
            //i猜的数字，每一个都试试
            //L..... i .......R
            int left = zuo(L,i-1);
            int rigth = zuo(i+1, R);
            int cur = i + Math.max(left,rigth);//永远最倒霉，所以是最大值
            min = Math.min(min,cur);//所有决策中选最小的
        }
        return min;
    }
    // 二维数组
    // * * *
    // * * *
    // * * *
    //改造递归，暴力递归
    public static int minAmt2(int n){
        //L -> 1-N
        //R -> 1-N
        int[][] dp = new int[n+1][n+1];
        //因为初始化都是0，所以dp的对角线，不用填了
        for (int i = 1; i < n; i++) {
            dp[i][i+1] = i;//不到0行0列
        }
        for (int L = n - 2; L >= 1; L--) {
            for (int R = L + 2; R <= n; R++) {
                int min = Math.min(L + dp[L + 1][R], R + dp[L][R - 1]);
                for (int i = L + 1; i < R; i++) {
                    min = Math.min(min, i + Math.max(dp[L][i - 1], dp[i + 1][R]));
                }
                dp[L][R] = min;
            }
        }
        return dp[1][n];
    }
}
