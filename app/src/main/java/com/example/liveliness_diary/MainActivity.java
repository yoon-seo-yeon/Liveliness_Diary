package com.example.liveliness_diary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private DiaryFragment diaryFragment = new DiaryFragment();
    private StatsFragment statsFragment = new StatsFragment();
    private SettingFragment settingFragment = new SettingFragment();

    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

//        Toast.makeText(this, "userId : " + userId, Toast.LENGTH_SHORT).show();
        Log.d("TAG", "PICkkkkkk userId : " + userId);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        homeFragment.setArguments(bundle);

//        Log.d("TAG", "PICK Main userId : " + userId);

        transaction.replace(R.id.frame, homeFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            Bundle bundle;

            switch (menuItem.getItemId()) {
                case R.id.stats:
                    bundle = new Bundle();
                    bundle.putString("userId", userId);
                    statsFragment.setArguments(bundle);

                    transaction.replace(R.id.frame, statsFragment).commitAllowingStateLoss();
                    break;

                case R.id.home:
                    bundle = new Bundle();
                    bundle.putString("userId", userId);
                    homeFragment.setArguments(bundle);

                    transaction.replace(R.id.frame, homeFragment).commitAllowingStateLoss();

                    break;

                case R.id.diary:
                    bundle = new Bundle();
                    bundle.putString("userId", userId);
                    diaryFragment.setArguments(bundle);

                    transaction.replace(R.id.frame, diaryFragment).commitAllowingStateLoss();
                    break;

                case R.id.user:
                    bundle = new Bundle();
                    bundle.putString("userId", userId);
                    settingFragment.setArguments(bundle);

                    transaction.replace(R.id.frame, settingFragment).commitAllowingStateLoss();
                    break;
            }

            return true;
        }
    }
}