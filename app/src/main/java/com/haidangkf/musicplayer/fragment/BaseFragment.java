/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haidangkf.musicplayer.fragment;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.activity.BaseActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    public static boolean sDisableFragmentAnimations = false;
    public Activity context;
    private Unbinder unbinder;
    ImageButton btnBack;
    TextView txtTitle;
    boolean isHomeBtn = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) context).enableStartScreen();
        iniBtnBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (sDisableFragmentAnimations) {
            Animation a = new Animation() {
            };
            a.setDuration(0);
            return a;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    protected int getActionBarSize() {
        Activity activity = getActivity();
        if (activity == null) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = activity.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    public void setTitle(String title) {
//        txtTitle = (TextView) getView().findViewById(R.id.header_title);
//        if (txtTitle != null) txtTitle.setText(title);
    }

    public void setTitle(int resTitle) {
        setTitle(getString(resTitle));
    }

    public void setIsHomeBtn(boolean isHomeBtn) {
        this.isHomeBtn = isHomeBtn;
    }

    private void iniBtnBack() {
//        btnBack = (ImageButton) getView().findViewById(R.id.header_back_btn);
//        if (btnBack != null){
////            if (isHomeBtn) {
////                btnBack.setImageResource(R.drawable.sg_ky_menu_btn_top_on);
////            }
//            btnBack.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isHomeBtn && context instanceof MainActivity){
//                        ((MainActivity)context).openTab(0);
//                    }else {
//                        context.onBackPressed();
//                    }
//                }
//            });
//        }
    }

    public void showBtnRight(int res) {
//        View btnRight = getView().findViewById(R.id.header_right_btn);
//        btnRight.setVisibility(View.VISIBLE);
//        if (btnRight instanceof ImageButton && res != 0){
//            ((ImageButton) btnRight).setImageResource(res);
//        }
    }

    protected int getScreenHeight() {
        Activity activity = getActivity();
        if (activity == null) {
            return 0;
        }
        return activity.findViewById(android.R.id.content).getHeight();
    }

    /**
     * start screen with Fragment
     *
     * @param className
     * @param bundle
     */
    public void startFragment(String className, Bundle bundle, boolean addToBackStack) {
        ((BaseActivity) getActivity()).startFragment(className, bundle, addToBackStack);
    }

    public void startFragmentNoAnimation(String className, Bundle bundle, boolean addToBackStack) {
        ((BaseActivity) getActivity()).startFragmentNoAnimation(className, bundle, addToBackStack);
    }

    public void startFragmentWithAnimation(String className, Bundle bundle, boolean addToBackStack, int in_right, int out_left, int in_left, int out_right) {
        ((BaseActivity) getActivity()).startFragmentWithAnimation(className, bundle, addToBackStack, in_right, out_left, in_left, out_right);
    }

}
