package com.example.anambhatia.likemeister;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

//Activity to create navigation bar and fragments.
public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String access;
    Bundle bundle;
    TextView f1;
    Fragment a;
    private final ArrayList<View> mMenuItems = new ArrayList<>(3);
    Button remove;
    ImageView iv;
    DrawerLayout drawer;
    //Button cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
         f1=((TextView) header.findViewById(R.id.tvf));

        //changing fonts using font files under assets.
        final Typeface typeFace1=Typeface.createFromAsset(getAssets(),"YanoneKaffeesatz-Regular.otf");
        f1.setTypeface(typeFace1);
        Intent intent = getIntent();
         access=intent.getStringExtra("number");
        bundle = new Bundle();
        bundle.putString("token",access);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView yourTextView = null;

        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            yourTextView = (TextView) f.get(toolbar);
            yourTextView.setTypeface(typeFace1);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }


        remove=((Button)findViewById(R.id.button5));

        iv=((ImageView) findViewById(R.id.imageView4));
        remove.setTypeface(typeFace1);
       drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        final Menu navMenu = navigationView.getMenu();
        navigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remember to remove the installed OnGlobalLayoutListener
                navigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // Loop through and find each MenuItem View
                for (int i = 0, length = 3; i < length; i++) {
                    final String id = "menuItem" + (i + 1);
                    final MenuItem item = navMenu.findItem(getResources().getIdentifier(id, "id", getPackageName()));
                    navigationView.findViewsWithText(mMenuItems, item.getTitle(), View.FIND_VIEWS_WITH_TEXT);
                }
                // Loop through each MenuItem View and apply your custom Typeface
                for (final View menuItem : mMenuItems) {
                    ((TextView) menuItem).setTypeface(typeFace1);
                }

                final MenuItem item = navMenu.findItem(getResources().getIdentifier("nav_logout", "id", getPackageName()));
                navigationView.findViewsWithText(mMenuItems, item.getTitle(), View.FIND_VIEWS_WITH_TEXT);
                for (final View menuItem : mMenuItems) {
                    ((TextView) menuItem).setTypeface(typeFace1);
                }


            }
        });


    }


    //pressing back button leads to drawer opening and closing
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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
            LoginManager.getInstance().logOut();
            Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //3 different menu bar items
        if (id == R.id.menuItem1) {

            setTitle("Predictor");

            Predictor fragment = new Predictor();
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment,fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.menuItem2) {


            setTitle("Facebook Stats");
            Stats fragment = new Stats();
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment,fragment);
            fragmentTransaction.commit();




        } else if (id == R.id.menuItem3) {

            setTitle("Friends Stats");
            Friend fragment = new Friend();
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment,fragment);
            fragmentTransaction.commit();

        }

        else if (id==R.id.nav_logout)
        {
            System.out.println(LoginManager.getInstance());
            LoginManager.getInstance().logOut();
            Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void rem(View v)
    {
        //to remove the intro image using an animation
        remove.setClickable(false);
        TranslateAnimation animate = new TranslateAnimation(0,0,0,-iv.getHeight()-iv.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        iv.startAnimation(animate);
        iv.setVisibility(View.GONE);
        remove.startAnimation(animate);
        remove.setVisibility(View.GONE);


    }
}
