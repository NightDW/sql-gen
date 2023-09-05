package com.laidw.sql.gen.service.impl;

import com.laidw.sql.gen.component.ClassInfo;
import com.laidw.sql.gen.constant.JoinType;
import com.laidw.sql.gen.entity.AggregateColumn;
import com.laidw.sql.gen.entity.Column;
import com.laidw.sql.gen.entity.Database;
import com.laidw.sql.gen.entity.Table;
import com.laidw.sql.gen.ro.JoinRO;
import com.laidw.sql.gen.ro.SqlGenRO;
import com.laidw.sql.gen.service.SqlGenService;
import com.laidw.sql.gen.strategy.*;
import com.laidw.sql.gen.util.AggregateUtil;
import com.laidw.sql.gen.util.StringUtil;
import com.laidw.sql.gen.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Description of class {@link SqlGenServiceImpl}.
 *
 * @author NightDW 2023/9/1 11:57
 */
@Service
public class SqlGenServiceImpl implements SqlGenService {

    @Autowired
    private TableAliasGenerator tableAliasGenerator;

    @Autowired
    private WhereClauseGenerator whereClauseGenerator;

    @Autowired
    private JoinColumnDeducer joinColumnDeducer;

    @Autowired
    private ColumnAliasGenerator columnAliasGenerator;

    @Autowired
    private TypeConverter typeConverter;

    @Autowired
    private AggregateColumnAliasGenerator aggregateColumnAliasGenerator;

    /**
     * 本类底层通过Render组件来渲染视图
     */
    private final List<Render> renders = Arrays.asList(
            new BasicRender(),
            new AliasRender(),
            new JoinOnRender(),
            new CheckBoxGroupRender()
    );

    /**
     * 本类底层通过SqlGenerator组件来生成Sql语句
     */
    private final List<SqlGenerator> sqlGenerators = Arrays.asList(
            new SelectFromSqlGenerator(),
            new JoinOnSqlGenerator(),
            new WhereSqlGenerator(),
            new GroupByHavingSqlGenerator(),
            new OrderBySqlGenerator()
    );

    @Override
    public SqlGenVO render(SqlGenRO sqlGenRO, Database database) {
        SqlGenVO sqlGenVO = new SqlGenVO();
        renders.forEach(render -> render.render(sqlGenRO, sqlGenVO, database));
        return sqlGenVO;
    }

    @Override
    public String generateSql(SqlGenRO sqlGenRO, Database database) {
        StringBuilder sb = new StringBuilder();
        sqlGenerators.forEach(sqlGenerator -> sqlGenerator.generate(sqlGenRO, sb));
        return sb.toString();
    }

    @Override
    public String[] generateClass(SqlGenRO sqlGenRO, Database database) {

        // 将表的别名与表的原名的对应关系存起来
        Map<String, String> alias2table = new HashMap<>();
        alias2table.put(sqlGenRO.getFrom().getTableAlias(), sqlGenRO.getFrom().getTableName());
        if (!CollectionUtils.isEmpty(sqlGenRO.getJoins())) {
            for (JoinRO join : sqlGenRO.getJoins()) {
                alias2table.put(join.getTableAlias(), join.getTableName());
            }
        }

        List<AggregateColumn> aggregates = AggregateUtil.parse(sqlGenRO.getAggregateSelects());
        String ro = getClassStr("RO", sqlGenRO.getWheres(), Collections.emptyList(), database, alias2table);
        String vo = getClassStr("VO", sqlGenRO.getSelects(), aggregates, database, alias2table);
        return new String[] {ro, vo};
    }

