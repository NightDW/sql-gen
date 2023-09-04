package com.laidw.sql.gen.strategy.impl;

import com.laidw.sql.gen.constant.AggregateType;
import com.laidw.sql.gen.entity.AggregateColumn;
import com.laidw.sql.gen.strategy.AggregateColumnAliasSetter;
import com.laidw.sql.gen.util.StringUtil;

/**
 * Description of class {@link SimpleAggregateColumnAliasSetter}.
 *
 * @author NightDW 2023/9/4 2:49
 */
public class SimpleAggregateColumnAliasSetter implements AggregateColumnAliasSetter {
    @Override
    public void set(AggregateColumn aggregateColumn) {
        if (StringUtil.isEmpty(aggregateColumn.getAlias())) {
            aggregateColumn.setAlias(generateAlias(aggregateColumn));
        }
    }

    private String generateAlias(AggregateColumn ac) {

        // 如果是COUNT(1)或COUNT(*)类型，则直接返回"count"
        if (ac.getType() == AggregateType.COUNT && StringUtil.isEmpty(ac.getTableAlias())) {
            return "count";
        }

        // 否则，通过聚合类型、表的别名、原始列名来生成别名
        return ac.getType().name().toLowerCase() + '_' + ac.getTableAlias() + '_' + ac.getColumnName();
    }
}
