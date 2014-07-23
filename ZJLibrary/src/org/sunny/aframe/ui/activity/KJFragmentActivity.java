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
package org.sunny.aframe.ui.activity;

import org.sunny.aframe.ui.fragment.BaseFragment;
import org.sunny.zjlibrary.R;

import android.app.FragmentTransaction;

/**
 * Application BaseActivity plus. For ease of use, your Activity should overload
 * changeFragment(Fragment frag).
 * 
 * @if you want include the Fragment,you should extends it for your Activity
 * @else you should extends KJFrameActivity for your Activity
 * 
 * @author sunny
 * @version 1.0
 * @created 2014-5-14
 */
public abstract class KJFragmentActivity extends BaseActivity {

    /** 改变界面的fragment */
    protected void changeFragment(int resView, BaseFragment targetFragment) {
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(resView, targetFragment, targetFragment.getClass()
                .getName());
        transaction.setCustomAnimations(R.anim.in_from_right,
                R.anim.out_to_left);
        transaction.commit();
    }

    /**
     * 你应该调用changeFragment(R.id.content, targetFragment);
     */
    protected abstract void changeFragment(BaseFragment targetFragment);
}