    /**
     * 本方法用于生成RO/VO的代码
     *
     * @param classname   类名
     * @param columns     WHERE/SELECT列，格式为alias.column_name
     * @param aggregates  聚合列信息
     * @param database    数据库信息
     * @param alias2table 表的别名与原名的对应关系
     */
    private String getClassStr(String classname, List<String> columns, List<AggregateColumn> aggregates, Database database, Map<String, String> alias2table) {
        columns = columns == null ? Collections.emptyList() : columns;

        // 为涉及到的列生成别名；别名的驼峰命名将作为字段名
        List<String> aliases = columnAliasGenerator.generate(columns);

        // 处理WHERE/SELECT列
        ClassInfo classInfo = new ClassInfo(classname);
        for (int i = 0; i < columns.size(); i++) {

            // 将列的别名转成驼峰命名，即可得到字段名称
            String propertyName = StringUtil.toCamelCase(aliases.get(i));

            // 解析当前的WHERE/SELECT列，获取到该列对应的表名和原始列名
            String column = columns.get(i);
            int dotIdx = column.indexOf('.');
            String tableName = alias2table.get(column.substring(0, dotIdx));
            String originalColumnName = column.substring(dotIdx + 1);

            // 获取到该列的SQL数据类型，并将其转成Java类型，然后添加相应的字段
            int dataType = database.getTable(tableName).getColumnDataType(originalColumnName);
            classInfo.addSimpleField(typeConverter.toJavaType(dataType), propertyName);
        }

        // 处理聚合列
        forEach(aggregates, aggregateColumnAliasGenerator.generate(aggregates), (aggregate, alias) -> {
            String propertyName = StringUtil.toCamelCase(alias);

            // 推断该聚合查询列的Java类型
            Class<?> javaType = aggregate.getType().javaType;
            if (javaType == Object.class) {
                String tableName = alias2table.get(aggregate.getTableAlias());
                int dataType = database.getTable(tableName).getColumnDataType(aggregate.getColumnName());
                javaType = typeConverter.toJavaType(dataType);
            }

            classInfo.addSimpleField(javaType, propertyName);
        });

        return classInfo.toString();
    }

    private Set<String> asSet(Collection<String> list) {
        return CollectionUtils.isEmpty(list) ? Collections.emptySet() : new HashSet<>(list);
    }

    private static <T1, T2> void forEach(List<T1> list1, List<T2> list2, BiConsumer<T1, T2> consumer) {
        int size = Math.min(list1.size(), list2.size());
        for (int i = 0; i < size; i++) {
            consumer.accept(list1.get(i), list2.get(i));
        }
    }


    //
    // 以下是内部组件
    //

    private interface Render {
        void render(SqlGenRO ro, SqlGenVO vo, Database database);
    }

    private class BasicRender implements Render {

        /**
         * 设置简单的信息
         */
        @Override
        public void render(SqlGenRO ro, SqlGenVO vo, Database database) {

            // 设置主表信息
            List<String> allTables = database.getTableNames();
            String selectedTable = ro.getFrom().getTableName();
            SelectBoxVO fromSelectBox = new SelectBoxVO(allTables, selectedTable);
            vo.setFrom(new FromVO(fromSelectBox, ro.getFrom().getTableAlias()));

            // 初步设置关联表的信息
            if (!CollectionUtils.isEmpty(ro.getJoins())) {
                vo.setJoins(convertJoins(ro.getJoins(), database));
            } else {
                vo.setJoins(Collections.emptyList());
            }

            // 设置HAVING条件和聚合查询列
            vo.setHaving(ro.getHaving());
            vo.setAggregateSelects(asSet(ro.getAggregateSelects()));
        }

        private List<JoinVO> convertJoins(List<JoinRO> joins, Database database) {
            List<JoinVO> ret = new ArrayList<>(joins.size());
            for (JoinRO joinRO : joins) {
                JoinVO joinVO = new JoinVO();
                joinVO.setSelectedType(new SelectBoxVO(JoinType.names(), joinRO.getType()));
                joinVO.setSelectedTable(new SelectBoxVO(database.getTableNames(), joinRO.getTableName()));
                joinVO.setTableAlias(joinRO.getTableAlias());
                ret.add(joinVO);
            }
            return ret;
        }
    }

    private class AliasRender implements Render {

        /**
         * 为没有别名的表生成别名
         */
        @Override
        public void render(SqlGenRO ro, SqlGenVO vo, Database database) {

            // 收集所有的表及其对应的别名
            List<String> tables = new ArrayList<>();
            List<String> aliases = new ArrayList<>();
            tables.add(ro.getFrom().getTableName());
            aliases.add(ro.getFrom().getTableAlias());
            if (ro.getJoins() != null) {
                for (JoinRO join : ro.getJoins()) {
                    tables.add(join.getTableName());
                    aliases.add(join.getTableAlias());
                }
            }

            // 生成并设置别名
            aliases = tableAliasGenerator.generateAliases(tables, aliases);
            vo.getFrom().setTableAlias(aliases.get(0));
            for (int i = 1; i < aliases.size(); i++) {
                vo.getJoins().get(i - 1).setTableAlias(aliases.get(i));
            }
        }
    }

