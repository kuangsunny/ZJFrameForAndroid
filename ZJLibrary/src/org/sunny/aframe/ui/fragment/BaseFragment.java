/*
 * Copyright (c) 2014-2015, sunny.
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
package org.sunny.aframe.ui.fragment;

import org.sunny.aframe.ZJLoger;

import android.os.Bundle;

/**
 * Application's base Fragment,you should inherit it for your Fragment
 * 
 * @author sunny
 * @version 1.0
 * @created 2014-5-28
 */
public abstract class BaseFragment extends KJFrameFragment {

    /***************************************************************************
     * 
     * print Fragment callback methods
     * 
     ***************************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZJLoger.state(this.getClass().getName(), "---------onCreateView ");
    }

    @Override
    public void onResume() {
        ZJLoger.state(this.getClass().getName(), "---------onResume ");
        super.onResume();
    }

    @Override
    public void onPause() {
        ZJLoger.state(this.getClass().getName(), "---------onPause ");
        super.onPause();
    }

    @Override
    public void onStop() {
        ZJLoger.state(this.getClass().getName(), "---------onStop ");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        ZJLoger.state(this.getClass().getName(), "---------onDestroy ");
        super.onDestroyView();
    }
}
