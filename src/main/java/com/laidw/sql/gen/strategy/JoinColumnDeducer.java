package com.laidw.sql.gen.strategy;

import java.util.List;

/**
 * 本组件用于推断连表条件
 *
 * @author NightDW 2023/9/2 16:58
 */
public interface JoinColumnDeducer {
    String[] NO_MATCH = new String[] {null, null};

    /**
     * 根据主表的列和从表的列来推断这两者的连表条件
     *
     * @param columns1 元素格式为alias.column_name
     * @param columns2 元素格式为alias.column_name
     * @return 有两个元素，分别代表主表和从表的用于连接的列
     */
    String[] deduce(List<String> columns1, List<String> columns2);
}
