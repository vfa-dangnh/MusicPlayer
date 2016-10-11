package com.haidangkf.musicplayer.activity;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.controls.PlayerConstants;
import com.haidangkf.musicplayer.dto.Song;
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

    public static View bottomBar;
    public static ImageView imgDiscBottom;
    public static TextView tvNameBottom;
    public static ImageButton btnPlayBottom, btnPreviousBottom, btnNextBottom;
    public static LinearLayout btnShowPlayer;
    public static Animation rotateAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        loadFragment(SongFragment.class.getName());
    }

    @Override
    public void onBackPressed() {
        Log.d(Common.TAG, "fragment in back stack = " + fm.getBackStackEntryCount());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fm.getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            if (lastPressed + DURATION > System.currentTimeMillis()) {
                Common.exitApp(context);
                return;
            }
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            lastPressed = System.currentTimeMillis();
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
                loadFragmentClearBackStack(SongFragment.class.getName());
            }
            return true;
        }

        if (id == R.id.action_album) {
            Fragment fragment = getVisibleFragment();
            if (!(fragment instanceof AlbumFragment)) {
                loadFragmentClearBackStack(AlbumFragment.class.getName());
            }
            return true;
        }

        if (id == R.id.action_artist) {
            Fragment fragment = getVisibleFragment();
            if (!(fragment instanceof ArtistFragment)) {
                loadFragmentClearBackStack(ArtistFragment.class.getName());
            }
            return true;
        }

        if (id == R.id.action_folder) {
            Fragment fragment = getVisibleFragment();
            if (!(fragment instanceof FolderFragment)) {
                loadFragmentClearBackStack(FolderFragment.class.getName());
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
            // play music online
        } else if (id == R.id.nav_local) {
            loadFragmentClearBackStack(SongFragment.class.getName());
        } else if (id == R.id.nav_exit) {
            Common.exitApp(context);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // ----------------------------------------------------------------------------

    public void loadFragment(String className) {
        startFragment(className, null, true);
    }

    public void loadFragmentClearBackStack(String className) {
        // clear all back stack
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        startFragment(className, null, true);
    }

    public void init() {
        bottomBar = findViewById(R.id.bottomBar);
        btnShowPlayer = (LinearLayout) bottomBar.findViewById(R.id.btnShowPlayer);
        imgDiscBottom = (ImageView) bottomBar.findViewById(R.id.imgDisc);
        tvNameBottom = (TextView) bottomBar.findViewById(R.id.tvSongName);
        btnNextBottom = (ImageButton) bottomBar.findViewById(R.id.btnNext);
        btnPlayBottom = (ImageButton) bottomBar.findViewById(R.id.btnPlay);
        btnPreviousBottom = (ImageButton) bottomBar.findViewById(R.id.btnPrevious);
        rotateAnim = AnimationUtils.loadAnimation(context, R.anim.rotate);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fm = context.getSupportFragmentManager();

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

        /**
         * when click on player bottom bar
         */
        btnShowPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, PlayerActivity.class));
            }
        });

        checkToShowPlayerBottomBar(context);
    }

    public static void updateUIBottomBar(Context context) {
        if (Common.isServiceRunning(context, MyService.class)) {
            Song song = PlayerConstants.SONG_LIST.get(PlayerConstants.SONG_INDEX);
            tvNameBottom.setText(song.getName());
            if (PlayerConstants.SONG_PAUSED) {
                imgDiscBottom.clearAnimation();
                btnPlayBottom.setImageResource(R.drawable.btn_play);
            } else {
                imgDiscBottom.startAnimation(rotateAnim);
                btnPlayBottom.setImageResource(R.drawable.btn_pause);
            }
        }
    }

    public static boolean checkToShowPlayerBottomBar(Context context) {
        if (Common.isServiceRunning(context, MyService.class)) {
            bottomBar.setVisibility(View.VISIBLE);
            return true;
        } else {
            bottomBar.setVisibility(View.GONE); // hide player bottom bar
            return false;
        }
    }

}