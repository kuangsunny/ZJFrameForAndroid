package org.sunny.aframe.cache.impl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sunny.aframe.cache.AbstractCache;
import org.sunny.aframe.cache.domain.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


public class DefaultCacheFactory implements AbstractCache{
	
	private static DefaultCacheFactory dbInstance;
	
	private static DefaultCacheFactory fileInstance;
	
	/**
	 *  内存缓存数据只是局限于当前打开的应用，应用一旦推出将会被清理
	 *  应用进入后台后缓存的数据可能会被系统回收
	 */
	private static DefaultCacheFactory memoryInstance;
	
	private DefaultCacheFactory(){};
	
	private CacheType cType;
	
	private static  Map<String,Entry> cacheMap = new HashMap<String, Entry>();
	
	public DefaultCacheFactory getInstance(CacheType type)
	{
		this.cType = type;
		if(type == CacheType.DB)
		{
			if(null == dbInstance)
			{
				dbInstance = new DefaultCacheFactory();
			}
			return dbInstance;
		}
		else if(type == CacheType.FILE)
		{
			if(null == fileInstance)
			{
				fileInstance = new DefaultCacheFactory();
			}
			return fileInstance;
		}
		else if(type == CacheType.MEMORY)
		{
			if( null == memoryInstance)
			{
				memoryInstance = new DefaultCacheFactory();
			}
			return memoryInstance;
		}
		return null;
	}
	
	@Override
	public void put(String key, String value) {
		put(key, value,-1l);
	}

	@Override
	public void put(String key, int value) {
		put(key, value,-1l);
	}

	@Override
	public void put(String key, short value) {
		put(key, value,-1l);
	}

	@Override
	public void put(String key, boolean value) {
		put(key, value,-1l);
	}

	@Override
	public void put(String key, List<?> value) {
		put(key, value,-1l);
	}

	@Override
	public void put(String key, Object value) {
		put(key, value,-1l);
	}

	@Override
	public void put(String key, Set<?> value) {
		put(key, value,-1l);
	}

	@Override
	public void put(String key, JSONObject json) {
		put(key, json,-1l);
	}

	@Override
	public void put(String key, JSONArray json) {
		put(key, json,-1l);
	}

	private void putToMap(String key, Object value, long intervalTime)
	{
		if(this.cType == CacheType.MEMORY)
		{
			if(intervalTime>0)
			{
				Entry entry = new Entry();
				entry.setContent(String.valueOf(value));
				entry.setTime(System.currentTimeMillis());
				entry.setDeadLine(intervalTime);
				cacheMap.put(key, entry);
			}
			else
			{
				Entry entry = new Entry();
				entry.setContent(String.valueOf(value));
				cacheMap.put(key, entry);
			}
		}
		else if(this.cType == CacheType.DB)
		{
			
		}
	}
	@Override
	public void put(String key, String value, long intervalTime) {
		putToMap(key, value, intervalTime);
	}

	@Override
	public void put(String key, int value, long intervalTime) {
		putToMap(key, value, intervalTime);
	}

	@Override
	public void put(String key, short value, long intervalTime) {
		putToMap(key, value, intervalTime);
	}

	@Override
	public void put(String key, boolean value, long intervalTime) {
		putToMap(key, value, intervalTime);
	}

	@Override
	public void put(String key, List<?> value, long intervalTime) {
		Gson gson = new Gson();
		if(null != value && value.size()>0)
		{
			putToMap(key,gson.toJson(value) , intervalTime);
		}
		
	}

	@Override
	public void put(String key, Object value, long intervalTime) {
		Gson gson = new Gson();
		if (null != value) {
			if (this.cType == CacheType.MEMORY) {
				if (intervalTime > 0) {
					Entry entry = new Entry();
					entry.setContent(gson.toJson(value));
					entry.setClassName(value.getClass().getName());
					entry.setTime(System.currentTimeMillis());
					entry.setDeadLine(intervalTime);
					cacheMap.put(key, entry);
				} else {
					Entry entry = new Entry();
					entry.setContent(String.valueOf(value));
					cacheMap.put(key, entry);
				}
			}
		}
	}

	@Override
	public void put(String key, Set<?> value, long intervalTime) {
		Gson gson = new Gson();
		if(null != value && value.size()>0)
		{
			putToMap(key,gson.toJson(value) , intervalTime);
		}
	}

	@Override
	public void put(String key, JSONObject json, long intervalTime) {
		if(null != json)
		{
			putToMap(key,json.toString() , intervalTime);
		}
	}

