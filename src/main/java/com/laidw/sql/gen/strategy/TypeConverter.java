package com.laidw.sql.gen.strategy;

/**
 * 将SQL中的数据类型转成Java类型
 *
 * @author NightDW 2023/9/3 15:34
 */
public interface TypeConverter {

    /**
     * 将SQL中的数据类型转成Java类型
     *
     * @param dataType {@link java.sql.Types}
     */
    Class<?> toJavaType(int dataType);
}
