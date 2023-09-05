package com.laidw.sql.gen.strategy;

import com.laidw.sql.gen.entity.AggregateColumn;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 为聚合列生成别名
     */
    default List<String> generateForAggregate(List<AggregateColumn> aggregateColumns) {
        if (CollectionUtils.isEmpty(aggregateColumns)) {
            return Collections.emptyList();
        }

        // 先按照普通的SELECT列的规则来生成别名
        List<String> columnNames = aggregateColumns.stream()
                .map(aggregateColumn -> aggregateColumn.getTableAlias() + '.' + aggregateColumn.getColumnName())
                .collect(Collectors.toList());
        List<String> alias = generate(columnNames);

        // 然后在别名前面再拼接上聚合类型即可
        for (int i = 0; i < alias.size(); i++) {
            alias.set(i, aggregateColumns.get(i).getType().name().toLowerCase() + '_' + alias.get(i));
        }

        return alias;
    }
}
