package test.me.libme.extension.es5x6;

import java.util.Date;

/**
 * Created by J on 2017/9/27.
 */
public class CPPHitBaseVO {


    protected String id;

    protected int version;

    /**
     * 0 为无效
     * 1 为有效
     */
    protected int isAvailable=1;

    protected String creatorId;

    protected Date createDate;

    protected String modifierId;

    protected Date modifyDate;

    protected String saasCode;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(int isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getSaasCode() {
        return saasCode;
    }

    public void setSaasCode(String saasCode) {
        this.saasCode = saasCode;
    }
}