	@Override
	public void put(String key, JSONArray json, long intervalTime) {
		if(null != json && json.length()>0)
		{
			putToMap(key,json.toString() , intervalTime);
		}
	}


	@Override
	public String getString(String key, String defaultValue) {
		if(this.cType == CacheType.MEMORY)
		{
			if(null == cacheMap.get(key))
			{
				return defaultValue;
			}
			else
			{
				if(cacheMap.get(key).isValide())
				{
					return cacheMap.get(key).getContent();
				}
				else
				{
					cacheMap.remove(key);
					return defaultValue;
				}
			}
		}
		return null;
	}

	@Override
	public int getInt(String key, int defaultValue) {
		
		if(this.cType == CacheType.MEMORY)
		{
			if(null == cacheMap.get(key))
			{
				return defaultValue;
			}
			else
			{
				if(cacheMap.get(key).isValide())
				{
					return Integer.parseInt(cacheMap.get(key).getContent());
				}
				else
				{
					cacheMap.remove(key);
					return defaultValue;
				}
			}
		}
		
		return 0;
	}

	@Override
	public short getShort(String key, short defaultValue) {
		if(this.cType == CacheType.MEMORY)
		{
			if(null == cacheMap.get(key))
			{
				return defaultValue;
			}
			else
			{
				if(cacheMap.get(key).isValide())
				{
					return Short.parseShort(cacheMap.get(key).getContent());
				}
				else
				{
					cacheMap.remove(key);
					return defaultValue;
				}
			}
		}
		return 0;
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		if(this.cType == CacheType.MEMORY)
		{
			if(null == cacheMap.get(key))
			{
				return defaultValue;
			}
			else
			{
				if(cacheMap.get(key).isValide())
				{
					return Boolean.parseBoolean(cacheMap.get(key).getContent());
				}
				else
				{
					cacheMap.remove(key);
					return defaultValue;
				}
			}
		}
		return false;
	}

	@Override
	public List<?> getList(String key, List<?> defaultValue) {
		if(this.cType == CacheType.MEMORY)
		{
			if(null == cacheMap.get(key))
			{
				return defaultValue;
			}
			else
			{
				if(cacheMap.get(key).isValide())
				{
					Gson gson = new Gson();
					Type type = new TypeToken<List<?>>(){}.getType();
					return gson.fromJson(cacheMap.get(key).getContent(),type);
					
				}
				else
				{
					cacheMap.remove(key);
					return defaultValue;
				}
			}
		}
		return null;
	}

	@Override
	public Object getObject(String key, Object defaultValue) {
		if(this.cType == CacheType.MEMORY)
		{
			if(null == cacheMap.get(key))
			{
				return defaultValue;
			}
			else
			{
				if(cacheMap.get(key).isValide())
				{
					Gson gson = new Gson();
					try {
						return gson.fromJson(cacheMap.get(key).getContent(),Class.forName(cacheMap.get(key).getClassName()));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					return null;
				}
				else
				{
					cacheMap.remove(key);
					return defaultValue;
				}
			}
		}
		return null;
	}

	@Override
		public Set<?> getSet(String key, Set<?> defaultValue) {
	
			if (this.cType == CacheType.MEMORY) {
				if (null == cacheMap.get(key)) {
					return defaultValue;
				} else {
					if (cacheMap.get(key).isValide()) {
						Gson gson = new Gson();
						Type type = new TypeToken<Set<?>>() {
						}.getType();
						return gson.fromJson(cacheMap.get(key).getContent(), type);
					} else {
						cacheMap.remove(key);
						return defaultValue;
					}
				}
			}
			return null;
		}

	@Override
	public JSONObject getJsonObject(String key, JSONObject defaultValue) {
		if (this.cType == CacheType.MEMORY) {
			if (null == cacheMap.get(key)) {
				return defaultValue;
			} else {
				if (cacheMap.get(key).isValide()) {
					try {
						return new JSONObject(cacheMap.get(key).getContent());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				} else {
					cacheMap.remove(key);
					return defaultValue;
				}
			}
		}
		return null;
	}

	@Override
	public JSONArray getJsonArray(String key, JSONArray defaultValue) {
		if (this.cType == CacheType.MEMORY) {
			if (null == cacheMap.get(key)) {
				return defaultValue;
			} else {
				if (cacheMap.get(key).isValide()) {
					try {
						return new JSONArray(cacheMap.get(key).getContent());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				} else {
					cacheMap.remove(key);
					return defaultValue;
				}
			}
		}
		return null;
	}

	@Override
	public void async() {

	}
	
	

}
