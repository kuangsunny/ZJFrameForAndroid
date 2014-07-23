package org.sunny.aframe.bitmap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import org.sunny.aframe.bitmap.core.BitmapCallBack;
import org.sunny.aframe.bitmap.core.MemoryCache;
import org.sunny.aframe.bitmap.utils.BitmapCreate;
import org.sunny.aframe.http.ZJConnectorManage;
import org.sunny.aframe.utils.FileUtils;
import org.sunny.aframe.utils.StringUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

/**
 * The BitmapLibrary's core classes
 * 
 * @author kymjs(kymjs123@gmail.com)
 * @version 1.0
 * @created 2014-7-11
 */
public class KJImageLoader {
	/**
	 * volley请求队列
	 */
	public static RequestQueue requesQueue = null;

	/** 记录所有正在下载或等待下载的任务 */
	private Set<BitmapWorkerTask> taskCollection;
	/** 缓存大小 */
	private MemoryCache mMemoryCache;

	private static KJImageLoader instanse;

	/** 图片载入状态将会回调相应的方法 */
	public BitmapCallBack callBack;

	/**
	 * 图片加载的配置器：以static修饰的配置器，保证一次设置可一直使用
	 */
	public static KJBitmapConfig config;

	private KJImageLoader(Context context) {
		if (config == null) {
			config = new KJBitmapConfig(context);
		}
		mMemoryCache = new MemoryCache(config.memoryCacheSize);
		taskCollection = new HashSet<BitmapWorkerTask>();
		requesQueue = ZJConnectorManage.getInstance(context).getmRequestQueue();
	}

	public static KJImageLoader getInstanse(Context context) {
		if (instanse == null) {
			instanse = new KJImageLoader(context);
		}
		return instanse;
	}

	/**
	 * 加载显示图片
	 * 
	 * @param imageView
	 *            显示图片的组件
	 * @param url
	 *            图片加载的URL
	 * @param defaultImageResId
	 *            默认的占位显示图片
	 * @param errorImageResId
	 *            错误的占位显示图片
	 */
	public void loadNetImage(String tag, final View imageView,
			final String url, int imgW, int imgH, final boolean shouldCache) {
		if (imageView instanceof ImageView) {
			((ImageView) imageView).setImageBitmap(config.loadingBitmap);
		} else {
			imageView.setBackgroundDrawable(new BitmapDrawable(
					config.loadingBitmap));
		}
		if (StringUtils.isEmpty(url))
			return;
		final String imgaeUrl = url;
		imageView.setTag(imgaeUrl);
		if (callBack != null) {
			callBack.imgLoading(imageView);
		}
		ImageRequest imageRquest = new ImageRequest(imgaeUrl,
				new Listener<Bitmap>() {

					@Override
					public void onResponse(Bitmap response) {
						if (imageView instanceof ImageView) {
							((ImageView) imageView).setImageBitmap(response);
						} else {
							imageView.setBackgroundDrawable(new BitmapDrawable(
									response));
						}
						if (callBack != null) {
							callBack.imgLoadSuccess(imageView);
						}
						
					}

				}, imgW, imgH, Config.RGB_565, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (imageView instanceof ImageView) {
							((ImageView) imageView)
									.setImageBitmap(config.errorBitmap);
						} else {
							imageView.setBackgroundDrawable(new BitmapDrawable(
									config.errorBitmap));
						}
						if (callBack != null) {
							callBack.imgLoadFailure(url, error.toString());
						}
						
					}
				});
		if (config.openMemoryCache) {
			imageRquest.setShouldCache(shouldCache);
		}
		imageRquest.setTag(tag);
		requesQueue.add(imageRquest);
	}

	/**
	 * 加载图片（核心方法）
	 * 
	 * @param imageView
	 *            要显示图片的控件(ImageView设置src，普通View设置bg)
	 * @param imageUrl
	 *            图片的URL
	 */
	public void loadLocalImage(View imageView, String imageUrl, int imgW,
			int imgH, boolean shouldCache) {
		final Bitmap bitmap = mMemoryCache.get(imageUrl);
		if (bitmap != null) {
			if (imageView instanceof ImageView) {
				((ImageView) imageView).setImageBitmap(bitmap);
			} else {
				imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
			}
		} else {
			if (imageView instanceof ImageView) {
				((ImageView) imageView).setImageBitmap(config.loadingBitmap);
			} else {
				imageView.setBackgroundDrawable(new BitmapDrawable(
						config.loadingBitmap));
			}
			if (callBack != null) {
				callBack.imgLoading(imageView);
			}
			BitmapWorkerTask task = new BitmapWorkerTask(imageView, imgW, imgH,
					shouldCache);
			taskCollection.add(task);
			task.execute(imageUrl);
		}
	}

	/********************* 异步获取Bitmap并设置image的任务类 *********************/
	private class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
		private View imageView;
		private int imgW;
		private int imgH;
		private String url;
		boolean shouldCache;

		public BitmapWorkerTask(View imageview, int imgW, int imgH,
				boolean shouldCache) {
			this.imageView = imageview;
			this.imgW = imgW;
			this.imgH = imgH;
			this.shouldCache = shouldCache;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = null;
			url = params[0];
			// 从指定链接调取image
			byte[] res = loadImgFromFile(params[0]);
			if (res != null) {
				bitmap = BitmapCreate.bitmapFromByteArray(res, 0, res.length,
						imgW, imgH);
				if (callBack != null) {
					callBack.imgLoadSuccess(imageView);
				}
			}
			if (bitmap != null) {
				if (config.openMemoryCache) {
					if (shouldCache) {
						mMemoryCache.put(params[0], bitmap); // 图片载入完成后缓存到LrcCache中
					}
				}
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			if (bitmap != null) {
				if (imageView instanceof ImageView) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
					imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
				}
			} else {
				if (imageView instanceof ImageView) {
					((ImageView) imageView).setImageBitmap(config.errorBitmap);
				} else {
					imageView.setBackgroundDrawable(new BitmapDrawable(
							config.errorBitmap));
				}
				if (callBack != null) {
					callBack.imgLoadFailure(url, "图片加载出错");
				}
			}

			taskCollection.remove(this);
		}
	}

	/**
	 * 从本地载入一张图片
	 * 
	 * @param imagePath
	 *            图片的地址
	 */
	private byte[] loadImgFromFile(String imagePath) {
		byte[] data = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(imagePath);
			if (fis != null) {
				data = FileUtils.input2byte(fis);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			FileUtils.closeIO(fis);
		}
		return data;
	}

	public BitmapCallBack getCallBack() {
		return callBack;
	}

	public void setCallBack(BitmapCallBack callBack) {
		this.callBack = callBack;
	}

}
