package com.peruzal.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int WEEKLY_FORECAST_ITEM_VIEW_TYPE = 2;
    private static final int TODAY_FORECAST_VIEW_TYPE = 0;
    private Context context;
    private static final DecimalFormat decimalFormat = new DecimalFormat("#");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E HH:MM", Locale.getDefault());
    private static SimpleDateFormat weekDayFormat = new SimpleDateFormat("E", Locale.getDefault());
    private TodayForecast todayForecast;
    private OnItemClickListener onItemClickListener;
    private List<Forecast> weeklyForecast = new ArrayList<>();

    public ForecastAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
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
        Forecast forecast = weeklyForecast.get(position);
        if (forecast != null) {
            holder.temperatureHigh.setText(context.getString(R.string.temperature, decimalFormat.format(forecast.apparentTemperatureHigh)));
            holder.temperatureLow.setText(context.getString(R.string.temperature, decimalFormat.format(forecast.apparentTemperatureLow)));
            Date date = new Date(forecast.time * 1000L);
            holder.dayOfWeek.setText(weekDayFormat.format(date));
            int forecastResource = IconUtils.getForecastIcon(forecast.icon);
            holder.icon.setAnimation(forecastResource);
        }
    }

    private void setUpTodayForecastView(@NonNull TodayForecastViewHolder holder) {
        if (todayForecast != null) {
            holder.currentTemperature.setText(context.getString(R.string.temperature, decimalFormat.format(this.todayForecast.temperature)));
            holder.currentDate.setText(simpleDateFormat.format(new Date()));
            holder.summary.setText(this.todayForecast.summary);
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
        return weeklyForecast.size();
    }

    public Forecast getForecastItem(int paramInt) {
        return weeklyForecast.get(paramInt);
    }

    public interface OnItemClickListener {
        void OnItemClick(int position, View view);
    }
}
