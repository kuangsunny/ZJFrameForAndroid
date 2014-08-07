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
import org.sunny.aframe.db.FinalDb;
import org.sunny.aframe.db.FinalDb.DaoConfig;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class DefaultCacheFactory implements AbstractCache {

	private static DefaultCacheFactory dbInstance;

	private static DefaultCacheFactory fileInstance;

	/**
	 * 内存缓存数据只是局限于当前打开的应用，应用一旦推出将会被清理 应用进入后台后缓存的数据可能会被系统回收
	 */
	private static DefaultCacheFactory memoryInstance;

	private DefaultCacheFactory() {
	};

	private CacheType cType;

	private FinalDb db;

	private static Map<String, Entry> cacheMap = new HashMap<String, Entry>();

	/**
	 * 单列获取实例
	 * @param type
	 * @return
	 */
	public DefaultCacheFactory getInstance(CacheType type) {
		this.cType = type;
		if (type == CacheType.DB) {
			if (null == dbInstance) {
				dbInstance = new DefaultCacheFactory();
				DaoConfig config = new DaoConfig();
				config.setDbName("cache_db");
				db = FinalDb.create(config);
			}
			return dbInstance;
		} else if (type == CacheType.FILE) {
			if (null == fileInstance) {
				fileInstance = new DefaultCacheFactory();
			}
			return fileInstance;
		} else if (type == CacheType.MEMORY) {
			if (null == memoryInstance) {
				memoryInstance = new DefaultCacheFactory();
			}
			return memoryInstance;
		}
		return null;
	}

	/* 
	 *存字符串
	 */
	@Override
	public void put(String key, String value) {
		put(key, value, -1l);
	}

	/* 
	 *存数值
	 */
	@Override
	public void put(String key, int value) {
		put(key, value, -1l);
	}
	/* 
	 *存short类型
	 */
	@Override
	public void put(String key, short value) {
		put(key, value, -1l);
	}
	/* 
	 *存布尔类型
	 */
	@Override
	public void put(String key, boolean value) {
		put(key, value, -1l);
	}
	/* 
	 *存List
	 */
	@Override
	public void put(String key, List<?> value) {
		put(key, value, -1l);
	}
	/* 
	 *存对象
	 */
	@Override
	public void put(String key, Object value) {
		put(key, value, -1l);
	}
	/* 
	 *存集合
	 */
	@Override
	public void put(String key, Set<?> value) {
		put(key, value, -1l);
	}
	/* 
	 *存json对象
	 */
	@Override
	public void put(String key, JSONObject json) {
		put(key, json, -1l);
	}
	/* 
	 *存json数组
	 */
	@Override
	public void put(String key, JSONArray json) {
		put(key, json, -1l);
	}

	/**
	 * 公共的存粗方法
	 * @param key
	 * @param value
	 * @param intervalTime
	 */
	private void putToMap(String key, Object value, long intervalTime) {
		if (this.cType == CacheType.MEMORY) {
			if (intervalTime > 0) {
				Entry entry = new Entry();
				entry.setContent(String.valueOf(value));
				entry.setTime(System.currentTimeMillis());
				entry.setDeadLine(intervalTime);
				cacheMap.put(key, entry);
			} else {
				Entry entry = new Entry();
				entry.setContent(String.valueOf(value));
				cacheMap.put(key, entry);
			}
		} else if (this.cType == CacheType.DB) {
			List<Entry> entrys = db.findAll(Entry.class, "key=" + key);
			if (null != entrys && entrys.size() > 0) {
				entrys.get(0).setContent(String.valueOf(value));
				entrys.get(0).setTime(System.currentTimeMillis());
				entrys.get(0).setDeadLine(intervalTime);
				db.save(entrys.get(0));
			} else {
				if (intervalTime > 0) {

					Entry entry = new Entry();
					entry.setKey(key);
					entry.setContent(String.valueOf(value));
					entry.setClassName(value.getClass().getName());
					entry.setTime(System.currentTimeMillis());
					entry.setDeadLine(intervalTime);
					db.save(entry);
				} else {
					Entry entry = new Entry();
					entry.setKey(key);
					entry.setContent(String.valueOf(value));
					entry.setClassName(value.getClass().getName());
					entry.setTime(System.currentTimeMillis());
					entry.setDeadLine(intervalTime);
					db.save(entry);
				}
			}
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
		if (null != value && value.size() > 0) {
			putToMap(key, gson.toJson(value), intervalTime);
		}

	}

	/* 
	 * 存对象 将对象数据转换成json存储
	 */
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
					entry.setClassName(value.getClass().getName());
					entry.setTime(System.currentTimeMillis());
					entry.setDeadLine(intervalTime);
					cacheMap.put(key, entry);
				}
			} else if (this.cType == CacheType.DB) {
				List<Entry> entrys = db.findAll(Entry.class, "key=" + key);
				if (null != entrys && entrys.size() > 0) {
					entrys.get(0).setContent(gson.toJson(value));
					entrys.get(0).setTime(System.currentTimeMillis());
					entrys.get(0).setDeadLine(intervalTime);
					db.save(entrys.get(0));
				} else {
					if (intervalTime > 0) {

						Entry entry = new Entry();
						entry.setKey(key);
						entry.setContent(gson.toJson(value));
						entry.setClassName(value.getClass().getName());
						entry.setTime(System.currentTimeMillis());
						entry.setDeadLine(intervalTime);
						db.save(entry);
					} else {
						Entry entry = new Entry();
						entry.setKey(key);
						entry.setContent(gson.toJson(value));
						entry.setClassName(value.getClass().getName());
						entry.setTime(System.currentTimeMillis());
						entry.setDeadLine(intervalTime);
						db.save(entry);
					}
				}

			}
		}
	}

	@Override
	public void put(String key, Set<?> value, long intervalTime) {
		Gson gson = new Gson();
		if (null != value && value.size() > 0) {
			putToMap(key, gson.toJson(value), intervalTime);
		}
	}

	@Override
	public void put(String key, JSONObject json, long intervalTime) {
		if (null != json) {
			putToMap(key, json.toString(), intervalTime);
		}
	}

	@Override
	public void put(String key, JSONArray json, long intervalTime) {
		if (null != json && json.length() > 0) {
			putToMap(key, json.toString(), intervalTime);
		}
	}

	@Override
	public String getString(String key, String defaultValue) {
		if (this.cType == CacheType.MEMORY) {
			if (null == cacheMap.get(key)) {
				return defaultValue;
			} else {
				if (cacheMap.get(key).isValide()) {
					return cacheMap.get(key).getContent();
				} else {
					cacheMap.remove(key);
					return defaultValue;
				}
			}
		} else if (this.cType == CacheType.DB) {
			List<Entry> entrys = db.findAllByWhere(Entry.class, "key=" + key);
			if (null != entrys && entrys.size() > 0) {
				if (entrys.get(0).isValide()) {
					return entrys.get(0).getContent();
				} else {
					db.delete(entrys.get(0));
					return defaultValue;
				}

			}
			return defaultValue;
		}
		return null;
	}

	@Override
	public int getInt(String key, int defaultValue) {

		if (this.cType == CacheType.MEMORY) {
			if (null == cacheMap.get(key)) {
				return defaultValue;
			} else {
				if (cacheMap.get(key).isValide()) {
					return Integer.parseInt(cacheMap.get(key).getContent());
				} else {
					cacheMap.remove(key);
					return defaultValue;
				}
			}
		} else if (this.cType == CacheType.DB) {
			List<Entry> entrys = db.findAllByWhere(Entry.class, "key=" + key);
			if (null != entrys && entrys.size() > 0) {
				if (entrys.get(0).isValide()) {
					return Integer.parseInt(entrys.get(0).getContent());
				} else {
					db.delete(entrys.get(0));
					return defaultValue;
				}
			}
			return defaultValue;
		}

		return 0;
	}

	@Override
	public short getShort(String key, short defaultValue) {
		if (this.cType == CacheType.MEMORY) {
			if (null == cacheMap.get(key)) {
				return defaultValue;
			} else {
				if (cacheMap.get(key).isValide()) {
					return Short.parseShort(cacheMap.get(key).getContent());
				} else {
					cacheMap.remove(key);
					return defaultValue;
				}
			}
		} else if (this.cType == CacheType.DB) {
			List<Entry> entrys = db.findAllByWhere(Entry.class, "key=" + key);
			if (null != entrys && entrys.size() > 0) {
				if (entrys.get(0).isValide()) {
					return Short.parseShort(entrys.get(0).getContent());
				} else {
					db.delete(entrys.get(0));
					return defaultValue;
				}
			}
			return defaultValue;
		}
		return 0;
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		if (this.cType == CacheType.MEMORY) {
			if (null == cacheMap.get(key)) {
				return defaultValue;
			} else {
				if (cacheMap.get(key).isValide()) {
					return Boolean.parseBoolean(cacheMap.get(key).getContent());
				} else {
					cacheMap.remove(key);
					return defaultValue;
				}
			}
		} else if (this.cType == CacheType.DB) {
			List<Entry> entrys = db.findAllByWhere(Entry.class, "key=" + key);
			if (null != entrys && entrys.size() > 0) {
				if (entrys.get(0).isValide()) {
					return Boolean.parseBoolean(entrys.get(0).getContent());
				} else {
					db.delete(entrys.get(0));
					return defaultValue;
				}

			}
			return defaultValue;
		}
		return false;
	}

	@Override
	public List<?> getList(String key, List<?> defaultValue) {
		if (this.cType == CacheType.MEMORY) {
			if (null == cacheMap.get(key)) {
				return defaultValue;
			} else {
				if (cacheMap.get(key).isValide()) {
					Gson gson = new Gson();
					Type type = new TypeToken<List<?>>() {
					}.getType();
					return gson.fromJson(cacheMap.get(key).getContent(), type);

				} else {
					cacheMap.remove(key);
					return defaultValue;
				}
			}
		} else if (this.cType == CacheType.DB) {
			List<Entry> entrys = db.findAllByWhere(Entry.class, "key=" + key);
			if (null != entrys && entrys.size() > 0) {
				if (entrys.get(0).isValide()) {
					Gson gson = new Gson();
					Type type = new TypeToken<List<?>>() {
					}.getType();
					return gson.fromJson(entrys.get(0).getContent(), type);
				} else {
					db.delete(entrys.get(0));
					return defaultValue;
				}
			}
			return defaultValue;
		}
		return null;
	}

	@Override
	public Object getObject(String key, Object defaultValue) {
		if (this.cType == CacheType.MEMORY) {
			if (null == cacheMap.get(key)) {
				return defaultValue;
			} else {
				if (cacheMap.get(key).isValide()) {
					Gson gson = new Gson();
					try {
						return gson
								.fromJson(cacheMap.get(key).getContent(), Class
										.forName(cacheMap.get(key)
												.getClassName()));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					return null;
				} else {
					cacheMap.remove(key);
					return defaultValue;
				}
			}
		} else if (this.cType == CacheType.DB) {
			List<Entry> entrys = db.findAllByWhere(Entry.class, "key=" + key);
			if (null != entrys && entrys.size() > 0) {

				if (entrys.get(0).isValide()) {
					Gson gson = new Gson();
					try {
						return gson.fromJson(entrys.get(0).getContent(),
								Class.forName(entrys.get(0).getClassName()));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					db.delete(entrys.get(0));
					return defaultValue;
				}

			}
			return defaultValue;
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
		} else if (this.cType == CacheType.DB) {
			List<Entry> entrys = db.findAllByWhere(Entry.class, "key=" + key);
			if (null != entrys && entrys.size() > 0) {

				if (entrys.get(0).isValide()) {
					Gson gson = new Gson();
					Type type = new TypeToken<Set<?>>() {
					}.getType();
					return gson.fromJson(entrys.get(0).getContent(), type);
				} else {
					db.delete(entrys.get(0));
					return defaultValue;
				}

			}
			return defaultValue;
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
		} else if (this.cType == CacheType.DB) {
			List<Entry> entrys = db.findAllByWhere(Entry.class, "key=" + key);
			if (null != entrys && entrys.size() > 0) {
				if (entrys.get(0).isValide()) {
					try {
						return new JSONObject(entrys.get(0).getContent());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					db.delete(entrys.get(0));
					return defaultValue;
				}

			}
			return defaultValue;
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
		} else if (this.cType == CacheType.DB) {
			List<Entry> entrys = db.findAllByWhere(Entry.class, "key=" + key);
			if (null != entrys && entrys.size() > 0) {

				if (entrys.get(0).isValide()) {
					try {
						return new JSONArray(entrys.get(0).getContent());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					db.delete(entrys.get(0));
					return defaultValue;
				}

			}
			return defaultValue;
		}
		return null;
	}

	@Override
	public void async() {

	}

}
