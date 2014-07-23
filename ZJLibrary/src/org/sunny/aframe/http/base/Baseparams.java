package org.sunny.aframe.http.base;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

/**
 * 通用参数  其他子类可继续
 * 
 * @author sunny
 * @version 1.0
 * @created 2014-7-11
 */
public class Baseparams {

	private Map<String, Object> params;
	
	/**
	 * 获取通用的参数
	 * 
	 * @return
	 */
	protected Map<String, Object> simpleParams(Context mContext) {
		Map<String, Object> params = new HashMap<String, Object>();
		return params;
	}

	public Map<String, Object> getParams() {
		return params;
	}
}
