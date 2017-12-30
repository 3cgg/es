package test.me.libme.extension.es5x6;

import me.libme.extension.es5x6.*;
import me.libme.kernel._c.util.JUniqueUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by J on 2017/9/24.
 */
public class TestES {

    private ESDocumentOperations esDocumentOperations;

    private String indexName="person-index";
    private String type="person";

    private final String id="a79fcef5-f32a-4a95-a841-76332f60c5bf";


    @ESDocument(indexName ="person-index",type = "person")
    public  static class Person implements IESModel {

        private String uniqueId;

        private  String name ;

        private int age;

        private String desc;

        private String deleted;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }

        public String getDeleted() {
            return deleted;
        }

        public void setDeleted(String deleted) {
            this.deleted = deleted;
        }

        @Override
        public String esId() {
            return getUniqueId();
        }
    }



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
                .configure().bulkProcessor(new SimpleBulkProcessorFactory()).ok();
        this.esDocumentOperations=restESDocumentOperations;
    }


    @Test
    public void insert(){

//        List<Person> persons=new ArrayList<>();
//        Person person1=new Person();
//        person1.setName("J");
//        person1.setAge(20);
//        person1.setUniqueId(JUniqueUtils.unique());
//        person1.setDeleted("N");
//        persons.add(person1);
//
//        for(int i=0;i<10;i++){
//            Person person=new Person();
//            person.setUniqueId(JUniqueUtils.unique());
//            person.setName("J-"+new Random(i).nextInt(999));
//            person.setAge(20+new Random(i).nextInt(999));
//            person.setDeleted("N");
//            persons.add(person);
//        }


        List<ESModel> esModels= new ArrayList<>();
        for(int i=0;i<10;i++){
            String id=JUniqueUtils.unique();
            ESModel esModel=new ESModel();
            esModel.operations()
                    .setString("esModel","Y")
                    .setString("name","江苏爱你不理科技有限公司"+new Random(i).nextInt(999))
                    .setLong("age",30l+new Random(i).nextLong())
                    .setNumber("height",1.75+new Random(i).nextDouble())
                    .setString("desc"," desc , person ? yes / no - "+new Random(i).nextInt(999) )
                    .setString("id",id)
                    .id(id)
                    .indexName(indexName)
                    .type("person");
            esModels.add(esModel);
        }

        List<IESModel> data=new ArrayList<>();
//        data.addAll(persons);
        data.addAll(esModels);

        esDocumentOperations.insert(indexName,type,data);

        try {
            Thread.sleep(100000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    @Test
    public void searchMatchuery(){

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(type);
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());


        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("name.raw", "江苏爱你不理科技有限公司836");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(matchQueryBuilder);
        searchRequest.source(sourceBuilder);


        List list=esDocumentOperations.list(indexName,type,searchRequest);
        System.out.println(list);

        try {
            Thread.sleep(100000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    @Test
    public void searchTermQuery(){


        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(type);
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());


        TermQueryBuilder matchQueryBuilder = new TermQueryBuilder("name", "有限公司");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(matchQueryBuilder);
        searchRequest.source(sourceBuilder);


        List list=esDocumentOperations.list(indexName,type,searchRequest);
        System.out.println(list);

        try {
            Thread.sleep(100000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    @Test
    public void searchWildQuery(){

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(type);
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.wildcardQuery("name", "有限*"));
        searchRequest.source(sourceBuilder);


        List list=esDocumentOperations.list(indexName,type,searchRequest);
        System.out.println(list);

        try {
            Thread.sleep(100000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }





    @Test
    public void getQuery(){

        Map data=esDocumentOperations.one(indexName,type,id);
        System.out.println(data);

        try {
            Thread.sleep(100000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    @Test
    public void updateQuery(){

        Map<String,Object> data=esDocumentOperations.one(indexName,type,id);
        ESModel esModel=new ESModel();
        data.forEach((key,value)->{
            esModel.operations()
                    .setObject(key,value);
        });


        esModel.operations()
                .setString("desc", esModel.operations().get("desc")+"  update !")
                .setString("id",id)
                .id(id)
                .indexName(indexName)
                .type("person");


        esDocumentOperations.update(indexName,type,id,esModel);

        System.out.println(data);

        try {
            Thread.sleep(100000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void deleteQuery(){

        esDocumentOperations.deletePermanently(indexName,type,id);

        System.out.println("OK");

        try {
            Thread.sleep(100000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }







}
