package ru.google.studyjam.bestweather.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.realm.RealmObject;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WindInfo extends RealmObject {
    private Double speed;

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }
}
