package me.libme.extension.es5x6;

import me.libme.kernel._c._m.JImpl;
import me.libme.kernel._c._m.JPage;
import me.libme.kernel._c._m.JPageUtil;
import me.libme.kernel._c._m.SimplePageRequest;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by J on 2017/9/23.
 */
public class RestESDocumentOperations implements ESDocumentOperations {

    private static final Logger logger= LoggerFactory.getLogger(RestESDocumentOperations.class);

    private final RestHighLevelClient client;

    private Configure configure=new Configure();

    public RestESDocumentOperations(RestHighLevelClient client) {
        this.client = client;
    }

    private volatile BulkProcessor bulkProcessor;

    /**
     * searchSourceBuilder.timeout(new TimeValue(90, TimeUnit.SECONDS)); seconds
     */
    private long timeout; // searchSourceBuilder.timeout(new TimeValue(90, TimeUnit.SECONDS));

    public Configure configure() {
        return configure;
    }

    public class Configure{

        public Configure bulkProcessor(IBulkProcessorFactory bulkProcessorFactory){
            try {
                RestESDocumentOperations.this.bulkProcessor=bulkProcessorFactory.getObject(client);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                throw new RuntimeException(e);
            }
            return this;
        }

        /**
         * set default timeout in seconds
         * @param seconds
         * @return
         */
        public Configure globalTimeout(long seconds){
            RestESDocumentOperations.this.timeout=seconds;
            return this;
        }

        public RestESDocumentOperations ok(){
            return RestESDocumentOperations.this;
        }

    }

    @Override
    public void bulkAsync(String indexName, String typeName, List<? extends IESModel> data) {
        try{
            List<IndexRequest> indexRequests=new ArrayList<>();
            for(IESModel model : data){
                IndexRequest indexRequest=indexRequest(indexName,typeName,model);
                indexRequest.opType(DocWriteRequest.OpType.INDEX);
                indexRequests.add(indexRequest);
            }
            indexRequests.forEach(bulkProcessor::add);
        }catch (IOException e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

    private IndexRequest indexRequest(String indexName, String typeName,IESModel data) throws Exception{
        IndexRequest indexRequest = new IndexRequest(indexName, typeName, data.esId())
                .source(contentBuilder(indexName, typeName, data));
        return indexRequest;
    }


    private XContentBuilder contentBuilder(String indexName, String typeName,IESModel data) throws Exception{
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        if(ESModel.class.isInstance(data)){
            ESModel esModel=(ESModel)data;
            Map<String,Object> map=esModel.directive().insert();
            for(Map.Entry<String,Object> entry :map.entrySet()){
                builder.field(entry.getKey(),entry.getValue());
            }
        }
        builder.endObject();
        return builder;
    }

    private void doShardInfo(ReplicationResponse.ShardInfo shardInfo){
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {

        }


        if(shardInfo.getSuccessful()>0){

        }

        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                logger.info(failure.nodeId()+"@node-id,"
                        +failure.shardId()+"@shard-id,"
                        +failure.index()+"@index,"
                        +failure.reason());
            }
        }

    }



