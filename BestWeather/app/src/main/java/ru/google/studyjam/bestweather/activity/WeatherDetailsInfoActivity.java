package ru.google.studyjam.bestweather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.google.studyjam.bestweather.Config;
import ru.google.studyjam.bestweather.R;
import ru.google.studyjam.bestweather.models.CityInfo;
import ru.google.studyjam.bestweather.models.DayWeather;

public class WeatherDetailsInfoActivity extends BaseActivity {

    public static final String CITY_ID = "city_id";

    @BindView(R.id.date) TextView dateTextView;
    @BindView(R.id.average_temperature) TextView averageTemperatureTextView;
    @BindView(R.id.max_temperature) TextView maxTemperatureTextView;
    @BindView(R.id.min_temperature) TextView minTemperatureTextView;
    @BindView(R.id.pressure) TextView pressureTextView;
    @BindView(R.id.wind_speed) TextView windSpeedTextView;
    @BindView(R.id.weather_image) ImageView weatherImage;

    private CityInfo cityInfo;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details_info);
        ButterKnife.bind(WeatherDetailsInfoActivity.this);
        showProgressBar();
        readCityInfo();
        feelCityWeatherInfo();
    }

    private void readCityInfo() {
        long cityId = getIntent().getExtras().getLong(CITY_ID);
        cityInfo =
                realm
                        .where(CityInfo.class)
                        .equalTo("cityId", cityId)
                        .findAll()
                        .first();
    }

    private void feelCityWeatherInfo() {
        setupToolbar(cityInfo.getName(), true);
        DayWeather dayWeather = cityInfo.getWeather().get(0);
        if (cityInfo.getWeather() != null) {
            Picasso.with(WeatherDetailsInfoActivity.this)
                    .load(Config.IMAGE_ENDPOINT + dayWeather.getIcon() + ".png")
                    .into(weatherImage);

        }
        dateTextView.setText(dateFormat.format(new Date(cityInfo.getDate() * 1000)));
        averageTemperatureTextView.setText(getString(R.string.weather_details_temperature_average,
                cityInfo.getMain().getTemp()));
        maxTemperatureTextView.setText(getString(R.string.weather_details_temperature_max,
                cityInfo.getMain().getMax()));
        minTemperatureTextView.setText(getString(R.string.weather_details_temperature_min,
                cityInfo.getMain().getMin()));

        pressureTextView.setText(getString(R.string.weather_details_pressure,
                cityInfo.getMain().getPressure()));

        windSpeedTextView.setText(getString(R.string.weather_details_wind_speed,
                cityInfo.getWind().getSpeed()));
        hideProgressBar();
    }
}
