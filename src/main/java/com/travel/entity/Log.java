package com.travel.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 操作日志
 */
@Entity
@Table(name = "travel_log")
public class Log extends BaseEntry{
	
	/**
	 * 有效
	 */
	public static final String STATUS_VALIDE = "1";
	/**
	 * 无效
	 */
	public static final String STATUS_INVALIDE = "0";
	
	/**
	 * 首页查看日志
	 */
	public static final String TYPE_SUMMARY = "1";
	/**
	 * 账号管理日志
	 */
	public static final String TYPE_USER = "2";
	
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 创建人
	 */
	private String crUser;
	
	
	private String type;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCrUser() {
		return crUser;
	}
	public void setCrUser(String crUser) {
		this.crUser = crUser;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
