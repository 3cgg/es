package test.me.libme.module.es5x6.sp;


import test.me.libme.module.es5x6.CPPHitBaseVO;

import java.util.Date;

public class SmartPush extends CPPHitBaseVO {

	// 标题
	private String title;

	// 摘要
	private String summary;
	
	// 来源
	private String source;
	
	// 链接
	private String link;
	
	// 存储时间
	private Date storage_time;

	private String categoryCode;


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Date getStorage_time() {
		return storage_time;
	}

	public void setStorage_time(Date storage_time) {
		this.storage_time = storage_time;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
}
