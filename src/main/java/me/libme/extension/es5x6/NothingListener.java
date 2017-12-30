package me.libme.extension.es5x6;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;

/**
 * Created by J on 2017/9/24.
 */
public class NothingListener implements BulkProcessor.Listener {

    @Override
    public void beforeBulk(long executionId, BulkRequest request) {

    }

    @Override
    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {

    }

    @Override
    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {

    }


}
