package com.laidw.sql.gen.constant;

import com.laidw.sql.gen.exception.SqlGenException;

/**
 * 聚合类型
 *
 * @author NightDW 2023/9/4 1:36
 */
public enum AggregateType {
    COUNT, MAX, MIN, AVG, SUM;

    public static AggregateType ofNameIgnoreCase(String name) {
        for (AggregateType value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        throw new SqlGenException("未知的聚合类型：" + name);
    }
}
