package com.laidw.sql.gen.strategy.impl;

import com.laidw.sql.gen.entity.AggregateColumn;
import com.laidw.sql.gen.strategy.AggregateColumnAliasGenerator;
import com.laidw.sql.gen.strategy.ColumnAliasGenerator;
import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description of class {@link SimpleAggregateColumnAliasGenerator}.
 *
 * @author NightDW 2023/9/4 2:49
 */
@AllArgsConstructor
public class SimpleAggregateColumnAliasGenerator implements AggregateColumnAliasGenerator {
    private final ColumnAliasGenerator columnAliasGenerator;

    @Override
    public List<String> generate(List<AggregateColumn> aggregateColumns) {
        if (CollectionUtils.isEmpty(aggregateColumns)) {
            return Collections.emptyList();
        }

        // 先按照普通的SELECT列的规则来生成别名
        List<String> columnNames = aggregateColumns.stream()
                .map(aggregateColumn -> aggregateColumn.getTableAlias() + '.' + aggregateColumn.getColumnName())
                .collect(Collectors.toList());
        List<String> alias = columnAliasGenerator.generate(columnNames);

        // 然后在别名前面再拼接上聚合类型即可
        for (int i = 0; i < alias.size(); i++) {
            alias.set(i, aggregateColumns.get(i).getType().name().toLowerCase() + '_' + alias.get(i));
        }

        return alias;
    }
}
