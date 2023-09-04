package com.laidw.sql.gen.service;

import com.laidw.sql.gen.entity.Database;
import com.laidw.sql.gen.ro.SqlGenRO;
import com.laidw.sql.gen.vo.FromVO;
import com.laidw.sql.gen.vo.SelectBoxVO;
import com.laidw.sql.gen.vo.SqlGenVO;

import java.util.Collections;

/**
 * 渲染视图、生成SQL的核心组件
 *
 * @author NightDW 2023/9/1 11:56
 */
public interface SqlGenService {

    /**
     * 将用户提交的表单信息渲染成视图，一般是在用户选择的表发生变动时调用
     *
     * @param sqlGenRO 用户提交的表单信息
     * @param database 数据库信息
     */
    SqlGenVO render(SqlGenRO sqlGenRO, Database database);

    /**
     * 根据用户提交的表单信息来生成SQL语句
     *
     * @param sqlGenRO 用户提交的表单信息
     * @param database 数据库信息
     */
    String generateSql(SqlGenRO sqlGenRO, Database database);

    /**
     * 生成RO/VO类的代码
     *
     * @param sqlGenRO 用户提交的表单信息
     * @param database 数据库信息
     * @return 第一/二个元素分别代表RO/VO类的代码
     */
    String[] generateClass(SqlGenRO sqlGenRO, Database database);

    /**
     * 渲染出一个空视图
     */
    default SqlGenVO render(Database database) {
        SqlGenVO vo = new SqlGenVO();
        vo.setFrom(new FromVO(new SelectBoxVO(database.getTableNames(), null), null));
        vo.setCheckedGroupBys(Collections.emptySet());
        return vo;
    }
}
