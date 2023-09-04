package com.laidw.sql.gen.strategy.impl;

import com.laidw.sql.gen.entity.Column;
import com.laidw.sql.gen.entity.Database;
import com.laidw.sql.gen.entity.Table;
import com.laidw.sql.gen.strategy.DatabaseLoader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取MySQL数据库的表信息
 *
 * @author NightDW 2023/9/3 1:07
 */
public class MySqlDatabaseLoader implements DatabaseLoader {

    @Override
    public boolean support(String url) {
        return url.startsWith("jdbc:mysql://");
    }

    @Override
    public Database get(String url, String user, String password) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        List<Table> tables = new ArrayList<>();

        // 数据库的元数据和数据库名称
        DatabaseMetaData dbMeta = connection.getMetaData();
        String databaseName = getDatabaseName(url);

        // 获取到所有表信息，并依次遍历
        ResultSet tablesResult = dbMeta.getTables(databaseName, null, null, new String[]{"TABLE"});
        while (tablesResult.next()) {

            // 获取到表名和列信息
            String tableName = tablesResult.getString("TABLE_NAME");
            ResultSet columnsResult = dbMeta.getColumns(databaseName, null, tableName, null);

            // 将当前表的列信息保存下来
            List<Column> columns = new ArrayList<>();
            while (columnsResult.next()) {
                String columnName = columnsResult.getString("COLUMN_NAME");
                int dataType = columnsResult.getInt("DATA_TYPE");
                columns.add(new Column(columnName, dataType));
            }

            columnsResult.close();

            // 将当前表保存下来
            tables.add(new Table(tableName, columns));
        }

        tablesResult.close();
        connection.close();
        return new Database(databaseName, tables);
    }

    private static String getDatabaseName(String url) {
        int from = url.lastIndexOf('/') + 1;
        int to = url.indexOf('?');
        if (to == -1) {
            to = url.length();
        }
        return url.substring(from, to);
    }
}
