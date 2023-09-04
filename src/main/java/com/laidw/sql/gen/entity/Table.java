package com.laidw.sql.gen.entity;

import com.laidw.sql.gen.exception.SqlGenException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库表信息
 *
 * @author NightDW 2023/8/29 15:26
 */
@Getter
@AllArgsConstructor
public class Table {
    private final String name;
    private final List<Column> columns;

    public List<String> getColumnNames() {
        return columns.stream().map(Column::getName).collect(Collectors.toList());
    }

    public int getColumnDataType(String columnName) {
        for (Column column : columns) {
            if (column.getName().equals(columnName)) {
                return column.getDataType();
            }
        }
        throw new SqlGenException("列不存在：" + columnName);
    }
}
