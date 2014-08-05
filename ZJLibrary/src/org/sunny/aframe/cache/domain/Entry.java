package org.sunny.aframe.cache.domain;

import java.io.Serializable;

public class Entry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String className;
	
	private String content;
	
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