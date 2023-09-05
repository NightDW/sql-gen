package com.laidw.sql.gen.vo;

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
     * 该组内的所有选项，即该表中的所有列名，以及这些列名是否被启用或勾选了
     */
    private final List<CheckItemVO> items;

    /**
     * 真正启用的列；元素格式为alias.column_name；为空时，所有列都启用
     * <p>假如GROUP BY usr.user_id，那么在SELECT时，usr.user_id之外的其它列都应该被禁用
     */
    private final Set<String> enables;

    /**
     * 用户真正勾选的选项，元素格式为alias.column_name
     */
    @Getter
    private final Set<String> checked;

    /**
     * @param name    表的别名
     * @param items   该表的所有列名
     * @param enables 所有GROUP BY列；当创建GROUP BY组件时，传空集合即可
     * @param checked 用户实际选中的列，格式为alias.column_name
     */
    public CheckBoxGroupVO(String name, List<String> items, Set<String> enables, Set<String> checked) {
        this.name = name;
        this.items = items.stream().map(CheckItemVO::new).collect(Collectors.toList());
        this.enables = enables;
        this.checked = checked;
    }

    @Override
    public Iterator<CheckItemVO> iterator() {
        return items.iterator();
    }

    /**
     * 多选框中的某个选项
     */
    @Getter
    public class CheckItemVO {

        /**
         * 选该项的展示名称
         */
        private final String name;

        /**
         * 该选项的完整名称，格式为alias.column_name
         */
        private final String fullName;

        public CheckItemVO(String name) {
            this.name = name;
            this.fullName = CheckBoxGroupVO.this.name + "." + name;
        }

        /**
         * 该选项是否被启用，该方法是提供给Thymeleaf调用的
         */
        @SuppressWarnings("unused")
        public boolean getEnabled() {
            return enables.isEmpty() || enables.contains(fullName);
        }

        /**
         * 该选项是否被勾选，该方法是提供给Thymeleaf调用的
         */
        @SuppressWarnings("unused")
        public boolean getChecked() {
            return getEnabled() && checked.contains(fullName);
        }
    }
}
