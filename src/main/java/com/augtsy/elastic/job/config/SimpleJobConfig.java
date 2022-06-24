package com.augtsy.elastic.job.config;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Description: SimpleJob 配置
 * @Auther: tsy
 * @Date: 2022/06/24/2:21 下午
 */
@Slf4j
@Configuration
public class SimpleJobConfig {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    @PostConstruct
    public void startSimpleJob() {
        applicationContext.getBeansWithAnnotation(ElasticJobConfig.class).forEach((className, obj) -> {
            ElasticJobConfig config = obj.getClass().getAnnotation(ElasticJobConfig.class);
            // 表达式
            String cron = StringUtils.defaultIfBlank(config.cron(), config.value());
            // 分片总数
            int shardingTotalCount = config.shardingTotalCount();
            // 分片项参数
            String shardingItemParameters = config.shardingItemParameters();
            JobListener elasticJobListener = new JobListener();
            SimpleJob simpleJob = (SimpleJob) obj;
            new SpringJobScheduler(simpleJob, zookeeperRegistryCenter,
                    getLiteJobConfiguration(simpleJob.getClass(), cron, shardingTotalCount, shardingItemParameters),
                    elasticJobListener).init();
        });
    }

    /**
     * 创建简单作业配置构建器.
     *
     * @param jobName            作业名称
     * @param cron               作业启动时间的cron表达式
     * @param shardingTotalCount 作业分片总数
     * @return 简单作业配置构建器
     */
    private LiteJobConfiguration getLiteJobConfiguration(Class<?> jobName, String cron, int shardingTotalCount, String shardingItemParameters) {
        return LiteJobConfiguration.newBuilder(
                new SimpleJobConfiguration(
                        JobCoreConfiguration.newBuilder(jobName.getName(), cron, shardingTotalCount)
                                .shardingItemParameters(shardingItemParameters)
                                .build(),
                        jobName.getCanonicalName()))
                .monitorExecution(true)
                .overwrite(false).build();
    }
}
