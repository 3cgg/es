package test.me.libme.module.es5x6.bs;


import test.me.libme.module.es5x6.CPPHitBaseVO;

public class BsOutVO extends CPPHitBaseVO {

	//标题
	private String title;
	
	//图
	private String firstPicId;

	//是否上下架
	private String status;
	
	//是否审核通过
	private String approve;
	
	//是否可靠
	private String isReliable;
	
	//简介
	private String introduction;
	
	//标签
	private String label;
	
	//技术领域
	private String technicalFieldName;
	
	//12库每个表对应code
	private String codeType;
	
	//12库对应图片路径
	private String firstPicPath;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstPicId() {
		return firstPicId;
	}

	public void setFirstPicId(String firstPicId) {
		this.firstPicId = firstPicId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApprove() {
		return approve;
	}

	public void setApprove(String approve) {
		this.approve = approve;
	}

	public String getIsReliable() {
		return isReliable;
	}

	public void setIsReliable(String isReliable) {
		this.isReliable = isReliable;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTechnicalFieldName() {
		return technicalFieldName;
	}

	public void setTechnicalFieldName(String technicalFieldName) {
		this.technicalFieldName = technicalFieldName;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getFirstPicPath() {
		return firstPicPath;
	}

	public void setFirstPicPath(String firstPicPath) {
		this.firstPicPath = firstPicPath;
	}
}
