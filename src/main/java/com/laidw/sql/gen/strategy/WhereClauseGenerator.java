package com.laidw.sql.gen.strategy;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * WHERE条件的生成器
 *
 * @author NightDW 2023/9/2 12:09
 */
public interface WhereClauseGenerator {

    /**
     * 根据列名来生成WHERE语句，如：usr.user_id user_id -> usr.user_id = #{userId}
     */
    String generate(String column, String alias);

    default List<String> generate(List<String> columns, List<String> alias) {
        if (CollectionUtils.isEmpty(columns)) {
            return Collections.emptyList();
        }

        List<String> ret = new ArrayList<>(columns.size());
        for (int i = 0; i < columns.size(); i++) {
            ret.add(generate(columns.get(i), alias.get(i)));
        }
        return ret;
    }
}
