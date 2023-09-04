package com.laidw.sql.gen.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 单选下拉框组件，主要用于展示所有的表，并且哪个表被用户选中了
 *
 * @author NightDW 2023/9/1 12:29
 */
public class SelectBoxVO implements Iterable<SelectBoxVO.SelectItemVO> {

    /**
     * 该组件的所有选项
     */
    private final List<SelectItemVO> items;

    /**
     * 用户选中的选项
     */
    @Getter
    private final String selected;

    public SelectBoxVO(List<String> items, String selected) {
        this.items = items.stream().map(SelectItemVO::new).collect(Collectors.toList());
        this.selected = selected;
    }

    @Override
    public Iterator<SelectBoxVO.SelectItemVO> iterator() {
        return items.iterator();
    }

    /**
     * 下拉框中的某个选项
     */
    @AllArgsConstructor
    public class SelectItemVO {

        /**
         * 选项名称
         */
        @Getter
        private String name;

        /**
         * 该选项是否被选中，该方法是提供给Thymeleaf调用的
         */
        @SuppressWarnings("unused")
        public boolean getSelected() {
            return name.equals(selected);
        }
    }
}
