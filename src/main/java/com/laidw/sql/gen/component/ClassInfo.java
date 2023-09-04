package com.laidw.sql.gen.component;

import com.laidw.sql.gen.exception.SqlGenException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 本组件负责生成RO/VO类的代码
 *
 * @author NightDW 2023/9/3 16:15
 */
public class ClassInfo {
    private final String classname;
    private final Set<String> imports = new TreeSet<>();
    private final Map<String, String> fields = new LinkedHashMap<>();

    public ClassInfo(String classname) {
        this.classname = classname;
    }

    public void addSimpleField(Class<?> type, String name) {
        if (fields.containsKey(name)) {
            throw new SqlGenException("字段已存在：" + name);
        }
        addImport(type);
        fields.put(name, type.getSimpleName());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!imports.isEmpty()) {
            for (String toImport : imports) {
                sb.append("import ").append(toImport).append(";\n");
            }
            sb.append('\n');
        }
        sb.append("public class ").append(classname).append(" {\n");
        for (Map.Entry<String, String> field : fields.entrySet()) {
            sb.append("    private ").append(field.getValue()).append(' ').append(field.getKey()).append(";\n");
        }
        sb.append('}');
        return sb.toString();
    }

    private void addImport(Class<?> type) {
        if (!type.getPackage().getName().equals("java.lang")) {
            imports.add(type.getName());
        }
    }
}
