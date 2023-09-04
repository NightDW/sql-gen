package com.laidw.sql.gen.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据库表的列信息
 *
 * @author NightDW 2023/9/1 11:13
 */
@Getter
@AllArgsConstructor
public class Column {
    private final String name;
    private final int dataType;
}
