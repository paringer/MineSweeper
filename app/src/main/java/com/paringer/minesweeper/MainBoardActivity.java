package com.paringer.minesweeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, View.OnLongClickListener, OnGameStateListener {
    Board board;
    GameBox game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        SkinMinesweeper skin = new SkinMinesweeper(this);
        game = new GameBox(skin);
        GridLayout gridLayout = (GridLayout) this.findViewById(R.id.board);
        game.updateScreenSize(getWidth(), getHeight());
        game.newGame(this, gridLayout);
        game.setOnGameStateListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("MINES: " + game.getMinesMarked() + " / " + game.getMinesTotal());

        Date date = new Date();
        Calendar instance = GregorianCalendar.getInstance();
        instance.set(2016, 8, 28);
        if(date.after(instance.getTime())) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("Trial period has expired")
                    .setMessage("please donate :)")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            })
            .show();
        }
    }

    public int getWidth() {
        Rect r = new Rect();
        GridLayout gridLayout = (GridLayout) this.findViewById(R.id.board);
        gridLayout.getWindowVisibleDisplayFrame(r);
        int t1 = r.width();
        int t2 = gridLayout.getMeasuredWidth();
        Rect r1 = new Rect();
        Rect r2 = new Rect();
        Rect r3 = new Rect();
        gridLayout.getGlobalVisibleRect(r1);
        gridLayout.getGlobalVisibleRect(r2);
        gridLayout.getDrawingRect(r3);
        t1 -= getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin)*2;
        return t1;
    }

    public int getHeight() {
        ActionBar supportActionBar = this.getSupportActionBar();
        android.app.ActionBar actionBar = this.getActionBar();
        int t = 0;
        if (supportActionBar!=null) t+= supportActionBar.getHeight();
        if (actionBar!=null) t+= actionBar.getHeight();
        int actionBarHeight = t;
        TypedValue tv = new TypedValue();
        if (actionBarHeight == 0 && getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());

        if(actionBarHeight == 0 && getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)){
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        if(actionBarHeight == 0 && getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true)){
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
//        if(actionBarHeight == 0 && getTheme().resolveAttribute(com.actionbarsherlock.R.attr.actionBarSize, tv, true)){
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
//        }
        t += actionBarHeight;

        int t1 = this.getWindowManager().getDefaultDisplay().getHeight() - getStatusBarHeight() - t;
        Rect r = new Rect();
        GridLayout gridLayout = (GridLayout) this.findViewById(R.id.board);
        gridLayout.getWindowVisibleDisplayFrame(r);
        int t2 = r.height();
        t1 -= getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin)*2;
        t2 -= getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin)*2;
        if(t1<t2 && t1>1){return t1;}
        if(t2<t1 && t2>1){return t2;}
        return Math.min(t1, t2);
    }

    public int getStatusBarHeight() {
        int statusBarHeight = 0;

//        if (hasOnScreenSystemBar())
        {
            int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = this.getResources().getDimensionPixelSize(resourceId);
            }
        }

//        Log.wtf("puzzle", "my puzzle statusBarHeight = " + statusBarHeight );
//        On MDPI devices, the status bar is 25px. We can use this as the base and multiply it by the density (rounded up) to get the status bar height on any device:
//        For reference: ldpi=.75, mdpi=1, hdpi=1.5, xhdpi=2

//        int statusBarHeight = Math.ceil(25 * context.getResources().getDisplayMetrics().density);

        return statusBarHeight;
    }

    private boolean hasOnScreenSystemBar() {
        Display display = this.getWindowManager().getDefaultDisplay();
        int rawDisplayHeight = 0;
        try {
            Method getRawHeight = Display.class.getMethod("getRawHeight");
            rawDisplayHeight = (Integer) getRawHeight.invoke(display);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        Rect r1 = new Rect();
//        DisplayMetrics dm = new DisplayMetrics();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            display.getRealMetrics(dm);
//            rawDisplayHeight = Math.max(rawDisplayHeight, dm.heightPixels);
//        }

        int UIRequestedHeight = display.getHeight();

        return rawDisplayHeight - UIRequestedHeight != 0;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
        if (id == R.id.nav_new_game) {
            GridLayout gridLayout = (GridLayout) this.findViewById(R.id.board);
            game.newGame(this, gridLayout);
            game.setOnGameStateListener(this);
        }
        if (id == R.id.nav_cheat) {
            GridLayout gridLayout = (GridLayout) this.findViewById(R.id.board);
            game.cheat(this, gridLayout);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onGameWon() {
        new DialogGameWon().show(getFragmentManager(), "dlgWon");
    }

    @Override
    public void onGameOver() {
        new DialogGameOver().show(getFragmentManager(), "dlgOver");
    }

    @Override
    public void onCellMarkedByFlag(int marked, int total) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("MINES: " + marked + " / " +total);
    }
}
