package com.laidw.sql.gen.strategy.impl;

import com.laidw.sql.gen.strategy.TableAliasGenerator;
import com.laidw.sql.gen.util.StringUtil;
import lombok.Setter;

import java.util.Collection;

/**
 * 简单的别名生成器；以表名作为别名，如果表名重复，则添加数字后缀
 *
 * @author NightDW 2023/9/1 1:21
 */
@Setter
public class SimpleTableAliasGenerator implements TableAliasGenerator {

    /**
     * 表名可能以某个前缀开头；因此，在生成别名时，可以考虑先去掉前缀，如：sys_class -> class
     */
    private String tablePrefix;

    /**
     * 对于单个单词的表名，该单词可能比较长，因此可以考虑只截取前几个字符，如：class -> cla
     */
    private int simpleMaxLength = Integer.MAX_VALUE;

    /**
     * 对于多个单词的表名，可以考虑只取每个单词的第一个字符，如：user_class_rel -> ucr
     */
    private boolean compactComposite = true;

    @Override
    public String generateAlias(String table, Collection<String> existedAliases) {
        String shortTableName = getShortTableName(table);

        if (!existedAliases.contains(shortTableName)) {
            return shortTableName;
        }

        String alias;
        int suffix = 1;
        while (existedAliases.contains(alias = shortTableName + suffix)) {
            suffix++;
        }
        return alias;
    }

    private String getShortTableName(String table) {
        table = StringUtil.deletePrefix(table, tablePrefix);
        if (table.indexOf('_') != -1) {
            if (compactComposite) {
                table = StringUtil.compact(table);
            }
        } else {
            if (table.length() > simpleMaxLength) {
                table = table.substring(0, simpleMaxLength);
            }
        }
        return table;
    }
}
