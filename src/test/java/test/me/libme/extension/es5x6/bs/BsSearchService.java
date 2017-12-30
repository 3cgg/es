package test.me.libme.extension.es5x6.bs;

import me.libme.kernel._c._m.JPage;
import me.libme.kernel._c._m.SimplePageRequest;
import test.me.libme.extension.es5x6.IndexSearchService;

/**
 * Created by J on 2017/9/27.
 */
@IndexSearchService
public interface BsSearchService {

    JPage<BsOutVO> searchBs(BsSearchCriteria bsSearchCriteria, SimplePageRequest simplePageRequest);

}
