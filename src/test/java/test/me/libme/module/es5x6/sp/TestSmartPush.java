package test.me.libme.module.es5x6.sp;

import me.libme.kernel._c._m.JPage;
import me.libme.kernel._c._m.SimplePageRequest;
import org.junit.Test;
import test.me.libme.module.es5x6.TestESBase;

/**
 * Created by J on 2017/9/27.
 */
public class TestSmartPush extends TestESBase {




    @Test
    public void searchSmartPush(){

        String title ="回收的苏打水";

        String summary="回收的苏打水";

//        String categoryCode="SE_NEWS";
        String categoryCode=null;

        SmartPushSearchServiceImpl smartPushSearchService=new SmartPushSearchServiceImpl();
        smartPushSearchService.setDocumentOperations(esDocumentOperations);

        SimplePageRequest simplePageRequest=new SimplePageRequest(1,20);

        JPage<SmartPush> smartPushs= smartPushSearchService.searchSmartPush(title,summary,categoryCode,simplePageRequest);

        System.out.println(smartPushs);


    }


}
