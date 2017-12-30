package me.libme.module.es5x6;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by J on 2017/9/28.
 */
public class SimpleTypeMapper<T> implements ESDataMapper<T> {

    private final Class<T> clazz;

    public SimpleTypeMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convert(Map<String, Object> data) throws Exception {
        T object=clazz.newInstance();
        for(Map.Entry<String,Object> entry:data.entrySet()){
            String fieldName=entry.getKey();
            Object value=entry.getValue();
            Class superClass=clazz;
            while(superClass!=null) {
                Field field=null;
                try {
                    field = superClass.getDeclaredField(fieldName);
                }catch (NoSuchFieldException e){

                }
                if (field != null) {
                    field.setAccessible(true);
                    if(field.getType().isInstance(value)) {
                        field.set(object, value);
                    }else{
                        throw new RuntimeException("imcompatible type : ["+fieldName+"@"+field.getType()+"] , value is ["+value+"@"+value.getClass()+"]");
                    }
                    break;
                }
                superClass=superClass.getSuperclass();
            }
        }

        return object;
    }


}
