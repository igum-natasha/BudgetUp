package com.example.budgetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class StatisticsActivity extends AppCompatActivity {

  private TabLayout tabLayout;
  private ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_statistics);

    BottomNavigationView nav_view = findViewById(R.id.navigationView);

    nav_view.setSelectedItemId(R.id.statistic);
    nav_view.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.home:
                startActivity(new Intent(StatisticsActivity.this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
              case R.id.statistic:
                return true;
              case R.id.account:
                Intent intent = new Intent(StatisticsActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
          }
        });

    tabLayout = findViewById(R.id.tabLayout);
    viewPager = findViewById(R.id.viewPager);

    tabLayout.setupWithViewPager(viewPager);

    VPAdapter vpAdapter =
        new VPAdapter(
            getSupportFragmentManager(),
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    vpAdapter.addFragment(new JournalFragment(), "Journal");
    vpAdapter.addFragment(new StatisticsFragment(), "Statistics");
    viewPager.setAdapter(vpAdapter);
  }
}
