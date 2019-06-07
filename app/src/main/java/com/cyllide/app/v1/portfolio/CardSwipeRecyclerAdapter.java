package com.cyllide.app.v1.portfolio;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cyllide.app.v1.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class CardSwipeRecyclerAdapter extends
        RecyclerView.Adapter<CardSwipeRecyclerAdapter.ViewHolder> {
    private List<String> data;
    Context context;

    public CardSwipeRecyclerAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.game_card_nifty50, parent, false);
        ViewHolder itemView = new ViewHolder(contactView);

        return itemView;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ArrayList<Entry> yAxisValues = new ArrayList<>();
        for(int i=0;i<100;i++){
            yAxisValues.add(new Entry((float)i,(float)(2*i+1)));
        }


        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        LineDataSet lineDataSet = new LineDataSet(yAxisValues,"Test");
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorPrimary));

        lineDataSets.add(lineDataSet);

        holder.lineChart.setData(new LineData(lineDataSets));
        holder.lineChart.getXAxis().setDrawLabels(false);
        holder.lineChart.getAxisLeft().setDrawGridLines(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillDrawable(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.chart_gradient));
        holder.lineChart.getLegend().setEnabled(false);
        Description d = new Description();
        d.setText("");
        holder.lineChart.setDescription(d);
        holder. lineChart.invalidate();
        holder.infoButton.setText(data.get(position));
        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),data.get(position),Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Layer type: ", Integer.toString(v.getLayerType()));
                Log.i("Hardware Accel type:", Integer.toString(View.LAYER_TYPE_HARDWARE));
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LineChart lineChart;
        private MaterialButton infoButton;
        public ViewHolder(View itemView) {
            super(itemView);
            lineChart = itemView.findViewById(R.id.portfolio_game_home_chart);
            infoButton = itemView.findViewById(R.id.game_card_info_button);



        }

    }
}