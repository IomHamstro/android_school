package ru.google.studyjam.bestweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.google.studyjam.bestweather.Config;
import ru.google.studyjam.bestweather.R;
import ru.google.studyjam.bestweather.adapters.WeatherAdapter;
import ru.google.studyjam.bestweather.models.CityInfo;
import ru.google.studyjam.bestweather.models.CityWeatherInfo;
import ru.google.studyjam.bestweather.network.SessionRestManager;
import ru.google.studyjam.bestweather.utils.Constants;

public class MainActivity extends BaseActivity {

    public static final int ADD_NEW_CITY = 999;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_to_refresh) SwipeRefreshLayout swipeToRefresh;

    @OnClick(R.id.add_city)
    public void onFloatingActionButtonClick() {
        openAddingNewCityActivity();
    }

    private WeatherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        setupToolbar(R.string.app_name, false);
        setupRecyclerView(recyclerView);
        setupSwipeToRefresh();
        load();
    }

    private void setupSwipeToRefresh() {
        swipeToRefresh.setOnRefreshListener(this::load);
    }

    @Override
    protected void showProgressBar() {
        if (!swipeToRefresh.isRefreshing()) {
            super.showProgressBar();
            swipeToRefresh.setEnabled(false);
        }
    }

    @Override
    protected void hideProgressBar() {
        if (swipeToRefresh.isRefreshing()) {
            swipeToRefresh.setRefreshing(false);
        } else {
            super.hideProgressBar();
            swipeToRefresh.setEnabled(true);
        }
    }

    protected void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter = new WeatherAdapter(MainActivity.this));
    }

    private void load() {
        showProgressBar();
        Call<CityWeatherInfo> call
                = SessionRestManager.getInstance().getRest()
                .getCurrentByCityIds(getUserCitiesList(),
                        "metric",
                        Config.APPLICATION_ID);
        call.enqueue(new Callback<CityWeatherInfo>() {
            @Override
            public void onResponse(Call<CityWeatherInfo> call, Response<CityWeatherInfo> response) {
                if (response.isSuccessful()) {
                    List<CityInfo> citiesInfo = response.body().getList();
                    adapter.setData(citiesInfo);
                    saveCitiesInfoToDatabase(citiesInfo);
                    hideProgressBar();
                    return;
                }
                Toast.makeText(MainActivity.this, "Произошла ошибка", Toast.LENGTH_LONG).show();
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<CityWeatherInfo> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Произошла ошибка соединения с интернетом",
                        Toast.LENGTH_LONG).show();
                RealmResults<CityInfo> results = realm.where(CityInfo.class).findAll();
                adapter.setData(Collections.unmodifiableList(results));
                hideProgressBar();
            }
        });
    }

    private String getUserCitiesList() {
        RealmResults<CityInfo> userCities = realm.where(CityInfo.class).findAll();
        if (userCities.size() == 0) return Constants.CITIES;
        StringBuilder builder = new StringBuilder();
        for (CityInfo cityInfo : userCities) {
            builder.append(cityInfo.getCityId());
            builder.append(',');
        }
        return builder.toString();
    }

    public void openAddingNewCityActivity() {
        Intent intent = new Intent(MainActivity.this, NewCityActivity.class);
        startActivityForResult(intent, ADD_NEW_CITY);
    }

    private void saveCitiesInfoToDatabase(List<CityInfo> citiesInfo) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(citiesInfo);
        realm.commitTransaction();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_NEW_CITY) {
            if (resultCode == Activity.RESULT_OK) {
                adapter.setData(realm.where(CityInfo.class).findAll());
            }
        }
    }


}
