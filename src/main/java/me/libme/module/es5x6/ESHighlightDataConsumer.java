package me.libme.module.es5x6;

import java.util.Map;

/**
 * Created by J on 2017/9/28.
 */
@FunctionalInterface
public interface ESHighlightDataConsumer<T> {

    void collect(T data,Map<String, String> highlightData) throws Exception;


}
