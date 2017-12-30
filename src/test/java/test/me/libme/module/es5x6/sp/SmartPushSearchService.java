package test.me.libme.module.es5x6.sp;

import me.libme.kernel._c._m.JPage;
import me.libme.kernel._c._m.SimplePageRequest;
import test.me.libme.module.es5x6.IndexSearchService;

/**
 * Created by J on 2017/9/27.
 */
@IndexSearchService
public interface SmartPushSearchService {

    JPage<SmartPush> searchSmartPush(String title, String summary,String categoryCode, SimplePageRequest simplePageRequest);

}
