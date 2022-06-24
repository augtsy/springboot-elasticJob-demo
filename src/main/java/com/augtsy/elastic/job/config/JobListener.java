package com.augtsy.elastic.job.config;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Auther: tsy
 * @Date: 2022/06/24/2:15 下午
 */
@Slf4j
public class JobListener implements ElasticJobListener {

    private long beginTime = 0;

    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        beginTime = System.currentTimeMillis();
        log.info("===>{} JOB BEGIN TIME: {} <===", shardingContexts.getJobName(), DateUtil.now());
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        long endTime = System.currentTimeMillis();
        log.info("===>{}, JOB END TIME: {},TOTAL CAST: {},current params:{} <===", shardingContexts.getJobName(),
                DateTime.now(), endTime - beginTime, shardingContexts.getJobParameter());
    }
}
