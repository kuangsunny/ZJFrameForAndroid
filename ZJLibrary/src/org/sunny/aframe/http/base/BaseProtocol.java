package org.sunny.aframe.http.base;

import java.util.Map;

import org.sunny.aframe.http.ZJConnectorManage;
import org.sunny.aframe.http.ZJHttpCallBack;

import android.content.Context;

import com.android.volley.Request.Method;

/**
 * 通用网络请求类
 * 
 * @author sunny
 * @version 1.0
 * @created 2014-7-11
 */

public class BaseProtocol {
	
	
	/**
	 * tag为当前activity的类名，baseActivity里需要增加一个setTag()的方法，用于在页面onDestory时清除所有的请求
	 * tag用于清除请求
	 * 
	 */
	// Json_post请求
	protected static long sentPostJsonRequest(Context context, String tag, String url, Map<String, Object> params,
			Class<?> responseClass, ZJHttpCallBack httpCallBack) {
		return ZJConnectorManage.getInstance(context).sentJsonRequestHttpSRequest(Method.POST, tag, url, params,
				responseClass, httpCallBack);
	}

	// Josn_get请求
	protected static long sentGetJsonRequest(Context context, String tag, String url, Map<String, Object> params,
			Class<?> responseClass, ZJHttpCallBack httpCallBack) {
		return ZJConnectorManage.getInstance(context).sentJsonRequestHttpSRequest(Method.GET, tag, url, params,
				responseClass, httpCallBack);
	}

	// String_post请求
	protected static long sentPostStringRequest(Context context, String tag, String url, Map<String, String> params,
			Class<?> responseClass, ZJHttpCallBack httpCallBack) {
		return ZJConnectorManage.getInstance(context).sentStringHttpRequest(Method.POST, tag, url, params, responseClass,
				httpCallBack);
	}

	// String_get请求
	protected static long sentGetStringRequest(Context context, String tag, String url, Map<String, String> params,
			Class<?> responseClass, ZJHttpCallBack httpCallBack) {
		return ZJConnectorManage.getInstance(context).sentStringHttpRequest(Method.GET, tag, url, params, responseClass,
				httpCallBack);
	}
}
