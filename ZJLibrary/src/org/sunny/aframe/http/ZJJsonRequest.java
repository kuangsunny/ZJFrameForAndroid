package org.sunny.aframe.http;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.sunny.aframe.ZJLoger;
import org.sunny.aframe.utils.StringUtils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

/**
 * 对JsonObjectRequest的封装，设置请求头，回调，并返回JsonObjectRequest的对像，用于加入volley的请求队列
 * 
 * */

public class ZJJsonRequest<T> implements Listener<JSONObject>, ErrorListener {
	private int method = Method.POST;
	private Class<T> responseClass;
	private JsonObjectRequest request;
	private ZJHttpCallBack httpCallBack;
	private long flag;

	public ZJJsonRequest(Context context, int method, long flag, String url,
			String tag, Map<String, Object> requestParam,
			Class<T> responseClass, ZJHttpCallBack httpCallBack) {
		this.method = method;
		init(context, flag, url, tag, requestParam, responseClass, httpCallBack);
	}

	public JsonObjectRequest getRequest() {
		return request;
	}

	private void init(Context context, long flag, String url, String tag,
			Map<String, Object> param, Class<T> responseClass,
			ZJHttpCallBack httpCallBack) {
		this.responseClass = responseClass;
		this.httpCallBack = httpCallBack;
		this.flag = flag;
		request = getJsonObjectRequest(url, param);
		request.setTag(tag);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ZJHttpConfig.SOCKET_TIMEOUT, ZJHttpConfig.RETRYCOUNT,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	private JsonObjectRequest getJsonObjectRequest(String url,
			final Map<String, Object> param) {

		try {
			return new JsonObjectRequest(method, url, new JSONObject(
					new Gson().toJson(param)), this, this) {
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					return ZJHead.getRequestHeader();
				}
			};
		} catch (JSONException e) {
			e.printStackTrace();
			return null;

		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		if (error != null && !StringUtils.isEmpty(error.getMessage()))
			ZJLoger.state(this.getClass().getName(), "onHttpSuccess:" + flag
					+ error.toString());
		if (httpCallBack != null)
			httpCallBack.onHttpError(flag, error);
	}

	@Override
	public void onResponse(JSONObject response) {
		if (response != null) {
			ZJLoger.state(this.getClass().getName(), "onHttpSuccess:" + flag
					+ response.toString());
			T responseEntity = new Gson().fromJson(response.toString(),
					responseClass);
			if (httpCallBack != null)
				httpCallBack.onHttpSuccess(response, responseEntity);
		}
	}
}
