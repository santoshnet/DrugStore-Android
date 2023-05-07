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
import com.frontendsource.drugstore.model.Offer;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Grocery App
 * https://github.com/quintuslabs/GroceryStore
 * Created on 18-Feb-2019.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */
public class PromotionSliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    List<Offer>offerList = new ArrayList<>();


    public PromotionSliderAdapter(Context context) {
        this.context = context;
    }

    public PromotionSliderAdapter(Context context, List<Offer>offerList) {
        this.context = context;
        this.offerList = offerList;
    }

    @Override
    public int getCount() {
        return offerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_promotion_slider, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        Offer offer =  offerList.get(position);
        if(offer.getImage()!=null){
            Picasso.get().load(RestClient.BASE_URL+offer.getImage()).into(imageView);
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