package org.sunny.example;

import org.sunny.aframe.ui.ActivityUtils;
import org.sunny.aframe.ui.activity.BaseSplash;
import org.sunny.example.activity.TabExample;

import android.widget.ImageView;

/**
 * 启动界面效果展示
 */
public class MainActivity extends BaseSplash {
    @Override
    protected void setRootBackground(ImageView view) {
        view.setBackgroundResource(R.drawable.bg);
    }

    @Override
    protected void redirectTo() {
        ActivityUtils.skipActivity(this, TabExample.class);
    }

}
