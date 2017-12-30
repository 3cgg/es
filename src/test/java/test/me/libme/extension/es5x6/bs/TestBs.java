package test.me.libme.extension.es5x6.bs;

import me.libme.kernel._c._m.JPage;
import me.libme.kernel._c._m.SimplePageRequest;
import org.junit.Test;
import test.me.libme.extension.es5x6.TestESBase;
import test.me.libme.extension.es5x6.sp.SmartPush;
import test.me.libme.extension.es5x6.sp.SmartPushSearchServiceImpl;

/**
 * Created by J on 2017/9/27.
 */
public class TestBs extends TestESBase {




    @Test
    public void searchSmartPush(){

        String title ="石墨烯";

        String instroduction="石墨烯";

        BsSearchCriteria bsSearchCriteria=new BsSearchCriteria();
        bsSearchCriteria.setTitle(title);
        bsSearchCriteria.setIntroduction(instroduction);

        BsSearchServiceImpl bsSearchService=new BsSearchServiceImpl();
        bsSearchService.setDocumentOperations(esDocumentOperations);

        SimplePageRequest simplePageRequest=new SimplePageRequest(0,20);

        JPage<BsOutVO> bsOuts= bsSearchService.searchBs(bsSearchCriteria,simplePageRequest);

        System.out.println(bsOuts);


    }


}
