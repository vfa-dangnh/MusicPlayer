package com.haidangkf.musicplayer.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.fragment.AlbumFragment;
import com.haidangkf.musicplayer.fragment.ArtistFragment;
import com.haidangkf.musicplayer.fragment.FolderFragment;
import com.haidangkf.musicplayer.fragment.SongFragment;
import com.haidangkf.musicplayer.service.MyService;
import com.haidangkf.musicplayer.utils.CircleTransform;
import com.haidangkf.musicplayer.utils.Common;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    View navHeader;
    ImageView imgProfile;
    FragmentManager fm;

    private static final int DURATION = 2000; // time passed between two back presses
    private long lastPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // Loading profile image
        Glide.with(context).load(R.drawable.profile_img)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        fm = context.getSupportFragmentManager();
        loadFragment(SongFragment.class.getName(), true);
    }

    @Override
    public void onBackPressed() {
        Log.d(Common.TAG, "fragment in back stack = " + fm.getBackStackEntryCount());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fm.getBackStackEntryCount() == 1) {
            if (lastPressed + DURATION > System.currentTimeMillis()) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            lastPressed = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_song) {
            Fragment fragment = getVisibleFragment();
            if (!(fragment instanceof SongFragment)) {
                loadFragment(SongFragment.class.getName(), false);
            }
            return true;
        }

        if (id == R.id.action_album) {
            Fragment fragment = getVisibleFragment();
            if (!(fragment instanceof AlbumFragment)) {
                loadFragment(AlbumFragment.class.getName(), false);
            }
            return true;
        }

        if (id == R.id.action_artist) {
            Fragment fragment = getVisibleFragment();
            if (!(fragment instanceof ArtistFragment)) {
                loadFragment(ArtistFragment.class.getName(), false);
            }
            return true;
        }

        if (id == R.id.action_folder) {
            Fragment fragment = getVisibleFragment();
            if (!(fragment instanceof FolderFragment)) {
                loadFragment(FolderFragment.class.getName(), false);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_online) {
        } else if (id == R.id.nav_local) {
            loadFragment(SongFragment.class.getName(), true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // ------------------------------------------------------------

    public void loadFragment(String className, boolean clearAllBackStack) {
        if (clearAllBackStack) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        startFragment(className, null, true);
    }

    // ------------------------------------------------------------

    public static void startService(Context context) {
        Log.d(Common.TAG, "start Service");
        context.startService(new Intent(context, MyService.class));
    }

    public static void stopService(Context context) {
        Log.d(Common.TAG, "stop Service");
        context.stopService(new Intent(context, MyService.class));
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}