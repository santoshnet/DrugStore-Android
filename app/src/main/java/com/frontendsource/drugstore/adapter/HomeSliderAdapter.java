package com.frontendsource.drugstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.frontendsource.drugstore.R;
import com.frontendsource.drugstore.api.clients.RestClient;
import com.frontendsource.drugstore.model.Banner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Grocery App
 * https://github.com/quintuslabs/GroceryStore
 * Created on 18-Feb-2019.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */
public class HomeSliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Banner>bannerList = new ArrayList<>();

    public HomeSliderAdapter(Context context) {
        this.context = context;
    }

    public HomeSliderAdapter(Context context, List<Banner>bannerList) {
        this.context = context;
        this.bannerList = bannerList;
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_home_slider, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        if(bannerList.get(position).getImage()!=null){
            Picasso.get().load(RestClient.BASE_URL+bannerList.get(position).getImage()).into(imageView);
        }


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}