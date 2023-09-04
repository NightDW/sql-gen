package com.laidw.sql.gen.util;

import com.laidw.sql.gen.constant.AggregateType;
import com.laidw.sql.gen.entity.AggregateColumn;
import com.laidw.sql.gen.exception.SqlGenException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 负责解析聚合列信息
 *
 * @author NightDW 2023/9/4 1:45
 */
public class AggregateUtil {

    public static List<AggregateColumn> parse(String aggregateColumn) {
        if (aggregateColumn == null || (aggregateColumn = aggregateColumn.trim()).isEmpty()) {
            return Collections.emptyList();
        }

        String[] aggCols = aggregateColumn.split(" *, *");
        List<AggregateColumn> list = new ArrayList<>(aggCols.length);
        for (String aggCol : aggCols) {
            String[] split = aggCol.split(" +");

            if (split.length >= 4 || (split.length == 3 && !split[1].equalsIgnoreCase("as"))) {
                throw new SqlGenException("聚合列有误：" + aggCol);
            }

            String alias = split.length == 1 ? null : split[split.length - 1];

            int leftIdx = split[0].indexOf('(');
            int rightIdx = split[0].lastIndexOf(')');

            if (leftIdx < 0 || rightIdx < 0) {
                throw new SqlGenException("聚合列有误：" + aggCol);
            }

            AggregateType type = AggregateType.ofNameIgnoreCase(split[0].substring(0, leftIdx));

            String fullName = split[0].substring(leftIdx + 1, rightIdx);
            int dotIdx = fullName.indexOf('.');
            String tableAlias = dotIdx == -1 ? null : fullName.substring(0, dotIdx);
            String columnName = fullName.substring(dotIdx + 1);

            // 只允许COUNT(1)和COUNT(*)这两种情况没有表的别名
            if (tableAlias == null && (type != AggregateType.COUNT || columnName.length() != 1)) {
                throw new SqlGenException("聚合列有误：" + aggCol);
            }

            list.add(new AggregateColumn(type, tableAlias, columnName, alias));
        }

        return list;
    }
}
