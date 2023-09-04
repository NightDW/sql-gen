package com.laidw.sql.gen.util;

import com.laidw.sql.gen.entity.Column;
import com.laidw.sql.gen.entity.Database;
import com.laidw.sql.gen.entity.Table;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

/**
 * 演示数据
 *
 * @author NightDW 2023/8/29 15:27
 */
public class DemoUtil {
    public static List<Table> tables = Arrays.asList(
            new Table("sys_user", Arrays.asList(
                    new Column("user_id", Types.BIGINT),
                    new Column("user_name", Types.VARCHAR),
                    new Column("gender", Types.TINYINT),
                    new Column("money", Types.DECIMAL),
                    new Column("birthday", Types.TIMESTAMP),
                    new Column("height", Types.DOUBLE),
                    new Column("attachment", Types.BLOB)
            )),
            new Table("sys_class", Arrays.asList(
                    new Column("class_id", Types.BIGINT),
                    new Column("class_name", Types.VARCHAR),
                    new Column("grade_id", Types.BIGINT)
            )),
            new Table("sys_grade", Arrays.asList(
                    new Column("grade_id", Types.BIGINT),
                    new Column("grade_name", Types.VARCHAR),
                    new Column("grade_code", Types.TINYINT)
            )),
            new Table("sys_user_class_rel", Arrays.asList(
                    new Column("rel_id", Types.BIGINT),
                    new Column("user_id", Types.BIGINT),
                    new Column("class_id", Types.BIGINT)
            )),
            new Table("sys_parent_child_rel", Arrays.asList(
                    new Column("rel_id", Types.BIGINT),
                    new Column("parent_user_id", Types.BIGINT),
                    new Column("child_user_id", Types.BIGINT)
            ))
    );

    public static final Database database = new Database("示例", tables);
}
