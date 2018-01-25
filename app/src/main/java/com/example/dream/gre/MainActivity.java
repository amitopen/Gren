package com.example.dream.gre;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private static final String BACK_STACK_ROOT_TAG = "root_home_fragment";
    private static final String SELECTED_ITEM = "selected_item";
    private Toolbar toolbar;
    private MenuItem menuItemSelected;
    private int mMenuItemSelected;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.content,new  HomeFragment() ).commit();
                    getSupportActionBar().setTitle("Gre");
                    getSupportActionBar().show();
                    return true;
                case R.id.navigation_study:
                    transaction.replace(R.id.content,new StudyFragment()).addToBackStack(BACK_STACK_ROOT_TAG).commit();
                    getSupportActionBar().setTitle("Study");
                    getSupportActionBar().show();
                    return true;
                case R.id.navigation_profile:
                    getSupportActionBar().setTitle("Profile");
                    getSupportActionBar().show();

                    transaction.replace(R.id.content,new LoginFragment()).addToBackStack(BACK_STACK_ROOT_TAG).commit();
                    return true;
                case R.id.navigation_more:
                    transaction.replace(R.id.content,new MoreFragment()).addToBackStack(BACK_STACK_ROOT_TAG).commit();
                    getSupportActionBar().hide();
                    return true;
            }
            return false;
        }

    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content,new HomeFragment()).commit();

        //Always load first fragment as default
        navigationView.setSelectedItemId(R.id.navigation_home);

        if (savedInstanceState != null) {
            mMenuItemSelected = savedInstanceState.getInt(SELECTED_ITEM, 0);
            menuItemSelected = navigationView.getMenu().findItem(mMenuItemSelected);
        } else {
            menuItemSelected = navigationView.getMenu().getItem(0);
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            onCreate(savedInstanceState);
        }
    }

    public void onBackPressed() {
        Toast.makeText(this, "Back19", Toast.LENGTH_SHORT).show();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStackImmediate();
        }
        else if (R.id.navigation_home != seletedItemId) {
            setHomeItem(MainActivity.this);
        }
        else
            super.onBackPressed();
    }
    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                activity.findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        Toast.makeText(this, "finish", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
