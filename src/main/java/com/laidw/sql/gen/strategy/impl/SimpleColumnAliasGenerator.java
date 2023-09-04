package com.laidw.sql.gen.strategy.impl;

import com.laidw.sql.gen.strategy.ColumnAliasGenerator;
import com.laidw.sql.gen.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 简单的SELECT列别名生成器
 *
 * @author NightDW 2023/9/3 14:47
 */
public class SimpleColumnAliasGenerator implements ColumnAliasGenerator {

    @Override
    public List<String> generate(List<String> columns) {

        // 默认将列的原始名称作为别名
        List<String> aliases = columns.stream()
                .map(StringUtil::getOriginalName)
                .collect(Collectors.toList());

        // 检查原始名称是否重复（将原始名称相同的列的下标归到同一组中）
        Map<String, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < aliases.size(); i++) {
            map.computeIfAbsent(aliases.get(i), k -> new ArrayList<>()).add(i);
        }

        // 为原始名称冲突的列重新生成别名，比如：class.code -> class.code class_code
        for (List<Integer> duplicateIndexes : map.values()) {
            if (duplicateIndexes.size() > 1) {
                for (int index : duplicateIndexes) {
                    aliases.set(index, columns.get(index).replace('.', '_'));
                }
            }
        }

        return aliases;
    }
}
