package com.laidw.sql.gen.ro;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 用户提交的主表信息
 *
 * @author NightDW 2023/9/1 11:44
 */
@Data
@EqualsAndHashCode
public class FromRO {

    /**
     * 表名
     */
    @NotBlank
    private String tableName;

    /**
     * 表名的别名
     */
    private String tableAlias;
}
