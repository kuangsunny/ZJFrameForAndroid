package org.sunny.aframe.cache;

import java.util.List;
import java.util.Set;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface AbstractCache {

	public enum CacheType{
		FILE,DB,MEMORY
	}
	
	public static final int TIME_HOUR = 60 * 60;
	public static final int TIME_DAY = TIME_HOUR * 24;
	public static final int MAX_SIZE = 1000 * 1000 * 50; // 50 mb
	public static final int MAX_COUNT = Integer.MAX_VALUE; // 不限制存放数据的数量
	
	public void put(String key,String value);
	public void put(String key,int value);
	public void put(String key,short value);
	public void put(String key,boolean value);
	public void put(String key,List<?> value);
	public void put(String key,Object value);
	public void put(String key,Set<?> value);
	public void put(String key,JsonObject json);
	public void put(String key,JsonArray json);
	
	
	public void put(String key,String value,long intervalTime);
	public void put(String key,int value,long intervalTime);
	public void put(String key,short value,long intervalTime);
	public void put(String key,boolean value,long intervalTime);
	public void put(String key,List<?> value,long intervalTime);
	public void put(String key,Object value,long intervalTime);
	public void put(String key,Set<?> value,long intervalTime);
	public void put(String key,JsonObject json,long intervalTime);
	public void put(String key,JsonArray json,long intervalTime);
	
	public String getString(String key,String defaultValue);
	public int getInt(String key,int defaultValue);
	public short getShort(String key,short defaultValue);
	public boolean getBoolean(String key,boolean defaultValue);
	public List<?> getList(String key,List<?> defaultValue);
	public Object getObject(String key,Object defaultValue);
	public Set<?> getSet(String key,Set<?> defaultValue);
	public JsonObject getJsonObject(String key,JsonObject defaultValue);
	public JsonArray getJsonArray(String key,JsonArray defaultValue);
	
	public void async();
	
	

}
