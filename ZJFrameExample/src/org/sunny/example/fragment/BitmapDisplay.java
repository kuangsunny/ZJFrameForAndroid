package org.sunny.example.fragment;

import org.sunny.aframe.bitmap.KJImageLoader;
import org.sunny.aframe.ui.BindView;
import org.sunny.aframe.ui.ViewInject;
import org.sunny.aframe.ui.fragment.BaseFragment;
import org.sunny.example.R;
import org.sunny.example.activity.SlidExample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 网络图片加载Demo（在侧滑界面下则展示侧滑效果）
 * 
 * @author kymjs(kymjs123@gmail.com)
 */
public class BitmapDisplay extends BaseFragment {
    @BindView(id = R.id.button1, click = true)
    private Button button;
    @BindView(id = R.id.imageview, click = true)
    private ImageView imageView;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container,
            Bundle bundle) {
        View view = inflater.inflate(R.layout.example_layout, null);
        return view;
    }

    @Override
    protected void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
        case R.id.button1:
            if (getActivity() instanceof SlidExample) {
                ((SlidExample) getActivity()).changeSlidMenu();
            } else {
				// 载入本地图片
				// String SDPATH = Environment.getExternalStorageDirectory() +
				// "/";;
				// ImageLoader.getInstanse(getActivity()).loadLocalImage(imageView,
				// SDPATH + "1.jpg", R.drawable.ic_launcher,
				// R.drawable.ic_launcher);
				// 载入网络图片
				KJImageLoader
						.getInstanse(getActivity())
						.loadNetImage(
								"",
								imageView,
								"http://www.eoeandroid.com/data/attachment/forum/201107/18/142935bbi8d3zpf3d0dd7z.jpg",
								imageView.getWidth(), imageView.getHeight(),
								true);
				ViewInject.toast("网络图片加载");
			}
            break;
        }
    }
}
