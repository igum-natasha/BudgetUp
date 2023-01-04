package com.example.budgetup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    int images[] = {
            R.drawable.onboarding1,
            R.drawable.onboarding2,
            R.drawable.onboarding3
    };
    int titles[] = {
            R.string.title_onboarding1,
            R.string.title_onboarding2,
            R.string.title_onboarding3
    };

    int desc[] = {
            R.string.desc_onboarding1,
            R.string.desc_onboarding2,
            R.string.desc_onboarding3
    };

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView sliderImage = (ImageView) view.findViewById(R.id.imageTitle);
        TextView sliderTitle = (TextView) view.findViewById(R.id.textTitle);
        TextView sliderDesc = (TextView) view.findViewById(R.id.textDescription);

        sliderImage.setImageResource(images[position]);
        sliderTitle.setText(titles[position]);
        sliderDesc.setText(desc[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
