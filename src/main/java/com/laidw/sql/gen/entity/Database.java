package com.laidw.sql.gen.entity;

import com.laidw.sql.gen.exception.SqlGenException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库信息
 *
 * @author NightDW 2023/9/1 11:14
 */
@Getter
@AllArgsConstructor
public class Database {
    private final String name;
    private final List<Table> tables;

    public Table getTable(String tableName) {
        for (Table table : tables) {
            if (table.getName().equals(tableName)) {
                return table;
            }
        }
        throw new SqlGenException("表不存在：" + tableName);
    }

    public List<String> getTableNames() {
        return tables.stream().map(Table::getName).collect(Collectors.toList());
    }
}
