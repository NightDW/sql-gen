package com.laidw.sql.gen.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表的连接类型，包括内连接、左连接、右连接
 *
 * @author NightDW 2023/9/1 1:50
 */
public enum JoinType {
    INNER_JOIN("INNER JOIN "),
    LEFT_JOIN("LEFT JOIN "),
    RIGHT_JOIN("RIGHT JOIN "),
    ;

    public final String sqlFragment;
    JoinType(String sqlFragment) {
        this.sqlFragment = sqlFragment;
    }

    public static List<String> names() {
        return Arrays.stream(values()).map(JoinType::name).collect(Collectors.toList());
    }
}
