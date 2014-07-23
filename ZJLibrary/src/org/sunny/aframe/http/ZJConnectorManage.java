package org.sunny.aframe.http;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.sunny.aframe.ZJLoger;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 通信模块：通信的公共模块，所有的网络消息发送出去，以及接收返回的消息都将经过
 * 1、当需要发送网络请求时，将http的参数进行签名封装之后，请求http发送出去，并且每次请求都将返回生成一个请求标识，作为返回时的筛选标识；
 * 
 * @ClassName: ConnectorCore
 * @Description: 通信模块
 * @author sunny
 * 
 */
public class ZJConnectorManage {
	private static ZJConnectorManage core;
	private Context context;
    /**
     * volley请求队列
     */
	private RequestQueue mRequestQueue;
	
    /**
     * 每次请求都将返回生成一个请求标识，作为返回时的筛选标识；
     */
	private AtomicLong httpCount = new AtomicLong(0);
	
	private ZJConnectorManage(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}

	public static ZJConnectorManage getInstance(Context context) {
		if (core == null) {
			core = new ZJConnectorManage(context);
		}
		core.context = context;
		return core;
	}

	// Json请求数据
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public synchronized <T> long sentJsonRequestHttpSRequest(int method,
			String tag, String url, final Map<String, Object> requestParams,
			final Class<T> responseClass, ZJHttpCallBack httpCallBack) {
		final long flag = httpCount.incrementAndGet();
		if (requestParams == null)
			ZJLoger.state(this.getClass().getName(), "sentHttpRequest:" + flag
					+ url);
		else
			ZJLoger.state(this.getClass().getName(), "sentHttpRequest:" + flag
					+ url + requestParams.toString());
		ZJJsonRequest kjJsonRequest = new ZJJsonRequest(context, method, flag,
				url, tag, requestParams, responseClass, httpCallBack);
		mRequestQueue.add(kjJsonRequest.getRequest());
		return flag;
	}

	// String请求数据
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public synchronized <T> long sentStringHttpRequest(int method, String tag,
			String url, final Map<String, String> requestParams,
			final Class<T> responseClass, ZJHttpCallBack httpCallBack) {
		final long flag = httpCount.incrementAndGet();
		if (requestParams == null)
			ZJLoger.state(this.getClass().getName(), "sentHttpRequest:" + flag
					+ url);
		else
			ZJLoger.state(this.getClass().getName(), "sentHttpRequest:" + flag
					+ url + requestParams.toString());
		ZJStringRequest kJStringRequest = new ZJStringRequest(context, method,
				flag, url, tag, requestParams, responseClass, httpCallBack);
		mRequestQueue.add(kJStringRequest.getRequest());
		return flag;
	}

	public RequestQueue getmRequestQueue() {
		return mRequestQueue;
	}
}
