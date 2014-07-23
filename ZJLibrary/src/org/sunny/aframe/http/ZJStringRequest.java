package org.sunny.aframe.http;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.sunny.aframe.utils.StringUtils;

import android.content.Context;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

/**
 * 对JsonObjectRequest的封装，设置请求头，回调，并返回JsonObjectRequest的对像，用于加入volley的请求队列
 * 
 * */
public class ZJStringRequest<T> implements Listener<String>, ErrorListener {
	private int method = Method.POST;
	private Class<T> responseClass;
	private com.android.volley.toolbox.StringRequest request;
	private ZJHttpCallBack httpCallBack;
	private long flag;

	public ZJStringRequest(Context context, int method, long flag, String url,
			String tag, Map<String, String> requestParam,
			Class<T> responseClass, ZJHttpCallBack httpCallBack) {
		this.method = method;
		init(context, flag, url, tag, requestParam, responseClass, httpCallBack);

	}

	public StringRequest getRequest() {
		return request;
	}

	private void init(Context context, long flag, String url, String tag,
			Map<String, String> requestParam, Class<T> responseClass,
			ZJHttpCallBack httpCallBack) {
		this.responseClass = responseClass;
		this.httpCallBack = httpCallBack;
		this.flag = flag;
		request = getStringRequest(url, requestParam);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ZJHttpConfig.SOCKET_TIMEOUT, ZJHttpConfig.RETRYCOUNT,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setTag(tag);
	}

	private StringRequest getStringRequest(String url,
			final Map<String, String> requestParam) {
		switch (method) {
		case Method.POST:
		default:
			return new StringRequest(method, url, this, this) {

				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					return ZJHead.getRequestHeader();
				}

				@Override
				protected Map<String, String> getParams()
						throws com.android.volley.AuthFailureError {
					return requestParam;
				};

			};
		case Method.GET:
			Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
			if (requestParam != null) {
				for (String key : requestParam.keySet()) {
					uriBuilder.appendQueryParameter(key, requestParam.get(key));
				}
			}
			return new StringRequest(uriBuilder.toString(), this, this) {
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					return ZJHead.getRequestHeader();
				}
			};
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		if (httpCallBack != null)
			httpCallBack.onHttpError(flag, error);
	}

	@Override
	public void onResponse(String response) {
		try {
			if (!StringUtils.isEmpty(response)) {
				JSONObject json = new JSONObject(response);
				T responseEntity = new Gson().fromJson(response, responseClass);
				if (httpCallBack != null)
					httpCallBack.onHttpSuccess(json, responseEntity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
