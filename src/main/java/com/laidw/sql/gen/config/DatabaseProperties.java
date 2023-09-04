package com.laidw.sql.gen.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description of class {@link DatabaseProperties}.
 *
 * @author NightDW 2023/9/3 13:32
 */
@Data
@EqualsAndHashCode
@ConfigurationProperties(prefix = "sql.gen.db")
public class DatabaseProperties {
    private String url;
    private String user;
    private String password;
}
