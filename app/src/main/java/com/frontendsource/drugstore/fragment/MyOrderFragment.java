package com.frontendsource.drugstore.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frontendsource.drugstore.model.Token;
import com.google.gson.Gson;
import com.frontendsource.drugstore.R;
import com.frontendsource.drugstore.adapter.OrderAdapter;
import com.frontendsource.drugstore.api.clients.RestClient;
import com.frontendsource.drugstore.model.Order;
import com.frontendsource.drugstore.model.OrdersResult;
import com.frontendsource.drugstore.model.User;
import com.frontendsource.drugstore.util.localstorage.LocalStorage;

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
public class MyOrderFragment extends Fragment {
    private static final String TAG = "MyOrderFragment==>";
    LocalStorage localStorage;
    LinearLayout linearLayout;
    Gson gson = new Gson();
    Order order;
    private List<Order> orderList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OrderAdapter mAdapter;
    private List<Order> newOrderList = new ArrayList<>();

    public MyOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);

        recyclerView = view.findViewById(R.id.order_rv);
        linearLayout = view.findViewById(R.id.no_order_ll);
        localStorage = new LocalStorage(getContext());

        User user = gson.fromJson(localStorage.getUserLogin(), User.class);
        Token token = new Token(user.getToken());
        fetchOrderDetails(token);

        return view;
    }

    private void fetchOrderDetails(Token token) {

        Call<OrdersResult> call = RestClient.getRestService(getContext()).orderDetails(token);
        call.enqueue(new Callback<OrdersResult>() {
            @Override
            public void onResponse(Call<OrdersResult> call, Response<OrdersResult> response) {
                Log.d("Response :=>", response.body() + "");
                if (response != null) {

                    OrdersResult ordersResult = response.body();
                    if (ordersResult.getCode() == 200) {

                        orderList = ordersResult.getOrderList();
                        setupOrderRecycleView();

                    }

                }
            }

            @Override
            public void onFailure(Call<OrdersResult> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

    }

    private void setupOrderRecycleView() {
        if (orderList.isEmpty()) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        mAdapter = new OrderAdapter(orderList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("MyOrder");
    }
}
