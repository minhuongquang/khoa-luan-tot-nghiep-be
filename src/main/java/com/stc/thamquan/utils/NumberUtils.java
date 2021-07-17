package com.stc.thamquan.utils;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/1/21
 * Time      : 17:37
 * Filename  : NumberUtils
 */
public class NumberUtils {
    /***
     * @author: Làm tròn số đến 2 chữ số
     * @param rate
     * @return
     */
    public static double myRound(double rate) {
        return (double) Math.round(rate * 100.0) / 100;
    }
}
