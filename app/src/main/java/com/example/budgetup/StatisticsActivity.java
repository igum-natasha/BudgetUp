package com.example.budgetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

  private TabLayout tabLayout;
  private ViewPager viewPager;
  static BottomNavigationView nav_view;
  private Dialog infoDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_statistics);

    nav_view = findViewById(R.id.navigationView);

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

    defineAddDialog();
    AppDatabase db = AppDatabase.build(getApplicationContext());
    List<Expense> expensesAll = db.expenseDao().getAll();
    if (expensesAll.isEmpty()) {
      infoDialog.show();
    }
  }
  @SuppressLint("UseCompatLoadingForDrawables")
  private void defineAddDialog() {
    infoDialog = new Dialog(StatisticsActivity.this);
    infoDialog.setContentView(R.layout.no_info_dialog);
    infoDialog.getWindow().setGravity(Gravity.BOTTOM);
    infoDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    infoDialog
            .getWindow()
            .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    infoDialog.setCancelable(false);
      Button track = infoDialog.findViewById(R.id.btnTrack);
    track.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(StatisticsActivity.this, NewExpenseActivity.class);
            startActivity(intent);
        }
    });
  }
  public static BottomNavigationView getNavigationview() {
    return nav_view;
  }
}
