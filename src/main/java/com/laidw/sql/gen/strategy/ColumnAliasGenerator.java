package com.laidw.sql.gen.strategy;

import java.util.List;

/**
 * 为SELECT/WHERE列起别名
 *
 * @author NightDW 2023/9/3 14:43
 */
public interface ColumnAliasGenerator {

    /**
     * 为SELECT/WHERE列起别名
     *
     * @param columns 元素的格式为alias.column_name
     */
    List<String> generate(List<String> columns);
}
