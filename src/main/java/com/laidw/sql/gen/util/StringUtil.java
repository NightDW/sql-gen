package com.laidw.sql.gen.util;

import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * 字符串操作
 *
 * @author NightDW 2023/9/2 11:50
 */
public class StringUtil {

    public static void append(StringBuilder sb, String prefix, String separator, String suffix, Collection<String> contents) {
        if (CollectionUtils.isEmpty(contents)) {
            return;
        }
        sb.append(prefix);
        for (String content : contents) {
            sb.append(content).append(separator);
        }
        sb.replace(sb.length() - separator.length(), sb.length(), suffix);
    }

    public static void append(StringBuilder sb, String prefix, String separator, String suffix, String... contents) {
        append(sb, prefix, separator, suffix, Arrays.asList(contents));
    }

    public static String toCamelCase(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        boolean upperFlag = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                upperFlag = true;
            } else if (upperFlag) {
                upperFlag = false;
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String deletePrefix(String str, String prefix) {
        return prefix != null && str.startsWith(prefix) ? str.substring(prefix.length()) : str;
    }

    /**
     * 对下划线命名的字符串进行压缩，比如：user_class_rel -> ucr
     */
    public static String compact(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        boolean isCandidate = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                isCandidate = true;
            } else if (isCandidate) {
                sb.append(c);
                isCandidate = false;
            }
        }
        return sb.toString();
    }

    /**
     * 获取原始列名，比如：user.user_id -> user_id
     */
    public static String getOriginalName(String s) {
        return s.substring(s.indexOf('.') + 1);
    }
}
