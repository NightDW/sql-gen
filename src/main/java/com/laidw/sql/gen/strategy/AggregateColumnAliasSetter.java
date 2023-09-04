package com.laidw.sql.gen.strategy;

import com.laidw.sql.gen.entity.AggregateColumn;

/**
 * 负责给聚合列生成并设置别名
 *
 * @author NightDW 2023/9/4 2:48
 */
public interface AggregateColumnAliasSetter {
    void set(AggregateColumn aggregateColumn);
}
