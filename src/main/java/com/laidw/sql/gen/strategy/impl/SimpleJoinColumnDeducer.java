package com.laidw.sql.gen.strategy.impl;

import com.laidw.sql.gen.strategy.JoinColumnDeducer;
import com.laidw.sql.gen.util.StringUtil;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 简单的JoinColumnDeducer组件，根据名称是否相等来判断
 *
 * @author NightDW 2023/9/2 17:08
 */
@Setter
public class SimpleJoinColumnDeducer implements JoinColumnDeducer {
    private Pattern excludePattern = null;
    private Pattern includePattern = Pattern.compile("id$");

    @Override
    public String[] deduce(List<String> columns1, List<String> columns2) {
        Map<String, String> originalNameMap = new HashMap<>();

        // 过滤掉columns2中不符合要求的列
        if (excludePattern != null) {
            columns2 = columns2.stream()
                    .filter(column -> excludePattern.asPredicate().negate().test(getOriginalName(originalNameMap, column)))
                    .collect(Collectors.toList());
        }

        // 保留columns2中符合要求的列
        if (includePattern != null) {
            columns2 = columns2.stream()
                    .filter(column -> includePattern.asPredicate().test(getOriginalName(originalNameMap, column)))
                    .collect(Collectors.toList());
        }

        // 对于columns1中的所有元素，根据原始列名进行分类
        Map<String, List<String>> columns1GroupByOriginalName = columns1.stream()
                .collect(Collectors.groupingBy(column -> getOriginalName(originalNameMap, column)));

        // 第一种匹配方式，支持sys_parent_child_rel.child_user_id = child.user_id之类的规则
        for (String column2 : columns2) {
            String originalName = column2.replace('.', '_');
            List<String> matches = columns1GroupByOriginalName.get(originalName);
            if (matches != null) {
                return new String[]{matches.get(0), column2};
            }
        }

        // 第二种匹配方式，支持child.user_id = sys_parent_child_rel.child_user_id之类的规则
        Map<String, String> fullName1ToColumn1 = new HashMap<>(columns1.size());
        for (String column1 : columns1) {
            fullName1ToColumn1.put(column1.replace('.', '_'), column1);
        }
        for (String column2 : columns2) {
            String originalName = getOriginalName(originalNameMap, column2);
            String matches = fullName1ToColumn1.get(originalName);
            if (matches != null) {
                return new String[]{matches, column2};
            }
        }

        // 依次处理columns2中的所有元素
        // 获取列名的原始名称，并判断columns1中是否包含该原始名称，如果是，则成功匹配
        for (String column2 : columns2) {
            String originalName = getOriginalName(originalNameMap, column2);
            List<String> matches = columns1GroupByOriginalName.get(originalName);
            if (matches != null) {
                return new String[]{matches.get(0), column2};
            }
        }

        return NO_MATCH;
    }

    private String getOriginalName(Map<String, String> originalNameMap, String column) {
        return originalNameMap.computeIfAbsent(column, StringUtil::getOriginalName);
    }
}