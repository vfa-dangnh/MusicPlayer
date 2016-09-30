package com.haidangkf.musicplayer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.fragment.BaseFragment;

import java.util.List;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {
    ImageButton btnBack;
    TextView txtTitle;

    public FragmentActivity context;
    private boolean isStartingFragment = false;
    private boolean isPushTop = false;
    private boolean isHomeBtn = false;

    /**
     * enable start screen
     */
    public void enableStartScreen() {
        // enable tab host for can click
        isStartingFragment = false;
    }

    /**
     * enable start screen
     */
    public void disableStartScreen() {
        // enable tab host for can click
        isStartingFragment = true;
    }

    /**
     * check starting screen
     */
    public boolean isStartingFragment() {
        return isStartingFragment;
    }

    public void startFragment(String className, Bundle bundle, boolean addToBackStack) {
        if (!isStartingFragment()) {
            disableStartScreen();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//			fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left
//					, R.anim.slide_out_right);

            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left
                    , R.anim.slide_out_right);

            BaseFragment fragment = (BaseFragment) Fragment.instantiate(this, className, bundle);

            fragmentTransaction.replace(R.id.frameView, fragment, className);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(className);
            }
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void startFragmentWithAnimation(String className, Bundle bundle, boolean addToBackStack, int in_right, int out_left, int in_left, int out_right) {
        if (!isStartingFragment()) {
            disableStartScreen();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//			fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left
//					, R.anim.slide_out_right);

            fragmentTransaction.setCustomAnimations(in_right, out_left, in_left
                    , out_right);

            BaseFragment fragment = (BaseFragment) Fragment.instantiate(this, className, bundle);

            fragmentTransaction.replace(android.R.id.tabcontent, fragment, className);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(className);
            }
            fragmentTransaction.commitAllowingStateLoss();
        }
    }


    public void startFragmentLeftRight(String className, Bundle bundle, boolean addToBackStack) {
        if (!isStartingFragment()) {
            disableStartScreen();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//			fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left
//					, R.anim.slide_out_right);

            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right
                    , R.anim.slide_out_left);

            BaseFragment fragment = (BaseFragment) Fragment.instantiate(this, className, bundle);

            fragmentTransaction.replace(android.R.id.tabcontent, fragment, className);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(className);
            }
            fragmentTransaction.commitAllowingStateLoss();
        }
    }


    public void startFragmentSlideUpDown(String className, Bundle bundle, boolean addToBackStack) {
        if (!isStartingFragment()) {
            disableStartScreen();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.no_change, R.anim.no_change
                    , R.anim.slide_down);
            BaseFragment fragment = (BaseFragment) Fragment.instantiate(this, className, bundle);

            fragmentTransaction.replace(android.R.id.tabcontent, fragment, className);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(className);
            }
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public Fragment startFragmentNoAnimation(String className, Bundle bundle, boolean addToBackStack) {
        if (!isStartingFragment()) {
            disableStartScreen();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//			fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_left);
//			fragmentTransaction.setCustomAnimations(R.anim.app_enter, R.anim.app_exit);
            BaseFragment fragment = (BaseFragment) Fragment.instantiate(this, className, bundle);

            fragmentTransaction.replace(android.R.id.tabcontent, fragment, className);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(className);
            }
            fragmentTransaction.commitAllowingStateLoss();
            return fragment;
        }
        return null;
    }

    /**
     * list screen hide tab bar
     */
    private static final String[] SCREEN_HIDE_TAB_BAR = new String[]{
//            TutorialActivity.class.getSimpleName(),
//            LoginActivity.class.getSimpleName(),
//            PointCheckInActivity.class.getSimpleName(),
    };
    /////////////////////////////////////////////////
    // Private fields
    /////////////////////////////////////////////////
    //flag for prevent start activity 2 times
    protected boolean mIsCanStartActivity = true;
    protected boolean mReInitialized = false;

    /////////////////////////////////////////////////
    // Public method
    /////////////////////////////////////////////////


    ///////////////////////////////////////////////////
    // Override methods
    ///////////////////////////////////////////////////
    /* (non-Javadoc)
         * @see android.support.v4.app.FragmentActivity#onStart()
         */
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /* (non-Javadoc)
         * @see android.app.Activity#setContentView(int)
         */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        context = this;
        enableStartScreen();
        iniBtnBack();
        ActivityInfo activityInfo = null;
        try {
            activityInfo = getPackageManager().getActivityInfo(
                    getComponentName(), PackageManager.GET_META_DATA);

            String title = activityInfo.loadLabel(getPackageManager())
                    .toString();
            setTitle(title);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void setIsHomeBtn(boolean isHomeBtn) {
        this.isHomeBtn = isHomeBtn;
    }

    private void iniBtnBack() {
//        btnBack = (ImageButton) findViewById(R.id.header_back_btn);
//        if (btnBack != null) {
////			if (isHomeBtn) btnBack.setImageResource(R.drawable.sg_ky_menu_btn_top_off);
//            btnBack.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onBackPressed();
//                }
//            });
//        }
    }

    public void setTitle(int resTitle) {
        setTitle(getString(resTitle));
    }

    public void setTitle(String title) {
//        if (txtTitle == null) txtTitle = (TextView) findViewById(R.id.header_title);
//        if (txtTitle != null) txtTitle.setText(title);
    }

    /* (non-Javadoc)
         * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
         */
    @Override
    protected void onCreate(Bundle saveIntanceBundle) {
        super.onCreate(saveIntanceBundle);
        setPushTop(false);
        if (saveIntanceBundle == null) {
            mReInitialized = false;
        } else {
            mReInitialized = true;
        }
    }

    public void setPushTop(boolean isPushTop) {
        this.isPushTop = isPushTop;
        if (!this.isPushTop) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            overridePendingTransition(R.anim.slide_up, R.anim.no_change);
        }
    }

    /*
     * (非 Javadoc)
     * @see android.app.Activity#startActivityForResult(android.content.Intent, int)
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (mIsCanStartActivity) {
            mIsCanStartActivity = false;
            super.startActivityForResult(intent, requestCode);
        }
    }

    /*
     * (非 Javadoc)
     * @see android.app.Activity#startActivityFromChild(android.app.Activity,
     * android.content.Intent, int)
     */
    @Override
    public void startActivityFromChild(Activity child, Intent intent, int requestCode) {
        initMoveNextActivity(intent);
        super.startActivityFromChild(child, intent, requestCode);
    }

    /*
     * (非 Javadoc)
     * @see android.app.Activity#startActivityIfNeeded(android.content.Intent, int)
     */
    @Override
    public boolean startActivityIfNeeded(Intent intent, int requestCode) {
        initMoveNextActivity(intent);
        return super.startActivityIfNeeded(intent, requestCode);
    }


    /**
     * on resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        mIsCanStartActivity = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
//        overridePendingTransition(0, 0);
    }

    /**
     * * (非 Javadoc)
     * * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        initMovePrevActivity();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * (非 Javadoc)
     * @see android.app.Activity#finish()
     */
    @Override
    public void finish() {
        initMovePrevActivity();
        hideKeyboard();
        super.finish();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Private function
    ////////////////////////////////////////////////////////////////////////////

    /**
     * get current fragment
     *
     * @return
     */
    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        Fragment f = null;
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    f = fragment;

                }
            }
        }
        return f;
    }

    public void popToTopFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            int sizeFrag = fragments.size();
            while (sizeFrag > 1) {
                Fragment fragment = fragments.get(sizeFrag - 1);
                fragmentTransaction.remove(fragment);
                fragments.remove(sizeFrag - 1);
                sizeFrag = fragments.size();
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//		outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
//		super.onSaveInstanceState(outState);
    }

    private void initMoveNextActivity(Intent moveIntent) {
        moveIntent.setFlags(moveIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

    private void initMovePrevActivity() {
        overridePendingTransition(0, 0);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView()
                .getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!isPushTop) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            overridePendingTransition(R.anim.no_change, R.anim.slide_down);
        }

    }
    //	public void startActivity(Intent intent){
//		super.startActivity(intent);
//		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//	}
}
