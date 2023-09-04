package com.laidw.sql.gen.strategy;

import com.laidw.sql.gen.entity.Database;

import java.sql.SQLException;

/**
 * 本组件用于读取数据库的表信息
 *
 * @author NightDW 2023/9/3 1:06
 */
public interface DatabaseLoader {
    boolean support(String url);
    Database get(String url, String user, String password) throws SQLException;
}
