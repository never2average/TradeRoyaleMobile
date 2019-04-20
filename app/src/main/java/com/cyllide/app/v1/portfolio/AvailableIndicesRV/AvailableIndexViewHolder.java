package com.cyllide.app.v1.portfolio.AvailableIndicesRV;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cyllide.app.v1.AppConstants;
import com.cyllide.app.v1.R;
import com.cyllide.app.v1.charts.ChartActivityIndex;

import android.content.Intent;
import android.graphics.Color;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class AvailableIndexViewHolder extends RecyclerView.ViewHolder {

    TextView indexName,indexValNet;
    ExpandableLayout expandableLayout;
    LinearLayout findexCard;
    ImageView analyzeIndices;

    public AvailableIndexViewHolder(@NonNull View itemView) {
        super(itemView);
        indexName=itemView.findViewById(R.id.indexName);
        indexValNet=itemView.findViewById(R.id.indexValue);
        expandableLayout = itemView.findViewById(R.id.indexExtraOptions);
        findexCard = itemView.findViewById(R.id.findexcard);
        analyzeIndices = itemView.findViewById(R.id.analyzeIndices);
    }

    public void populate(final AvailableIndexModel stocksModel){

        indexName.setText(stocksModel.getIndexName());
        setIndexValue(indexValNet);

        findexCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandableLayout.isExpanded()){
                    expandableLayout.setExpanded(false);
                }
                else{
                    expandableLayout.setExpanded(true);
                }
            }
        });

        analyzeIndices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChartActivityIndex.class);
                intent.putExtra("ticker",stocksModel.getIndexName());
                view.getContext().startActivity(intent);
            }
        });

    }
    RequestQueue indexRequestQueue;
    Map<String,String> requestHeaders = new ArrayMap<String,String>();
    private void setIndexValue(final TextView indexValNet) {
        indexRequestQueue = Volley.newRequestQueue(indexValNet.getContext());
        String requestEndpoint = indexValNet.getContext().getResources().getString(R.string.dataApiBaseURL)+ "stock/close";
        requestHeaders.put("value","1231D123");
        requestHeaders.put("ticker","123"+indexName.getText()+"123");
        requestHeaders.put("singleVal","True");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    DecimalFormat df = new DecimalFormat("####0.00");
                    Log.d("IndexViewHolder",response);
                    JSONObject responseObject = new JSONObject(response);
                    double indexChanges = responseObject.getDouble("movement");
                    double indexValue = responseObject.getDouble("data");
                    Date date = new Date(responseObject.getLong("timestamp"));
                    DateFormat dateformatter = new SimpleDateFormat("HH");
                    int hour = Integer.parseInt(dateformatter.format(date));
                    if(hour<16 && hour>=9) {
                        if (indexChanges >= 0) {
                            indexValNet.setTextColor(Color.parseColor("#00ff00"));
                            indexValNet.setText(df.format(indexValue) + "(+" + String.valueOf(df.format(indexChanges)) + "%)" + "▲");
                        } else {
                            indexValNet.setTextColor(Color.parseColor("#ff0000"));
                            indexValNet.setText(df.format(indexValue) + "(" + String.valueOf(df.format(indexChanges)) + "%)" + "▼");

                        }
                    }
                    else{
                        indexValNet.setTextColor(ContextCompat.getColor(indexValNet.getContext(), R.color.dark_gray));
                        indexValNet.setText(df.format(indexValue) + "(+" + String.valueOf(df.format(indexChanges)) + "%)" + "▲");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Question Error", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return requestHeaders;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("whats failing", String.valueOf(mStatusCode));
                return super.parseNetworkResponse(response);
            }
        };
        indexRequestQueue.add(stringRequest);
    }
}
