package com.sky.CatBreeds;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;


import com.sky.CatBreeds.adapter.MainFragmentPagerAdapter;
import com.sky.CatBreeds.fragment.HomepageFragment;
import com.sky.CatBreeds.fragment.KeepFragment;

public class HomeActivity extends AppCompatActivity {

  //  @BindView(R.id.toolbar)
    //android.support.v7.widget.Toolbar toolbar;
   // @BindView(R.id.tablayout)
    TabLayout tabLayout;
   // @BindView(R.id.main_viewpager)
    ViewPager viewPager;

    protected static final int USERINFOACTIVITY_CODE = 0;
    protected static final int LOGINACTIVITY_CODE = 1;

    // Tab
    private FragmentManager mFragmentManager;
    private MainFragmentPagerAdapter mainFragmentPagerAdapter;
    private String  myUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        myUser=intent.getStringExtra("KEY");

        setContentView(R.layout.activity_home);


        initEventAndData();
    }


    protected void initEventAndData() {
        viewPager=(ViewPager)findViewById(R.id.main_viewpager);
        tabLayout=(TabLayout) findViewById(R.id.tablayout);
      //  toolbar=(Toolbar) findViewById(R.id.toolbar);

        //初始化ViewPager
        mFragmentManager = getSupportFragmentManager();
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(mFragmentManager);
        mainFragmentPagerAdapter.addFragment(new HomepageFragment(), "cats");
        mainFragmentPagerAdapter.addFragment(new KeepFragment(), " favourites,");
        viewPager.setAdapter(mainFragmentPagerAdapter);
        //初始化TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("cats"));
        tabLayout.addTab(tabLayout.newTab().setText("favourites"));
        tabLayout.setupWithViewPager(viewPager);

    }

    /**
     * 监听Drawer
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




}
