package com.peruzal.weather.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.peruzal.weather.Constants;
import com.peruzal.weather.R;
import com.peruzal.weather.helpers.IconUtils;
import com.peruzal.weather.models.Forecast;
import com.peruzal.weather.models.TodayForecast;
import com.peruzal.weather.viewHolders.TodayForecastViewHolder;
import com.peruzal.weather.viewHolders.WeeklyForecastViewHolder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int WEEKLY_FORECAST_ITEM_VIEW_TYPE = 2;
    private static final int TODAY_FORECAST_VIEW_TYPE = 0;
    static final DecimalFormat decimalFormat = new DecimalFormat("#");
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E HH:MM");
    static SimpleDateFormat weekDayFormat = new SimpleDateFormat("E");
    TodayForecast todayForecast;
    OnItemClickListener onItemClickListener;
    List<Forecast> weeklyForecast = new ArrayList();

    public ForecastAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case TODAY_FORECAST_VIEW_TYPE:
                setUpTodayForecastView((TodayForecastViewHolder) holder);
                break;
            case WEEKLY_FORECAST_ITEM_VIEW_TYPE:
                setUpWeeklyForecastView((WeeklyForecastViewHolder) holder, position);
                break;
        }
    }

    private void setUpWeeklyForecastView(@NonNull WeeklyForecastViewHolder holder, int position) {
        WeeklyForecastViewHolder dailyForecastViewHolder = holder;
        Forecast forecast = weeklyForecast.get(position);
        if (forecast != null) {
            dailyForecastViewHolder.temperatureHigh.setText(decimalFormat.format(forecast.apparentTemperatureHigh) + Constants.DEGREES_SYMBOL);
            dailyForecastViewHolder.temperatureLow.setText(decimalFormat.format(forecast.apparentTemperatureLow) + Constants.DEGREES_SYMBOL);
            Date date = new Date(forecast.time * 1000L);
            dailyForecastViewHolder.dayOfWeek.setText(weekDayFormat.format(date));
            int forecastIcon = IconUtils.getForecastIcon(forecast.icon);
            dailyForecastViewHolder.icon.setImageResource(forecastIcon);
        }
    }

    private void setUpTodayForecastView(@NonNull TodayForecastViewHolder holder) {
        TodayForecastViewHolder currentlyViewHolder = holder;
        if (todayForecast != null) {
            currentlyViewHolder.currentTemperature.setText(decimalFormat.format(this.todayForecast.temperature) + Constants.DEGREES_SYMBOL);
            currentlyViewHolder.currentDate.setText(simpleDateFormat.format(new Date()));
            currentlyViewHolder.summary.setText(this.todayForecast.summary);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        switch (getItemViewType(viewType)){
            case TODAY_FORECAST_VIEW_TYPE:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast_header, parent, false);
                return new TodayForecastViewHolder(itemView);
            case WEEKLY_FORECAST_ITEM_VIEW_TYPE:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weekly_forecast, parent, false);
                return new WeeklyForecastViewHolder(itemView, onItemClickListener);
                default:
                    throw new IllegalStateException();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TODAY_FORECAST_VIEW_TYPE;
        }
        return WEEKLY_FORECAST_ITEM_VIEW_TYPE;
    }

    public void setData(TodayForecast todayForecast, List<Forecast> weeklyForecast) {
        this.todayForecast = todayForecast;
        this.weeklyForecast = weeklyForecast;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return weeklyForecast.size() == 0 ? 0 : weeklyForecast.size();
    }

    public Forecast getForecastItem(int paramInt) {
        return weeklyForecast.get(paramInt);
    }

    public interface OnItemClickListener {
        void OnItemClick(int position, View view);
    }
}
