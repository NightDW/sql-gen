package com.laidw.sql.gen.ro;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 切换数据库的请求参数
 *
 * @author NightDW 2023/9/3 1:57
 */
@Data
@EqualsAndHashCode
public class ChangeDatabaseRO {

    @NotBlank
    private String url;

    private String user;

    private String password;
}
