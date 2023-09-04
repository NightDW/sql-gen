package com.laidw.sql.gen.ro;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 用户提交的关联表信息
 *
 * @author NightDW 2023/9/1 11:44
 */
@Data
@EqualsAndHashCode
public class JoinRO {

    /**
     * 连接类型
     */
    @NotBlank
    private String type;

    /**
     * 关联表名
     */
    @NotBlank
    private String tableName;

    /**
     * 关联表名的别名
     */
    private String tableAlias;

    /**
     * ON条件：主表的用于连接的列名，格式为alias.column_name
     */
    private String column1;

    /**
     * ON条件：关联表的用于连接的列名，格式为alias.column_name
     */
    private String column2;
}