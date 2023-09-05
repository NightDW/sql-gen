package com.laidw.sql.gen.ro;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户提交的信息
 *
 * @author NightDW 2023/8/31 14:47
 */
@Data
@EqualsAndHashCode
public class SqlGenRO {

    /**
     * 主表信息
     */
    @NotNull
    @Valid
    private FromRO from;

    /**
     * 关联表信息
     */
    @Valid
    private List<JoinRO> joins;

    /**
     * 需要进行WHERE查询的列，格式为alias.column_name
     */
    private List<String> wheres;

    /**
     * 需要进行分组的列，格式为alias.column_name
     */
    private List<String> groupBys;

    /**
     * HAVING条件，由用户自定义
     */
    private String having;

    /**
     * 需要查询出来的列，格式为alias.column_name
     */
    private List<String> selects;

    /**
     * 聚合查询列，格式为AGGREGATE(alias.column_name)；这里可能会包含空选项
     */
    private List<String> aggregateSelects;

    /**
     * 需要进行排序的列，格式为alias.column_name
     */
    private List<String> orderBys;
}
