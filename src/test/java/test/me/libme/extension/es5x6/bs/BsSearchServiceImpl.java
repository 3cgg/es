package test.me.libme.extension.es5x6.bs;

import me.libme.extension.es5x6.ESDocumentOperations;
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

/**
 * Created by J on 2017/9/28.
 */
public class BsSearchServiceImpl implements BsSearchService {


    private ESDocumentOperations documentOperations;

    public void setDocumentOperations(ESDocumentOperations documentOperations) {
        this.documentOperations = documentOperations;
    }


    @Override
    public JPage<BsOutVO> searchBs(BsSearchCriteria bsSearchCriteria, SimplePageRequest simplePageRequest) {

        String indexName="cpp-bs-index";
        String type="bs";

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
                new HighlightBuilder.Field("introduction");
        highlightSummary.highlighterType("unified");
        highlightSummary.preTags("<font color=\"#FF0000\">");
        highlightSummary.postTags("</font>");
        highlightBuilder.field(highlightSummary);

        HighlightBuilder.Field highligthtTechnicalFieldName =
                new HighlightBuilder.Field("technicalFieldName");
        highligthtTechnicalFieldName.highlighterType("unified");
        highligthtTechnicalFieldName.preTags("<font color=\"#FF0000\">");
        highligthtTechnicalFieldName.postTags("</font>");
        highlightBuilder.field(highligthtTechnicalFieldName);

        searchSourceBuilder.highlighter(highlightBuilder);

        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();


        String title=bsSearchCriteria.getTitle();
        if(JStringUtils.isNotNullOrEmpty(title)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("title",title );
            boolQueryBuilder.should().add(matchQueryBuilder);
        }

        String introduction=bsSearchCriteria.getIntroduction();
        if(JStringUtils.isNotNullOrEmpty(introduction)){
            TermQueryBuilder matchQueryBuilder = new TermQueryBuilder("introduction", introduction);
            boolQueryBuilder.should().add(matchQueryBuilder);
        }

        String technicalFieldName=bsSearchCriteria.getTechnicalFieldName();
        if(JStringUtils.isNotNullOrEmpty(technicalFieldName)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("technicalFieldName",technicalFieldName );
            boolQueryBuilder.should().add(matchQueryBuilder);
        }

        searchSourceBuilder.query(boolQueryBuilder);

        JPage<BsOutVO> data= documentOperations.search(indexName,type,searchRequest,simplePageRequest,searchSourceBuilder,
                mapData->{
//                    DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    BsOutVO bsOutVO=new BsOutVO();
                    bsOutVO.setId((String) mapData.get("id"));
                    bsOutVO.setApprove((String) mapData.get("approve"));
                    bsOutVO.setTitle((String) mapData.get("title"));
                    bsOutVO.setIntroduction((String) mapData.get("introduction"));
                    bsOutVO.setFirstPicPath((String) mapData.get("firstPicPath"));
                    bsOutVO.setSaasCode((String) mapData.get("saasCode"));
                    bsOutVO.setIsReliable((String) mapData.get("isReliable"));
                    bsOutVO.setCodeType((String) mapData.get("codeType"));
                    bsOutVO.setTechnicalFieldName((String) mapData.get("technicalFieldName"));
                    bsOutVO.setStatus((String) mapData.get("status"));

                    return bsOutVO;
                },(smartPush,highlightData)->{
                    if(highlightData.containsKey("title")){
                        smartPush.setTitle(highlightData.get("title"));
                    }

                    if(highlightData.containsKey("introduction")){
                        smartPush.setIntroduction(highlightData.get("introduction"));
                    }

                    if(highlightData.containsKey("technicalFieldName")){
                        smartPush.setTechnicalFieldName(highlightData.get("technicalFieldName"));
                    }
                });
        return  data;

    }


}
