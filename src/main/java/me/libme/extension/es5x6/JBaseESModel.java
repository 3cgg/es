package me.libme.extension.es5x6;

import me.libme.kernel._c._m.JModel;

import java.util.Date;


/**
 * The super class that those all implementations can be serialized
 * @author J
 *
 */
public abstract class JBaseESModel implements JModel {

	private String id;

	/**
	 * the primary key as traditional RDBMS
	 */
	private String uniqueId;

	/**
	 * create user id
	 */
	private String createId;
	
	/**
	 * update user id 
	 */
	private String updateId;
	
	/**
	 * create time
	 */
	private Date createTime;
	
	/**
	 * update time
	 */
	private Date updateTime;
	
	/**
	 * marks whether the record is deleted. Value is Y|N
	 */
	private String deleted;
	
	/**
	 * the property can limit the async operation effectively 
	 */
	private int version;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getUpdateId() {
		return updateId;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	@Override
	public JBaseESModel clone() throws CloneNotSupportedException {
		return (JBaseESModel) super.clone();
	}
	
}
