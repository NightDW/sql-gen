package com.laidw.sql.gen.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>多选框组件，主要用于展示某张表中有哪些列，并且哪些列被用户选中了
 *
 * <p>假设用户选择了a和b两张表，并且选择了a表的a1列；那么，在页面中，将会用如下方式来展示：
 * <li>a: ✔a1 ×a2
 * <li>b: ×b1 ×b2
 *
 * @author NightDW 2023/9/1 12:29
 */
public class CheckBoxGroupVO implements Iterable<CheckBoxGroupVO.CheckItemVO> {

    /**
     * 组名，也就是表的别名
     */
    @Getter
    private final String name;

    /**
     * 该组内的所有选项，即该表中的所有列名，以及这些列名是否被勾选了
     */
    private final List<CheckItemVO> items;

    /**
     * 用户真正勾选的选项，格式为alias.column_name
     */
    private final Set<String> checked;

    /**
     * @param name    表的别名
     * @param items   该表的所有列名
     * @param checked 用户实际选中的列，格式为alias.column_name
     */
    public CheckBoxGroupVO(String name, List<String> items, Set<String> checked) {
        this.name = name;
        this.items = items.stream().map(CheckItemVO::new).collect(Collectors.toList());
        this.checked = checked;
    }

    @Override
    public Iterator<CheckItemVO> iterator() {
        return items.iterator();
    }

    /**
     * 多选框中的某个选项
     */
    @AllArgsConstructor
    public class CheckItemVO {

        /**
         * 选该项的展示名称
         */
        @Getter
        private String name;

        /**
         * 该选项是否被勾选，该方法是提供给Thymeleaf调用的
         */
        @SuppressWarnings("unused")
        public boolean getChecked() {
            return checked.contains(getFullName());
        }

        /**
         * 该选项的完整名称
         */
        public String getFullName() {
            return CheckBoxGroupVO.this.name + "." + name;
        }
    }
}
