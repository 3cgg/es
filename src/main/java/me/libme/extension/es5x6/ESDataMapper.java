package me.libme.extension.es5x6;

import java.util.Map;

/**
 * Created by J on 2017/9/28.
 */
@FunctionalInterface
public interface ESDataMapper<T> {

    T convert(Map<String, Object> data) throws Exception;


}
