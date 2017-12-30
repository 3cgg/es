package me.libme.extension.es5x6;

import org.elasticsearch.action.search.SearchRequest;

/**
 * Created by J on 2017/9/23.
 */
public interface IESSearchRequest {

    SearchRequest searchRequest() throws RuntimeException;

}
