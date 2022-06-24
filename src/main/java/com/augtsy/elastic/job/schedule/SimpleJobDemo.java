package com.augtsy.elastic.job.schedule;

import com.augtsy.elastic.job.config.ElasticJobConfig;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description: 业务定时任务
 * @Auther: tsy
 * @Date: 2022/06/24/2:36 下午
 */
@Slf4j
@Component
@ElasticJobConfig(cron = "*/4 * * * * ?", shardingTotalCount = 5, shardingItemParameters = "0=0,1=1,2=2,3=3,4=4")
public class SimpleJobDemo implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        //获取分片总数
        int shardingTotalCount = shardingContext.getShardingTotalCount();
        //获取分片项
        int shardingItem = shardingContext.getShardingItem();
        //获取分片项参数
        String shardingParameter = shardingContext.getShardingParameter();

        log.info("分片总数:{} 分片项:{} 分片项参数:{}", shardingTotalCount, shardingItem, shardingParameter);
        // 业务逻辑，ElasticJob 不提供数据处理的功能，框架只会将分片项分配至各个运行中的作业服务器，开发者需要自行处理
    }
}
