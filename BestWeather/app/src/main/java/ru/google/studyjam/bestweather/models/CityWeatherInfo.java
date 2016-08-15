package ru.google.studyjam.bestweather.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CityWeatherInfo {

    private List<CityInfo> list;

    public CityWeatherInfo() {
    }

    public List<CityInfo> getList() {
        return list;
    }

    public void setList(List<CityInfo> list) {
        this.list = list;
    }
}
