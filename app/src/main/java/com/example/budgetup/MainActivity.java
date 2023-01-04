package com.example.budgetup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ViewPager sliderViewPager;
    LinearLayout dotLayout;
    Button btnSkip, btnNext;

    TextView[] dots;

    ViewPagerAdapter viewPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSkip = findViewById(R.id.btnSkip);
        btnNext = findViewById(R.id.btnNext);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItem(0) < 2) {
                    sliderViewPager.setCurrentItem(getItem(1), true);
                } else {
                    Intent i = new Intent(MainActivity.this, SplashActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        sliderViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        dotLayout = (LinearLayout) findViewById(R.id.indicatorLayout);

        viewPagerAdapter = new ViewPagerAdapter(this);

        sliderViewPager.setAdapter(viewPagerAdapter);

        setUpIndicator(0);
        sliderViewPager.addOnPageChangeListener(viewListener);

    }

    public void setUpIndicator(int position) {
        dots = new TextView[3];
        dotLayout.removeAllViews();


        for (int i=0; i< dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.base_200));
            dotLayout.addView(dots[i]);
        }

        dots[position].setTextColor(getResources().getColor(R.color.primary_400));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setUpIndicator(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getItem(int i) {
        return sliderViewPager.getCurrentItem() + i;
    }
}