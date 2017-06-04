package com.hanson.foundation.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hanson.core.domain.IdEntity;
import com.hanson.core.tools.CustomEnum;
import com.hanson.security.domain.User;

/**
 * 系统日志实体
 * @author hanson
 *
 */
@Entity
@Table(name="system_log")
public class SystemLog extends IdEntity{

	@ManyToOne(fetch=FetchType.LAZY)
	private User user;
	
	private String ip;
	
	private Date handle_time = new Date();
	
	private String title;
	
	@Lob
	@Column(columnDefinition="LongText")
	private String content;
	
	@Column(columnDefinition="int default 0")
	private Integer type = 0;
	
	public static enum LogType implements CustomEnum{

		REGISTER("注册",1),LOGIN("登录",5),LOGOUT("退出",10),CHANGEPWS("修改密码",10),
		VIEW("查看",15),UPDATE("更新",19),SAVE("保存",20),DELETE("删除",25),
		SPECIAL("特殊处理",45)
		;
		
		private String typeName;
		private int value;
		private LogType(String typeName, int value){
			this.typeName = typeName;
			this.value = value;
		}
		
		@Override
		public String typeName() {
			return this.typeName;
		}
		@Override
		public int value() {
			return this.value;
		}
		@Override
		public String originalName() {
			return this.name();
		}
	}
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getHandle_time() {
		return handle_time;
	}

	public void setHandle_time(Date handle_time) {
		this.handle_time = handle_time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
