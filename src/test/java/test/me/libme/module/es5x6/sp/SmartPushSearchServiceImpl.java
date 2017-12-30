package test.me.libme.module.es5x6.sp;

import me.libme.module.es5x6.ESDocumentOperations;
import me.libme.kernel._c._m.JPage;
import me.libme.kernel._c._m.SimplePageRequest;
import me.libme.kernel._c.util.JStringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by J on 2017/9/27.
 */
public class SmartPushSearchServiceImpl implements SmartPushSearchService {

    private ESDocumentOperations documentOperations;

    public void setDocumentOperations(ESDocumentOperations documentOperations) {
        this.documentOperations = documentOperations;
    }

    @Override
    public JPage<SmartPush> searchSmartPush(String title, String summary, String categoryCode, SimplePageRequest simplePageRequest){

        String indexName="cpp-smart-push-index";
        String type="smart-push";

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(type);
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle =
                new HighlightBuilder.Field("title");
        highlightTitle.highlighterType("unified");
        highlightTitle.preTags("<font color=\"#FF0000\">");
        highlightTitle.postTags("</font>");
        highlightBuilder.field(highlightTitle);

        HighlightBuilder.Field highlightSummary =
                new HighlightBuilder.Field("summary");
        highlightSummary.highlighterType("unified");
        highlightSummary.preTags("<font color=\"#FF0000\">");
        highlightSummary.postTags("</font>");
        highlightBuilder.field(highlightSummary);

        searchSourceBuilder.highlighter(highlightBuilder);

        searchSourceBuilder.sort(new FieldSortBuilder("storageTime").order(SortOrder.DESC));

        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();


        if(JStringUtils.isNotNullOrEmpty(title)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("title",title );
            boolQueryBuilder.should().add(matchQueryBuilder);
        }

        if(JStringUtils.isNotNullOrEmpty(summary)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("summary",summary );
            boolQueryBuilder.should().add(matchQueryBuilder);
        }

        if(JStringUtils.isNotNullOrEmpty(categoryCode)){
            TermQueryBuilder matchQueryBuilder = new TermQueryBuilder("categoryCode", categoryCode);
            boolQueryBuilder.must().add(matchQueryBuilder);
        }

        searchSourceBuilder.query(boolQueryBuilder);

        JPage<SmartPush> data= documentOperations.search(indexName,type,searchRequest,simplePageRequest,searchSourceBuilder,
                mapData->{
                    DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    SmartPush smartPush=new SmartPush();
                    smartPush.setLink((String) mapData.get("link"));
                    smartPush.setCategoryCode((String) mapData.get("categoryCode"));
                    smartPush.setTitle((String) mapData.get("title"));
                    smartPush.setSummary((String) mapData.get("summary"));
                    smartPush.setStorage_time(dateFormat.parse((String) mapData.get("storageTime")));
                    smartPush.setSaasCode((String) mapData.get("saasCode"));
                    smartPush.setCategoryCode((String) mapData.get("categoryCode"));
                    return smartPush;
                },(smartPush,highlightData)->{
                    if(highlightData.containsKey("title")){
                        smartPush.setTitle(highlightData.get("title"));
                    }

                    if(highlightData.containsKey("summary")){
                        smartPush.setSummary(highlightData.get("summary"));
                    }
                });
        return  data;
    }


}
