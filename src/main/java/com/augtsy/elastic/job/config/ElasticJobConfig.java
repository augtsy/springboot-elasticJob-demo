package com.augtsy.elastic.job.config;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Description: ElasticJob枚举配置
 * @Auther: tsy
 * @Date: 2022/06/24/2:08 下午
 */
@Inherited
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface ElasticJobConfig {


    String cron() default "";

    String value() default "";

    /**
     * 分片总数
     */
    int shardingTotalCount() default 1;

    /**
     * 分片项参数
     */
    String shardingItemParameters() default "0=0";
}
