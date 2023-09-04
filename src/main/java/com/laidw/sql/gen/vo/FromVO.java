package com.laidw.sql.gen.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交给HTML模板渲染的主表信息
 *
 * @author NightDW 2023/9/1 11:44
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
public class FromVO {

    /**
     * 所有的表，以及用户实际选中的表
     */
    private SelectBoxVO selectedTable;

    /**
     * 主表的别名
     */
    private String tableAlias;
}