    private class JoinOnRender implements Render {

        /**
         * 完善关联表信息，主要负责生成主表的列的下拉框和关联表的列的下拉框
         */
        @Override
        public void render(SqlGenRO ro, SqlGenVO vo, Database database) {
            if (CollectionUtils.isEmpty(ro.getJoins())) {
                return;
            }

            // 获取主表的所有列
            List<String> preColumns = new ArrayList<>();
            addColumns(database.getTable(vo.getFrom().getSelectedTable().getSelected()), vo.getFrom().getTableAlias(), preColumns);

            // 依次处理这些关联表
            int idx = 0;
            for (JoinVO joinVO : vo.getJoins()) {

                // 获取关联表的所有列
                List<String> joinTableColumns = new ArrayList<>();
                addColumns(database.getTable(joinVO.getSelectedTable().getSelected()), joinVO.getTableAlias(), joinTableColumns);

                // 设置主表的列的下拉框和关联表的列的下拉框，并自动推断要连接的列
                String column1 = ro.getJoins().get(idx).getColumn1();
                String column2 = ro.getJoins().get(idx).getColumn2();
                if (StringUtil.isEmpty(column1) || StringUtil.isEmpty(column2) || !column2.startsWith(joinVO.getTableAlias())) {
                    String[] deduce = joinColumnDeducer.deduce(preColumns, joinTableColumns);
                    column1 = deduce[0];
                    column2 = deduce[1];
                }
                joinVO.setJoinColumns1(new SelectBoxVO(preColumns, column1));
                joinVO.setJoinColumns2(new SelectBoxVO(joinTableColumns, column2));

                // 将当前关联表的列添加到preColumns集合中，并更新idx下标
                preColumns.addAll(joinTableColumns);
                idx++;
            }
        }

        private void addColumns(Table table, String alias, List<String> columns) {
            for (Column column : table.getColumns()) {
                columns.add(alias + "." + column.getName());
            }
        }
    }

    private class CheckBoxGroupRender implements Render {

        /**
         * 设置WHERE、GROUP BY、SELECT、ORDER BY等信息
         */
        @Override
        public void render(SqlGenRO ro, SqlGenVO vo, Database database) {

            // 设置GROUP BY
            Set<String> groupBys = asSet(ro.getGroupBys());
            vo.setGroupBys(getCheckBoxGroups(vo, database, Collections.emptySet(), groupBys));

            // 设置SELECT；GROUP BY列默认当作SELECT列
            Set<String> selects = new HashSet<>(groupBys);
            if (ro.getSelects() != null) {
                selects.addAll(ro.getSelects());
            }
            vo.setSelects(getCheckBoxGroups(vo, database, groupBys, selects));

            vo.setWheres(getCheckBoxGroups(vo, database, groupBys, asSet(ro.getWheres())));
            vo.setOrderBys(getCheckBoxGroups(vo, database, groupBys, asSet(ro.getOrderBys())));
        }

        private List<CheckBoxGroupVO> getCheckBoxGroups(SqlGenVO vo, Database database, Set<String> enabled, Set<String> checked) {
            List<CheckBoxGroupVO> checkBoxGroups = new ArrayList<>();
            checkBoxGroups.add(getTableColumnsCheckBox(vo.getFrom().getSelectedTable().getSelected(), vo.getFrom().getTableAlias(), database, enabled, checked));
            for (JoinVO join : vo.getJoins()) {
                checkBoxGroups.add(getTableColumnsCheckBox(join.getSelectedTable().getSelected(), join.getTableAlias(), database, enabled, checked));
            }
            return checkBoxGroups;
        }

        private CheckBoxGroupVO getTableColumnsCheckBox(String tableName, String alias, Database database, Set<String> enabled, Set<String> checked) {
            return new CheckBoxGroupVO(alias, database.getTable(tableName).getColumnNames(), enabled, checked);
        }
    }

