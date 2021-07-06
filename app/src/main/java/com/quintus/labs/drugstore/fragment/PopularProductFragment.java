package com.quintus.labs.drugstore.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.quintus.labs.drugstore.R;
import com.quintus.labs.drugstore.adapter.ProductAdapter;
import com.quintus.labs.drugstore.api.clients.RestClient;
import com.quintus.labs.drugstore.helper.Data;
import com.quintus.labs.drugstore.model.Product;
import com.quintus.labs.drugstore.model.ProductResult;
import com.quintus.labs.drugstore.model.Token;
import com.quintus.labs.drugstore.model.User;
import com.quintus.labs.drugstore.util.localstorage.LocalStorage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Drug App
 * https://github.com/quintuslabs/GroceryStore
 * Created on 18-Feb-2019.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularProductFragment extends Fragment {
    RecyclerView pRecyclerView;
    Data data;
    View progress;
    LocalStorage localStorage;
    Gson gson = new Gson();
    User user;
    Token token;
    List<Product> productList = new ArrayList<>();
    private ProductAdapter pAdapter;


    public PopularProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular, container, false);

        pRecyclerView = view.findViewById(R.id.popular_product_rv);

        progress = view.findViewById(R.id.progress_bar);

        localStorage = new LocalStorage(getContext());
        user = gson.fromJson(localStorage.getUserLogin(), User.class);
        token = new Token(user.getToken());
        getPopularProduct();


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Popular Product");
    }

    private void getPopularProduct() {
        showProgressDialog();
        Call<ProductResult> call = RestClient.getRestService(getContext()).popularProducts(token);
        call.enqueue(new Callback<ProductResult>() {
            @Override
            public void onResponse(Call<ProductResult> call, Response<ProductResult> response) {
                Log.d("Response :=>", response.body() + "");
                if (response != null) {

                    ProductResult productResult = response.body();
                    if (productResult.getCode() == 200) {

                        productList = productResult.getProductList();
                        setupPopularProductRecycleView();

                    }

                }

                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ProductResult> call, Throwable t) {
                hideProgressDialog();
            }
        });
    }

    private void setupPopularProductRecycleView() {

        pAdapter = new ProductAdapter(productList, getContext(), "pop");
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getContext());
        pRecyclerView.setLayoutManager(pLayoutManager);
        pRecyclerView.setItemAnimator(new DefaultItemAnimator());
        pRecyclerView.setAdapter(pAdapter);

    }

    private void hideProgressDialog() {
        progress.setVisibility(View.GONE);
    }

    private void showProgressDialog() {
        progress.setVisibility(View.VISIBLE);
    }
}
