package me.libme.extension.es5x6;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * Created by J on 2017/9/24.
 */
public interface IBulkProcessorFactory {

    BulkProcessor getObject(RestHighLevelClient client) throws Exception;

}
