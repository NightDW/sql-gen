package com.laidw.sql.gen.strategy;

import com.laidw.sql.gen.entity.AggregateColumn;

import java.util.List;

/**
 * 负责给聚合列生成别名；注意聚合列的别名不能与普通列的别名重复
 *
 * @author NightDW 2023/9/4 2:48
 */
public interface AggregateColumnAliasGenerator {
    List<String> generate(List<AggregateColumn> aggregateColumns);
}
