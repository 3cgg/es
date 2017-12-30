package me.libme.module.es5x6;

import me.libme.kernel._c._fn.Try;
import me.libme.kernel._c._m.JModel;
import me.libme.kernel._c.bean.SimpleFieldOnClassFinder;
import me.libme.kernel._c.util.DateConverter;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by J on 2017/7/31.
 */
public class ESModel implements IESModel {

    private String indexName;

    private String type;

    private String id;

    private Map<String,Object> data=new LinkedHashMap<>();

    private Operations operations=new Operations();

    private Filter filter=new Filter();

    private Directive directive=new Directive();

    Directive directive(){
        return directive;
    }

    public Operations operations(){
        return operations;
    }

    public Filter filter(){
        return filter;
    }

    public static interface Matcher{
        boolean matcher(String key, Object value);
    }

    public class Filter{

        /**
         * FOR {@link JBaseESModel}
         */
        private Matcher INSERT=
                (key,value)->{
                return "id".equalsIgnoreCase(key)
                        ||"createId".equalsIgnoreCase(key)
                        ||"createTime".equalsIgnoreCase(key)
                        ||"updateId".equalsIgnoreCase(key)
                        ||"updateTime".equalsIgnoreCase(key)
                        ||"version".equalsIgnoreCase(key);
                };

        /**
         * FOR {@link JBaseESModel}
         */
        private Matcher UPDATE=
                (key,value)->{
                    return "id".equalsIgnoreCase(key);
                };


        public Filter filter(Matcher matcher){
            Map<String,Object> retain=new LinkedHashMap<>();
            data.forEach((key,value)->{
                if(!matcher.matcher(key,value)){
                    retain.put(key,value);
                }
            });
            data=retain;
            return this;
        }

        /**
         *
         * @see JBaseESModel
         * @return
         */
        public Filter filterInsert(){
            return filter(INSERT);
        }

        /**
         *
         * @see JBaseESModel
         * @return
         */
        public Filter filterUpdate(){
            return filter(INSERT);
        }


    }


    public class Operations{

        public Operations setObject(String key,Object value){
            if(Long.class.isInstance(value)){
                return setLong(key,(Long)value);
            }
            if(Date.class.isInstance(value)){
                return setDate(key,(Date)value);
            }
            if(String.class.isInstance(value)){
                return setString(key,(String)value);
            }
            if(Number.class.isInstance(value)){
                return setNumber(key,(Number)value);
            }
            data.put(key,value);
            return this;
        }

        public Operations setLong(String key,Long value){
            data.put(key,value);
            return this;
        }

        public Operations setDate(String key,Date value){
            data.put(key,value);
            return this;
        }

        public Operations setDate(String key,String value){
            return setDate(key,value,null);
        }

        public Operations setDate(String key,String value,DateConverter converter){
            Date date=
                    converter==null?DateConverter.option().convert(value)
                     : converter.convert(value);
            return setDate(key,date);
        }

        public Operations setString(String key,String value){
            data.put(key,value);
            return this;
        }

        public Operations setNumber(String key,Number value){
            data.put(key,value);
            return this;
        }

        public Object get(String key){
            return data.get(key);
        }

        public Object remove(String key){
            return data.remove(key);
        }

        public Operations indexName(String indexName){
            ESModel.this.indexName=indexName;
            return this;
        }

        public Operations type(String type){
            ESModel.this.type=type;
            return this;
        }

        public String indexName(){
            return indexName;
        }

        public String type(){
            return type;
        }

        public Operations id(String id){
            ESModel.this.id=id;
            return this;
        }

        public String id(){
            return id;
        }


    }



    class Directive{

//        public Update update(){
//            Update update=new Update();
//            data.forEach((key,value)->{
//                update.set(key,value)
//                ;
//            });
//            return update;
//        }

        public Map<String,Object> insert(){
            return data;
        }

    }

    @Override
    public ESModel clone() throws CloneNotSupportedException {
        ESModel esModel=new ESModel();
        esModel.data=this.data;
        esModel.indexName=this.indexName;
        esModel.type=this.type;
        esModel.id=this.id;
        return esModel;
    }

    public static ESModel parse(final JModel baseModel) throws Exception{

        if(ESModel.class.isInstance(baseModel)){
            return ((ESModel)baseModel).clone();
        }

        ESModel esModel=new ESModel();
        SimpleFieldOnClassFinder simpleFieldOnClassFinder
                =new SimpleFieldOnClassFinder(baseModel.getClass());
        simpleFieldOnClassFinder.find()
                .forEach(Try.apply(fieldMeta->{
                    Field field=fieldMeta.getField();
                    field.setAccessible(true);
                    Object value=field.get(baseModel);
                    esModel.operations.setObject(fieldMeta.getFieldName(),value);
                }));
        if(JBaseESModel.class.isInstance(baseModel)){

            esModel.operations().indexName(indexName(baseModel.getClass()));
            esModel.operations().type(type(baseModel.getClass()));
            esModel.operations().id(((JBaseESModel)baseModel).getId());
        }

        return esModel;

    }


    public static String indexName(Class<?> clazz){
        ESDocument document=clazz.getAnnotation(ESDocument.class);
        return document.indexName();
    }

    public static String type(Class<?> clazz){
        ESDocument document=clazz.getAnnotation(ESDocument.class);
        return document.type();
    }

    @Override
    public String esId() {
        return operations().id();
    }
}
