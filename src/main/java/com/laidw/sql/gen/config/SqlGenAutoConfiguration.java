package com.laidw.sql.gen.config;

import com.laidw.sql.gen.strategy.*;
import com.laidw.sql.gen.strategy.impl.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description of class {@link SqlGenAutoConfiguration}.
 *
 * @author NightDW 2023/9/2 15:34
 */
@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
public class SqlGenAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WhereClauseGenerator whereClauseGenerator() {
        return new MybatisWhereClauseGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "sql.gen.alias")
    public TableAliasGenerator tableAliasGenerator() {
        return new SimpleTableAliasGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "sql.gen.join")
    public JoinColumnDeducer joinColumnDeducer() {
        return new SimpleJoinColumnDeducer();
    }

    @Bean
    @ConditionalOnMissingBean
    public ColumnAliasGenerator selectColumnProcessor() {
        return new SimpleColumnAliasGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "sql.gen.type")
    public TypeConverter typeConverter() {
        return new SimpleTypeConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public AggregateColumnAliasSetter aggregateColumnAliasSetter() {
        return new SimpleAggregateColumnAliasSetter();
    }

    /**
     * DatabaseLoader组件可以有多个，这里默认只注册MySqlDatabaseLoader组件
     */
    @Bean
    @ConditionalOnMissingBean
    public MySqlDatabaseLoader mySqlDatabaseLoader() {
        return new MySqlDatabaseLoader();
    }
}
