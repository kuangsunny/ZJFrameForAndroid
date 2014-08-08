package org.sunny.aframe.cache.domain;

import java.io.Serializable;

import org.sunny.aframe.db.annotation.Id;
import org.sunny.aframe.db.annotation.Table;
@Table(name = "entry")
public class Entry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String className;
	
	private String content;
	
	private String key;
	
	private long time;
	
	private long deadLine;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(long deadLine) {
		this.deadLine = deadLine;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isValide()
	{
		if(deadLine>0)
		{
			if(System.currentTimeMillis() - time > deadLine*1000)
			{
				return false;
			}
		}
		else
		{
			return true;
		}
		
		return true;
	}
	
	

}
