package com.example.kartikbhardwaj.bottom_navigation.Portfolio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kartikbhardwaj.bottom_navigation.Portfolio.PendingOrdersRV.OrdersAdapter;
import com.example.kartikbhardwaj.bottom_navigation.Portfolio.PendingOrdersRV.OrdersModel;
import com.example.kartikbhardwaj.bottom_navigation.R;

import java.util.ArrayList;
import java.util.List;


public class PendingOrdersFragment extends Fragment {
    RecyclerView RV;

    private   String OrderType[]={"fhgas"};
    private   String PositionType[]={"akjfgd"};
    private   String Quantity[]={"ksjgfh"};
    private   String ExecutionPrice[]={"kejghrw"};

    private String StockTicker[]={"wkgel"};
    private String CurrentStockPrice[]={"skejgoihtw"};

    private List<OrdersModel> dummyData() {
        List<OrdersModel> data = new ArrayList<>(12);
        for (int i = 0; i < 1; i++) {
            data.add(new OrdersModel(OrderType[i], PositionType[i],Quantity[i],ExecutionPrice[i],StockTicker[i],CurrentStockPrice[i]));
        }//data is the list of objects to be set in the list item
        return data;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pending_orders_fragment,container,false);
        RV=(RecyclerView)rootView.findViewById(R.id.pending_Orders_RV);
        RV.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<OrdersModel> data1 =dummyData();
        OrdersAdapter ordersAdapter= new OrdersAdapter(data1);
        RV.setAdapter(ordersAdapter);

        return rootView;




    }



}




