package com.cyllide.app.beta.portfolio;

import android.content.Context;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cyllide.app.beta.AppConstants;
import com.cyllide.app.beta.portfolio.PendingOrdersRV.OrdersAdapter;
import com.cyllide.app.beta.portfolio.PendingOrdersRV.OrdersModel;
import com.cyllide.app.beta.portfolio.PortfolioPositionsRV.BalanceClass;
import com.cyllide.app.beta.portfolio.PortfolioPositionsRV.CurrentPositions;
import com.cyllide.app.beta.portfolio.PortfolioPositionsRV.PositionsAdapter;
import com.cyllide.app.beta.portfolio.PortfolioPositionsRV.PositionsModel;
import com.cyllide.app.beta.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PortfolioPositionsFragment extends Fragment {

    private RecyclerView positionsRV, pendingOrdersRV;
    TextView positionsStatus, pendingOrdersStatus;
    List positionsModel;
    List ordersModel;
    RequestQueue pendingOrderQueue;
    RequestQueue holdingPositionsQueue;
    RequestQueue deleteHoldingPositionsQueue;
    RequestQueue deletePendingOrderQueue;
    Map<String, String> deleteHoldingPositionRequestHeader;
    Map<String, String> deletePendingOrderRequestHeader = new ArrayMap<>();
    Map<String, String> pendingOrderRequestHeader = new ArrayMap<>();
    Map<String, String> holdingPositionRequestHeader = new ArrayMap<>();


    private List<PositionsModel> portfolioPositionsData() {
        List<PositionsModel> data = new ArrayList<>();
        for (int i = 0; i < CurrentPositions.tickerName.size(); i++) {
            if (System.currentTimeMillis() / 1000L - CurrentPositions.tickerEntryTime.get(i) >= 300) {
                data.add(new PositionsModel(CurrentPositions.tickerName.get(i), CurrentPositions.tickerQuantity.get(i), String.valueOf(1234.23), CurrentPositions.tickerPositionType.get(i), CurrentPositions.tickerQuantity.get(i) * 1234.23));
                BalanceClass.balance -= (CurrentPositions.tickerQuantity.get(i) * 1234.23);
            }
        }
        return data;
    }

    private List<OrdersModel> pendingOrdersData() {
        List<OrdersModel> data = new ArrayList<>(12);
        for (int i = 0; i < CurrentPositions.tickerName.size(); i++) {
            if (System.currentTimeMillis() / 1000L - CurrentPositions.tickerEntryTime.get(i) < 300) {
                data.add(new OrdersModel(CurrentPositions.tickerPositionType.get(i), CurrentPositions.tickerQuantity.get(i), CurrentPositions.tickerName.get(i)));
            }
        }
        return data;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.portfolio_positions_fragment, null);
        positionsRV = rootView.findViewById(R.id.positions_rv);
        positionsRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        pendingOrdersRV = rootView.findViewById(R.id.pending_orders_RV);
        pendingOrdersRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        positionsStatus = rootView.findViewById(R.id.available_stocks_positions_status_tv);
        pendingOrdersStatus = rootView.findViewById(R.id.available_stocks_pending_order_status_tv);

        List<PositionsModel> data1 = portfolioPositionsData();


        PositionsAdapter positionsAdapter = new PositionsAdapter(data1);
        positionsRV.setAdapter(positionsAdapter);


        List<OrdersModel> ordersModels = pendingOrdersData();
        OrdersAdapter ordersAdapter = new OrdersAdapter(ordersModels);
        pendingOrdersRV.setAdapter(ordersAdapter);

        getPendingOrders(getContext(), pendingOrdersRV);
        getHoldingPositions(getContext(), positionsRV);

        return rootView;
    }

    private void getPendingOrders(Context context, final RecyclerView pendingOrdersRV) {
        String url = getResources().getString(R.string.apiBaseURL) + "portfolios/positionlist";
        pendingOrderQueue = Volley.newRequestQueue(context);
        pendingOrderRequestHeader.put("token", AppConstants.token);
        pendingOrderRequestHeader.put("portfolioID", AppConstants.portfolioID);
        pendingOrderRequestHeader.put("posType", "Pending");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray responseData = new JSONObject(response).getJSONArray("data");
                    populatePendingOrdersRV(responseData, pendingOrdersRV);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return pendingOrderRequestHeader;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        pendingOrderQueue.add(stringRequest);

    }

    private void deletePendingOrders(Context context, final RecyclerView pendingOrdersRV) {
        String url = getResources().getString(R.string.apiBaseURL) + "portfolios/positionlist";
        deletePendingOrderQueue = Volley.newRequestQueue(context);
        deletePendingOrderRequestHeader.put("token", AppConstants.token);
        deletePendingOrderRequestHeader.put("portfolioID", AppConstants.portfolioID);
        deletePendingOrderRequestHeader.put("posType", "Pending");
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray responseData = new JSONObject(response).getJSONArray("data");
                    populatePendingOrdersRV(responseData, pendingOrdersRV);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return deletePendingOrderRequestHeader;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        deletePendingOrderQueue.add(stringRequest);

    }


    private void getHoldingPositions(Context context, final RecyclerView recyclerView) {
        String url = getResources().getString(R.string.apiBaseURL) + "portfolios/positionlist";
        holdingPositionsQueue = Volley.newRequestQueue(context);
        holdingPositionRequestHeader.put("token", AppConstants.token);
        holdingPositionRequestHeader.put("portfolioID", AppConstants.portfolioID);
        holdingPositionRequestHeader.put("posType", "Holding");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray responseData = new JSONObject(response).getJSONArray("data");
                    populateHoldingPositionsRV(responseData, recyclerView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return holdingPositionRequestHeader;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        holdingPositionsQueue.add(stringRequest);

    }


    private void populateHoldingPositionsRV(JSONArray jsonArray, RecyclerView recyclerView) {
        positionsModel = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                positionsModel.add(new PositionsModel(
                        jsonArray.getJSONObject(i).getString("ticker"),
                        jsonArray.getJSONObject(i).getInt("quantity"),
                        jsonArray.getJSONObject(i).getString("entryPrice"),
                        jsonArray.getJSONObject(i).getBoolean("longPosition"),
                        jsonArray.getJSONObject(i).getDouble("entryPrice") * jsonArray.getJSONObject(i).getInt("quantity")
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (positionsModel.size() == 0) {
            positionsStatus.setVisibility(View.VISIBLE);
            positionsStatus.setText("Nothing to show.");
        } else {
            OrdersAdapter myOrdersAdapter = new OrdersAdapter(positionsModel);
            recyclerView.setAdapter(myOrdersAdapter);
            positionsStatus.setVisibility(View.INVISIBLE);
        }

        PositionsAdapter myPositionAdapter = new PositionsAdapter(positionsModel);
        recyclerView.setAdapter(myPositionAdapter);
    }

    private void populatePendingOrdersRV(JSONArray jsonArray, RecyclerView recyclerView) {
        ordersModel = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                ordersModel.add(
                        new OrdersModel(
                                jsonArray.getJSONObject(i).getBoolean("longPosition"),
                                jsonArray.getJSONObject(i).getInt("quantity"),
                                jsonArray.getJSONObject(i).getString("ticker")
                        )
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (ordersModel.size() == 0) {
            pendingOrdersStatus.setVisibility(View.VISIBLE);
            pendingOrdersStatus.setText("Nothing to show.");
        } else {
            OrdersAdapter myOrdersAdapter = new OrdersAdapter(ordersModel);
            recyclerView.setAdapter(myOrdersAdapter);
            pendingOrdersStatus.setVisibility(View.INVISIBLE);
        }

    }


}