    private interface SqlGenerator {
        void generate(SqlGenRO ro, StringBuilder sb);
    }

    private class SelectFromSqlGenerator implements SqlGenerator {

        /**
         * 拼接SELECT和FROM
         */
        @Override
        public void generate(SqlGenRO ro, StringBuilder sb) {
            sb.append("SELECT").append('\n');

            // 获取SELECT列和聚合列
            List<String> selects = ro.getSelects();
            List<AggregateColumn> aggregates = AggregateUtil.parse(ro.getAggregateSelects());

            // 处理SELECT列
            if (!CollectionUtils.isEmpty(selects)) {

                // 为SELECT列添加别名
                List<String> alias = columnAliasGenerator.generate(selects);
                for (int i = 0; i < alias.size(); i++) {
                    alias.set(i, selects.get(i) + " " + alias.get(i));
                }

                // 同一张表的SELECT列写在同一行
                int begin = 0;
                while (begin < alias.size()) {
                    int end = getEndIdx(alias, begin);
                    StringUtil.append(sb, "  ", ", ", ",\n", alias.subList(begin, end));
                    begin = end;
                }

                // 如果没有聚合列，则删除最后一个逗号
                if (aggregates.isEmpty()) {
                    sb.deleteCharAt(sb.length() - 2);
                }
            }

            // 处理聚合列
            if (!aggregates.isEmpty()) {
                sb.append("  ");
                forEach(aggregates, aggregateColumnAliasGenerator.generate(aggregates), (aggregate, alias) -> {
                    sb.append(aggregate.toString()).append(' ').append(alias).append(", ");
                });
                sb.delete(sb.length() - 2, sb.length());
                sb.append('\n');
            }

            StringUtil.append(sb, "FROM ", " ", "\n", ro.getFrom().getTableName(), ro.getFrom().getTableAlias());
        }

        private int getEndIdx(List<String> list, int from) {
            String first = list.get(from);
            String prefix = first.substring(0, first.indexOf('.') + 1);
            while (from < list.size() && list.get(from).startsWith(prefix)) {
                from++;
            }
            return from;
        }
    }

    private static class JoinOnSqlGenerator implements SqlGenerator {

        /**
         * 拼接JOIN和ON
         */
        @Override
        public void generate(SqlGenRO ro, StringBuilder sb) {
            if (CollectionUtils.isEmpty(ro.getJoins())) {
                return;
            }
            for (JoinRO join : ro.getJoins()) {
                StringUtil.append(sb, JoinType.valueOf(join.getType()).sqlFragment, " ", "", join.getTableName(), join.getTableAlias());
                StringUtil.append(sb, " ON ", " = ", "\n", join.getColumn1(), join.getColumn2());
            }
        }
    }

    private class WhereSqlGenerator implements SqlGenerator {

        /**
         * 拼接WHERE
         */
        @Override
        public void generate(SqlGenRO ro, StringBuilder sb) {
            if (CollectionUtils.isEmpty(ro.getWheres())) {
                return;
            }
            List<String> alias = columnAliasGenerator.generate(ro.getWheres());
            StringUtil.append(sb, "WHERE 1 = 1\n  ", "\n  ", "\n", whereClauseGenerator.generate(ro.getWheres(), alias));
        }
    }

    private static class GroupByHavingSqlGenerator implements SqlGenerator {

        /**
         * 拼接GROUP BY和HAVING
         */
        @Override
        public void generate(SqlGenRO ro, StringBuilder sb) {
            if (CollectionUtils.isEmpty(ro.getGroupBys())) {
                return;
            }
            StringUtil.append(sb, "GROUP BY ", ", ", "\n", ro.getGroupBys());
            if (!StringUtil.isEmpty(ro.getHaving())) {
                sb.append("HAVING ").append(ro.getHaving()).append('\n');
            }
        }
    }

    private static class OrderBySqlGenerator implements SqlGenerator {

        /**
         * 拼接ORDER BY
         */
        @Override
        public void generate(SqlGenRO ro, StringBuilder sb) {
            StringUtil.append(sb, "ORDER BY ", ", ", "", ro.getOrderBys());
        }
    }
}
