package org.sunny.aframe.http;

import java.util.HashMap;
import java.util.Map;

public class ZJHead {

	/**
	 * 自定义请求头数据
	 * 
	 * */

	public static Map<String, String> getRequestHeader() {
		Map<String, String> headParams = new HashMap<String, String>();
		headParams.put("content-type", "application/json");
		headParams.put("charset", "UTF-8");
		return headParams;
	}

}
