package org.sunny.example.fragment;

import org.sunny.aframe.bitmap.KJImageLoader;
import org.sunny.aframe.ui.BindView;
import org.sunny.aframe.ui.fragment.BaseFragment;
import org.sunny.example.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class ListBitmapExample extends BaseFragment {
    @BindView(id = R.id.listview)
    private ListView listview;

    private Activity aty;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container,
            Bundle bundle) {
        aty = getActivity();
        return inflater.inflate(R.layout.listview, null);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        listview.setAdapter(new ListviewAdapter());
    }

    class ListviewAdapter extends BaseAdapter {

        ImageView image;

        @Override
        public int getCount() {
            return 30;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = image = new ImageView(aty);
			} else {
				image = (ImageView) convertView;
			}
			KJImageLoader.getInstanse(getActivity()).loadLocalImage(image,
					"/storage/sdcard0/1.jpg", image.getWidth(),
					image.getHeight() , true);
			// kjb.display(image,
			// "http://173.194.72.31/images/srpr/logo11w.png");
			return convertView;
		}
    }
}
