package com.peruzal.weather.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.peruzal.weather.R;
import com.peruzal.weather.adapters.ForecastAdapter;


public class WeeklyForecastViewHolder extends RecyclerView.ViewHolder {
    public TextView dayOfWeek;
    public LottieAnimationView icon;
    public TextView temperatureHigh;
    public TextView temperatureLow;

    public WeeklyForecastViewHolder(@NonNull View itemView, ForecastAdapter.OnItemClickListener itemClickListener) {
        super(itemView);
        itemView.setOnClickListener(view -> itemClickListener.OnItemClick(getAdapterPosition(), view));
        this.dayOfWeek = itemView.findViewById(R.id.dayOfWeek);
        this.temperatureHigh = itemView.findViewById(R.id.temperatureHigh);
        this.temperatureLow = itemView.findViewById(R.id.temperatureLow);
        this.icon = itemView.findViewById(R.id.icon);
    }
}
