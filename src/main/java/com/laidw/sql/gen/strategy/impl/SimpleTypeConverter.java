package com.laidw.sql.gen.strategy.impl;

import com.laidw.sql.gen.strategy.TypeConverter;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 简单的类型转换器
 *
 * @author NightDW 2023/9/3 15:37
 */
@Setter
@Getter
public class SimpleTypeConverter implements TypeConverter {
    private Map<Integer, Class<?>> mapping = new HashMap<>();
    {
        mapping.put(Types.BIT, Integer.class);
        mapping.put(Types.TINYINT, Integer.class);
        mapping.put(Types.SMALLINT, Integer.class);
        mapping.put(Types.INTEGER, Integer.class);
        mapping.put(Types.BIGINT, Long.class);
        mapping.put(Types.FLOAT, Float.class);
        mapping.put(Types.DOUBLE, Double.class);
        mapping.put(Types.DECIMAL, BigDecimal.class);
        mapping.put(Types.CHAR, String.class);
        mapping.put(Types.VARCHAR, String.class);
        mapping.put(Types.DATE, Date.class);
        mapping.put(Types.TIME, Time.class);
        mapping.put(Types.TIMESTAMP, Timestamp.class);
    }

    @Override
    public Class<?> toJavaType(int dataType) {
        return mapping.getOrDefault(dataType, Object.class);
    }
}
