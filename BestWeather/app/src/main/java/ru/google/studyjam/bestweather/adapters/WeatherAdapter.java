package ru.google.studyjam.bestweather.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.google.studyjam.bestweather.Config;
import ru.google.studyjam.bestweather.R;
import ru.google.studyjam.bestweather.activity.WeatherDetailsInfoActivity;
import ru.google.studyjam.bestweather.models.CityInfo;
import ru.google.studyjam.bestweather.models.DayWeather;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    private List<CityInfo> citiesInfo;
    private Context context;

    public WeatherAdapter(Context context) {
        this.context = context;
        citiesInfo = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CityInfo cityInfo = citiesInfo.get(position);
        holder.cityName.setText(cityInfo.getName());
        holder.temperature.setText(context.getString(R.string.weather_temperature,
                cityInfo.getMain().getTemp()));
        holder.date.setText(dateFormat.format(new Date(cityInfo.getDate() * 1000)));

        DayWeather dayWeather = cityInfo.getWeather().get(0);
        if (dayWeather != null) {
            Picasso.with(context)
                    .load(Config.IMAGE_ENDPOINT + dayWeather.getIcon() + ".png")
                    .into(holder.weatherImage);
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, WeatherDetailsInfoActivity.class);
            intent.putExtra(WeatherDetailsInfoActivity.CITY_ID, cityInfo.getCityId());
            context.startActivity(intent);
        });
    }

    public void setData(List<CityInfo> citiesInfo) {
        this.citiesInfo = citiesInfo;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return citiesInfo.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.city_name) TextView cityName;
        @BindView(R.id.date) TextView date;
        @BindView(R.id.temperature) TextView temperature;
        @BindView(R.id.weather_image) ImageView weatherImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
