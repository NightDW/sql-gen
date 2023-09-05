package com.laidw.sql.gen.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 交给HTML模板渲染的页面信息
 *
 * @author NightDW 2023/9/1 11:49
 */
@Data
@EqualsAndHashCode
public class SqlGenVO {

    /**
     * 主表信息
     */
    private FromVO from;

    /**
     * 关联表信息
     */
    private List<JoinVO> joins;

    /**
     * WHERE条件；这里会展示涉及到的所有表及其所有的列，以及这些列是否被选中
     */
    private List<CheckBoxGroupVO> wheres;

    /**
     * GROUP BY信息；这里会展示涉及到的所有表及其所有的列，以及这些列是否被选中
     */
    private List<CheckBoxGroupVO> groupBys;

    /**
     * HAVING条件
     */
    private String having;

    /**
     * WHERE信息；这里会展示涉及到的所有表及其所有的列，以及这些列是否被选中
     */
    private List<CheckBoxGroupVO> selects;

    /**
     * 用户自己指定的聚合查询列
     */
    private String aggregateSelect;

    /**
     * ORDER BY条件；这里会展示涉及到的所有表及其所有的列，以及这些列是否被选中
     */
    private List<CheckBoxGroupVO> orderBys;
}
