package com.laidw.sql.gen.strategy.impl;

import com.laidw.sql.gen.strategy.WhereClauseGenerator;
import com.laidw.sql.gen.util.StringUtil;

/**
 * 简单的WHERE条件的生成器，适合用于Mybatis
 *
 * @author NightDW 2023/9/2 12:11
 */
public class MybatisWhereClauseGenerator implements WhereClauseGenerator {
    private static final String TEMPLATE = "<if test='%s != null'>AND %s = #{%s}</if>";

    @Override
    public String generate(String column, String alias) {
        String property = StringUtil.toCamelCase(alias);
        return String.format(TEMPLATE, property, column, property);
    }
}
