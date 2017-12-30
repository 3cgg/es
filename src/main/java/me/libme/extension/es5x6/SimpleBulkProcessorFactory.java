package me.libme.extension.es5x6;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.threadpool.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by J on 2017/9/24.
 */
public class SimpleBulkProcessorFactory implements IBulkProcessorFactory {


    private final ThreadPool threadPool;

    public SimpleBulkProcessorFactory(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    public SimpleBulkProcessorFactory() {

        Settings settings=Settings.builder()
                .put("node.name","test")
                .build();
        this.threadPool=new ThreadPool(settings);
    }

    private static final Logger logger= LoggerFactory.getLogger(SimpleBulkProcessorFactory.class);

    @Override
    public BulkProcessor getObject(RestHighLevelClient client) throws Exception {


        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                int numberOfActions = request.numberOfActions();
                logger.debug("Executing bulk [{}] with {} requests", executionId, numberOfActions);
//                bulkProcessListener.beforeBulk(executionId,request);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                if (response.hasFailures()) {
                    logger.warn("Bulk [{}] executed with failures", executionId);
                } else {
                    logger.debug("Bulk [{}] completed in {} milliseconds", executionId, response.getTook().getMillis());
                }
//                bulkProcessListener.afterBulk(executionId, request, response);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                logger.error("Failed to execute bulk", failure);
//                bulkProcessListener.afterBulk(executionId, request, failure);
            }
        };

        BulkProcessor.Builder builder = new BulkProcessor.Builder(client::bulkAsync, listener, threadPool);
        builder.setBulkActions(500);
        builder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB));
        builder.setConcurrentRequests(0);
        builder.setFlushInterval(TimeValue.timeValueSeconds(10L));
        builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3));

        return builder.build();
    }


}
