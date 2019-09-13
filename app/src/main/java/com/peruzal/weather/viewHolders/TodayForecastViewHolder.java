package com.peruzal.weather.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peruzal.weather.R;


public class TodayForecastViewHolder extends RecyclerView.ViewHolder {
    public TextView currentDate;
    public TextView currentTemperature;
    public TextView summary;
    public TextView unit;

    public TodayForecastViewHolder(@NonNull View itemView) {
        super(itemView);
        this.currentDate = itemView.findViewById(R.id.currentDate);
        this.currentTemperature = itemView.findViewById(R.id.currentTemperature);
        this.unit = itemView.findViewById(R.id.unit);
        this.summary = itemView.findViewById(R.id.summary);
    }
}
