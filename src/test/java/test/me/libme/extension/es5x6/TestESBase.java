package test.me.libme.extension.es5x6;

import me.libme.extension.es5x6.ESDocumentOperations;
import me.libme.extension.es5x6.RestESDocumentOperations;
import me.libme.extension.es5x6.RestHighLevelClientBuilder;
import me.libme.extension.es5x6.SimpleBulkProcessorFactory;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Before;

/**
 * Created by J on 2017/9/27.
 */
public class TestESBase {


    protected ESDocumentOperations esDocumentOperations;


    @Before
    public void init(){

        RestHighLevelClient restHighLevelClient
                =new RestHighLevelClientBuilder()
//                .addAddress("192.168.0.113",9200)
                .addAddress("192.168.93.128",9200)
                .auth("elastic","changeme")
                .addHeader("ES_REQUEST","ONLY_TEST")
                .build();

        RestESDocumentOperations restESDocumentOperations=new RestESDocumentOperations(restHighLevelClient)
                .configure()
                .bulkProcessor(new SimpleBulkProcessorFactory())
                .globalTimeout(90)
                .ok();
        this.esDocumentOperations=restESDocumentOperations;
    }

}
