package me.libme.module.es5x6;

import me.libme.kernel._c._m.JPage;
import me.libme.kernel._c._m.SimplePageRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by J on 2017/9/23.
 */
public interface ESDocumentOperations {

    JPage<? extends Map> search(String indexName,String typeName, SearchRequest searchRequest, SimplePageRequest page,SearchSourceBuilder searchSourceBuilder);

    <T> JPage<T> search(String indexName, String typeName, SearchRequest searchRequest, SimplePageRequest page,SearchSourceBuilder searchSourceBuilder,ESDataMapper<T> esDataMapper);

    <T> JPage<T> search(String indexName, String typeName, SearchRequest searchRequest, SimplePageRequest page,SearchSourceBuilder searchSourceBuilder,ESDataMapper<T> esDataMapper,ESHighlightDataConsumer<T> highlightDataConsumer);

    void insert(String indexName,String typeName,IESModel data);

    void insert(String indexName,String typeName,List<? extends IESModel> data);

    void update(String indexName,String typeName,String id,IESModel data);

    void update(String indexName, String typeName, SearchRequest searchRequest, IESModel data);

    void update(String indexName, String typeName, IESSearchRequest searchRequest, IESModel data);

    /**
     * logical delete
     * @param indexName
     * @param id
     */
    void delete(String indexName,String typeName,String id);

    /**
     * logical delete
     * @param indexName
     * @param searchRequest
     */
    void delete(String indexName,String typeName,SearchRequest searchRequest);

    /**
     * logical delete
     * @param indexName
     * @param searchRequest
     */
    void delete(String indexName,String typeName,IESSearchRequest searchRequest);

    void deletePermanently(String indexName,String typeName,String id);

    void deletePermanently(String indexName,String typeName,SearchRequest searchRequest);

    void deletePermanently(String indexName,String typeName,IESSearchRequest searchRequest);

    JPage<? extends Map> page(String indexName,String typeName, SearchRequest searchRequest, SimplePageRequest page);

    <T> JPage<T> page(String indexName, String typeName, SearchRequest searchRequest, SimplePageRequest page, ESDataMapper<T> esDataMapper);

    JPage<? extends Map> page(String indexName, String typeName, IESSearchRequest searchRequest, SimplePageRequest page);

    <T> JPage<T> page(String indexName, String typeName, IESSearchRequest searchRequest, SimplePageRequest page, ESDataMapper<T> esDataMapper);

    List<? extends Map> list(String indexName,String typeName, SearchRequest searchRequest);

    <T> List<T> list(String indexName,String typeName, SearchRequest searchRequest,ESDataMapper<T> esDataMapper);

    List<? extends Map> list(String indexName,String typeName, IESSearchRequest searchRequest);

    <T> List<T> list(String indexName, String typeName, IESSearchRequest searchRequest, ESDataMapper<T> esDataMapper);

    Map one(String indexName,String typeName, SearchRequest searchRequest);

    <T> T one(String indexName,String typeName, SearchRequest searchRequest,ESDataMapper<T> esDataMapper);

    Map one(String indexName,String typeName, IESSearchRequest searchRequest);

    <T> T one(String indexName, String typeName, IESSearchRequest searchRequest, ESDataMapper<T> esDataMapper);

    Map one(String indexName, String typeName, String id);

    <T> T one(String indexName,String typeName, String id,ESDataMapper<T> esDataMapper);


    /**
     * may insert or update if the id exists
     * @param indexName
     * @param typeName
     * @param data
     */
    void bulkAsync(String indexName,String typeName,List<? extends IESModel> data);

}
