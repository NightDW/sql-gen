package com.laidw.sql.gen.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交给HTML模板渲染的关联表信息
 *
 * @author NightDW 2023/9/1 11:44
 */
@Data
@EqualsAndHashCode
public class JoinVO {

    /**
     * 所有关联类型，以及用户实际选中的类型
     */
    private SelectBoxVO selectedType;

    /**
     * 所有的表，以及用户实际选中的表
     */
    private SelectBoxVO selectedTable;

    /**
     * 关联表的别名
     */
    private String tableAlias;

    /**
     * 主表的所有列，以及用户实际选中的列
     */
    private SelectBoxVO joinColumns1;

    /**
     * 关联表的所有列，以及用户实际选中的列
     */
    private SelectBoxVO joinColumns2;
}