    @Override
    public void insert(String indexName, String typeName, IESModel data) {

        try{
            IndexRequest indexRequest = indexRequest(indexName,typeName,data)
                    .opType(DocWriteRequest.OpType.CREATE);
            indexRequest.waitForActiveShards(ActiveShardCount.ALL);
            IndexResponse indexResponse=client.index(indexRequest);
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            doShardInfo(shardInfo);

        }catch (IOException e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void insert(String indexName, String typeName, List<? extends IESModel> data) {

        try{
            BulkRequest request = new BulkRequest();
            for(IESModel model : data){
                IndexRequest indexRequest=indexRequest(indexName,typeName,model);
                indexRequest.opType(DocWriteRequest.OpType.CREATE);
                request.add(indexRequest);
            }
            request.waitForActiveShards(ActiveShardCount.ALL);
            BulkResponse bulkResponse= client.bulk(request);
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();

                }
            }

        }catch (IOException e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(String indexName, String typeName, String id, IESModel data) {
        try{

            XContentBuilder contentBuilder=contentBuilder(indexName, typeName, data);
            UpdateRequest request = new UpdateRequest(indexName,typeName,id)
                    .doc(contentBuilder);
            request.docAsUpsert(false);
            UpdateResponse updateResponse = client.update(request);
            ReplicationResponse.ShardInfo shardInfo = updateResponse.getShardInfo();
            doShardInfo(shardInfo);

            if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {

            } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {

            } else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED) {

            } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {

            }

        }catch (IOException e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                throw new RuntimeException("document not found : "+id);
            }else{
                throw new RuntimeException(e);
            }
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(String indexName, String typeName, SearchRequest searchRequest, IESModel data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(String indexName, String typeName, IESSearchRequest searchRequest, IESModel data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(String indexName, String typeName, String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(String indexName, String typeName, SearchRequest searchRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(String indexName, String typeName, IESSearchRequest searchRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deletePermanently(String indexName, String typeName, String id) {
        try{

            DeleteRequest request = new DeleteRequest(indexName,typeName,id);
            DeleteResponse deleteResponse = client.delete(request);
            ReplicationResponse.ShardInfo shardInfo = deleteResponse.getShardInfo();
            doShardInfo(shardInfo);
            if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                throw new RuntimeException("record not found : "+id);
            }


        }catch (IOException e){
            logger.error(e.getMessage(),e);
        }catch (ElasticsearchException exception) {
            if (exception.status() == RestStatus.CONFLICT) {
                throw new RuntimeException("version conflict : "+id);
            }
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePermanently(String indexName, String typeName, SearchRequest searchRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deletePermanently(String indexName, String typeName, IESSearchRequest searchRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JPage<? extends Map> search(String indexName, String typeName, SearchRequest searchRequest, SimplePageRequest page, SearchSourceBuilder searchSourceBuilder) {
        return search(indexName, typeName, searchRequest, page,searchSourceBuilder,t->new HashMap(t),(t,highlightData)->{
            //default replace the same property
            highlightData.forEach((key,value)->{
                t.put(key,value);
            });
        });
    }

    @Override
    public <T> JPage<T> search(String indexName, String typeName, SearchRequest searchRequest, SimplePageRequest page, SearchSourceBuilder searchSourceBuilder, ESDataMapper<T> esDataMapper) {
        return search(indexName, typeName, searchRequest, page,searchSourceBuilder,esDataMapper,(t,highlightData)->{});
    }

    private void doSearchSourceBuilder(SearchSourceBuilder searchSourceBuilder){
        TimeValue timeValue= searchSourceBuilder.timeout();
        if(timeValue==null){
            searchSourceBuilder.timeout(new TimeValue(timeout, TimeUnit.SECONDS));
        }

    }

    @Override
    public <T> JPage<T> search(String indexName, String typeName, SearchRequest searchRequest, SimplePageRequest page,SearchSourceBuilder searchSourceBuilder,ESDataMapper<T> esDataMapper,ESHighlightDataConsumer<T> highlightDataConsumer) {
        try{

            List data=new ArrayList<>();

            long totalCount;
            int pageNumber=page.getPageNumber();
            int pageSize=page.getPageSize();
            searchSourceBuilder.from(pageNumber);
            searchSourceBuilder.size(pageSize);

            doSearchSourceBuilder(searchSourceBuilder);

            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);


            SearchHits searchHits = searchResponse.getHits();
            totalCount=searchHits.getTotalHits();

            for (SearchHit hit : searchHits) {
                // do something with the SearchHit
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                T dataPerHit=esDataMapper.convert(sourceAsMap);

                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                Map<String, String> highlights = new HashMap<>(highlightFields.size());

                for(Map.Entry<String, HighlightField> highlightFieldEntry :highlightFields.entrySet()){
                    StringBuffer stringBuffer=new StringBuffer();
                    HighlightField highlightField=highlightFieldEntry.getValue();
                    for(Text text:highlightField.fragments()){
                        stringBuffer.append(text.string());
                    }
                    highlights.put(highlightFieldEntry.getKey(),stringBuffer.toString());
                }
                highlightDataConsumer.collect(dataPerHit,highlights);

                data.add(dataPerHit);
            }

            int tempTotalPageNumber= JImpl.caculateTotalPageNumber(totalCount, pageSize);

            JImpl pageImpl=new JImpl();
            JPageUtil.replaceConent(pageImpl, data);
            pageImpl.setTotalRecordNumber(totalCount);
            pageImpl.setTotalPageNumber(tempTotalPageNumber);
            SimplePageRequest simplePageRequest=new SimplePageRequest(pageNumber, page.getPageSize());
            pageImpl.setPageable(simplePageRequest);
            return pageImpl;
        }catch (IOException e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public JPage<? extends Map> page(String indexName, String typeName, SearchRequest searchRequest, SimplePageRequest page) {
        return page(indexName, typeName, searchRequest, page,t->new HashMap(t));

    }

    @Override
    public <T> JPage<T> page(String indexName, String typeName, SearchRequest searchRequest, SimplePageRequest page, ESDataMapper<T> esDataMapper) {
        try{

            List data=new ArrayList<>();

            long totalCount=0;
            int pageNumber=page.getPageNumber();
            int pageSize=page.getPageSize();

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.from(pageNumber);
            sourceBuilder.size(pageSize);

            doSearchSourceBuilder(sourceBuilder);

            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);


            SearchHits searchHits = searchResponse.getHits();
            totalCount=searchHits.getTotalHits();

            for (SearchHit hit : searchHits) {
                // do something with the SearchHit
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                T obj=esDataMapper.convert(sourceAsMap);
                data.add(obj);
            }

            int tempTotalPageNumber= JImpl.caculateTotalPageNumber(totalCount, pageSize);

            JImpl pageImpl=new JImpl();
            JPageUtil.replaceConent(pageImpl, data);
            pageImpl.setTotalRecordNumber(totalCount);
            pageImpl.setTotalPageNumber(tempTotalPageNumber);
            SimplePageRequest simplePageRequest=new SimplePageRequest(pageNumber, page.getPageSize());
            pageImpl.setPageable(simplePageRequest);

            return pageImpl;


        }catch (IOException e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public JPage<? extends Map> page(String indexName, String typeName, IESSearchRequest searchRequest, SimplePageRequest page) {
        return page(indexName, typeName, searchRequest, page,t->new HashMap(t));
    }

    @Override
    public <T> JPage<T> page(String indexName, String typeName, IESSearchRequest searchRequest, SimplePageRequest page, ESDataMapper<T> esDataMapper) {
        return page(indexName, typeName, searchRequest.searchRequest(), page,esDataMapper);
    }

    @Override
    public List<? extends Map> list(String indexName, String typeName, SearchRequest searchRequest) {
        return list(indexName, typeName, searchRequest,t->new HashMap(t));
    }

    @Override
    public <T> List<T> list(String indexName, String typeName, SearchRequest searchRequest, ESDataMapper<T> esDataMapper) {
        List result=new ArrayList<>();
        try{

            doSearchSourceBuilder(searchRequest.source());

            SearchResponse searchResponse = client.search(searchRequest);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                // do something with the SearchHit
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                T obj=esDataMapper.convert(sourceAsMap);
                result.add(obj);
            }

        }catch (IOException e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public List<? extends Map> list(String indexName, String typeName, IESSearchRequest searchRequest) {
        return list(indexName, typeName, searchRequest.searchRequest(),t->new HashMap(t));
    }

    @Override
    public <T> List<T> list(String indexName, String typeName, IESSearchRequest searchRequest, ESDataMapper<T> esDataMapper) {
        return list(indexName, typeName, searchRequest.searchRequest(),esDataMapper);
    }

    @Override
    public Map one(String indexName, String typeName, SearchRequest searchRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T one(String indexName, String typeName, SearchRequest searchRequest, ESDataMapper<T> esDataMapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map one(String indexName, String typeName, IESSearchRequest searchRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T one(String indexName, String typeName, IESSearchRequest searchRequest, ESDataMapper<T> esDataMapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map one(String indexName, String typeName, String id) {
        return one(indexName, typeName, id,t->new HashMap(t));
    }

    @Override
    public <T> T one(String indexName, String typeName, String id, ESDataMapper<T> esDataMapper) {
        try{
            GetRequest getRequest = new GetRequest(indexName,typeName,id);

            GetResponse getResponse = client.get(getRequest);

            if (getResponse.isExists()) {
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
                return esDataMapper.convert(sourceAsMap);
            } else {

            }

        }catch (IOException e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {

            }
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
        return null;
    }


}
