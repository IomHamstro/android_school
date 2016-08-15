package ru.google.studyjam.bestweather.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.google.studyjam.bestweather.models.CityInfo;
import ru.google.studyjam.bestweather.models.CityWeatherInfo;

public interface BestWeatherRest {

    @GET("data/2.5/group")
    Call<CityWeatherInfo> getCurrentByCityIds(@Query("id") String cityIds,
                                              @Query("units") String units,
                                              @Query("appid") String appId);

    @GET("data/2.5/weather")
    Call<CityInfo> getCurrentByCityName(@Query("q") String cityName,
                                        @Query("units") String units,
                                        @Query("appid") String appId);
}
