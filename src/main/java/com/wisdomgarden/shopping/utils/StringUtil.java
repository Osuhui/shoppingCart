package com.wisdomgarden.shopping.utils;

/**
 * @Function
 * @Author huiwl
 * @Date 2022/7/17
 **/
public class StringUtil {
    private StringUtil() {
    }

    /**
     * 比较字符串是否相同
     *
     * @param s1 s1
     * @param s2 s2
     * @return boolean
     */
    public static boolean equals(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return false;
        }
        return s1.equals(s2);
    }

    /**
     * 比较字符串是否相同
     *
     * @param s1 s1
     * @param s2 s2
     * @return boolean
     */
    public static boolean equalsAll(String s1, String[] s2) {
        if (s1 == null || s2 == null) {
            return false;
        }
        for (String s : s2) {
            if (!s.equals(s1)) {
                return false;
            }
        }
        return true;
    }
}
