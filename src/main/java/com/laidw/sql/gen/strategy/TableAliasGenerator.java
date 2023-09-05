package com.laidw.sql.gen.strategy;

import com.laidw.sql.gen.exception.SqlGenException;
import com.laidw.sql.gen.util.StringUtil;

import java.util.*;

/**
 * 表的别名的生成器
 *
 * @author NightDW 2023/9/1 1:20
 */
public interface TableAliasGenerator {

    /**
     * 为指定的表生成别名
     *
     * @param table          该表的名称
     * @param existedAliases 已存在的别名
     */
    String generate(String table, Collection<String> existedAliases);

    /**
     * 批量生成别名
     *
     * @param tables        待生成别名的表名
     * @param customAliases 已存在的别名；customAliases.get(i)是tables.get(i)的别名
     */
    default List<String> generate(List<String> tables, List<String> customAliases) {
        if (tables.size() != customAliases.size()) {
            throw new SqlGenException("表名称数量与别名数量不一致");
        }

        // 校验customAliases中的非空元素是否重复，并且将非空元素放到Set集合中
        Set<String> existedAliases = new HashSet<>(tables.size());
        for (String alias : customAliases) {
            if (!StringUtil.isEmpty(alias) && !existedAliases.add(alias)) {
                throw new SqlGenException("别名重复：" + alias);
            }
        }

        // 如果所有表都已经有别名了，则直接返回customAliases
        if (existedAliases.size() == tables.size()) {
            return customAliases;
        }

        // 将customAliases中的空元素替换成对应的别名并返回
        String[] aliases = customAliases.toArray(new String[0]);
        for (int i = 0; i < tables.size(); i++) {
            if (StringUtil.isEmpty(aliases[i])) {
                String alias = generate(tables.get(i), existedAliases);
                aliases[i] = alias;
                existedAliases.add(alias);
            }
        }
        return Arrays.asList(aliases);
    }
}
