package com.qm.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.qm.R;
//适配器类
public class SlidePagerAdapter extends PagerAdapter {

    private Context context;
    private int[] slideImages = {R.drawable.poster1_players, R.drawable.poster2_team,R.drawable.poster1_players};

    public SlidePagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return slideImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.slide_item, container, false);

        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(slideImages[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
