package com.laidw.sql.gen.entity;

import com.laidw.sql.gen.constant.AggregateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 聚合列信息
 *
 * @author NightDW 2023/9/4 1:39
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
public class AggregateColumn {

    /**
     * 聚合类型
     */
    private AggregateType type;

    /**
     * 表的别名，比如COUNT(usr.user_id)中的usr
     */
    private String tableAlias;

    /**
     * 列名，比如COUNT(usr.user_id)中的user_id
     */
    private String columnName;

    @Override
    public String toString() {
        return type.name() + '(' + tableAlias + '.' + columnName + ")";
    }
}
