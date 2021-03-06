/*
 * Copyright (c) 2014, sunny.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sunny.aframe.ui.activity;

import org.sunny.aframe.ui.AnnotateUtil;
import org.sunny.aframe.ui.I_BroadcastReg;
import org.sunny.aframe.ui.KJActivityManager;
import org.sunny.aframe.ui.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * update log:
 * @1.5 abstract protocol: I_KJActivity
 * @1.6 add method initThreadData()
 */

/**
 * Activity's framework,the developer shouldn't extends it
 * 
 * @author sunny
 * @version 1.6
 * @created 2014-3-1
 * @lastChange 2014-5-30
 */
public abstract class KJFrameActivity extends Activity implements
        OnClickListener, I_BroadcastReg, I_KJActivity {

    /** initialization widget */
    protected void initWidget() {}

    /**
     * initialization data. And this method run in background thread, so you
     * shouldn't change ui
     */
    protected void initThreadData() {}

    /** initialization data */
    protected void initData() {}

    /** initialization */
    @Override
    public void initialize() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initThreadData();
            }
        }).start();
        initWidget();
        initData();
    }

    /** listened widget's click method */
    @Override
    public void widgetClick(View v) {}

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    @Override
    public void registerBroadcast() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KJActivityManager.create().addActivity(this);
        setRootView(); // 必须放在annotate之前调用
        AnnotateUtil.initBindView(this); // 必须放在initialization之前调用
        initialize();
        registerBroadcast();
    }

    @Override
    public void unRegisterBroadcast() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcast();
        KJActivityManager.create().finishActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && KJActivityManager.create().getCount() < 2) {
            ViewInject.create().getExitDialog(this);
        }
        return super.onKeyDown(keyCode, event);
    }
}
