package ru.google.studyjam.bestweather.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CityInfo extends RealmObject {

    @JsonProperty("id")
    @PrimaryKey
    private long cityId;
    private String name;
    @JsonProperty("dt") private long date;
    private DayInfo main;
    private RealmList<DayWeather> weather;
    private WindInfo wind;

    public CityInfo() {
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DayInfo getMain() {
        return main;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setMain(DayInfo main) {
        this.main = main;
    }

    public RealmList<DayWeather> getWeather() {
        return weather;
    }

    public void setWeather(RealmList<DayWeather> weather) {
        this.weather = weather;
    }

    public WindInfo getWind() {
        return wind;
    }

    public void setWind(WindInfo wind) {
        this.wind = wind;
    }
}
