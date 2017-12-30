package test.me.libme.extension.es5x6.bs;

import me.libme.kernel._c._m.JModel;

/**
 * Created by J on 2017/9/28.
 */
public class BsSearchCriteria implements JModel {

    private String title;

    private String technicalFieldName;

    private String introduction;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTechnicalFieldName() {
        return technicalFieldName;
    }

    public void setTechnicalFieldName(String technicalFieldName) {
        this.technicalFieldName = technicalFieldName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
