package com.stc.thamquan.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 11:17
 * Filename  : StringUtils
 */
public class StringUtils {
    public static String removeAccentJava(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D');
    }
}
