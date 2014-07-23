package org.sunny.aframe.http;

import org.json.JSONObject;

import com.android.volley.VolleyError;

/**
 * http接口回调，方面及时使用回调数据
 * 
 * @author sunny
 * 
 */
public interface ZJHttpCallBack {

	public <T> void onHttpSuccess(JSONObject response, T responseEntity);

	public void onHttpError(long flag, VolleyError e);

}
