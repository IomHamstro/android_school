package ru.google.studyjam.bestweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.google.studyjam.bestweather.Config;
import ru.google.studyjam.bestweather.R;
import ru.google.studyjam.bestweather.models.CityInfo;

public class NewCityActivity extends BaseActivity {

    @BindView(R.id.editText_city) EditText editTextCity;

    @OnClick(R.id.btn_sendCity)
    public void onClick() {
        load(editTextCity.getText().toString());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_city);
        ButterKnife.bind(this);
        setupToolbar(R.string.new_city_toolbar_title, true);
    }

    public void sendNewCity(View view) {
        EditText nameField = (EditText) findViewById(R.id.editText_city);
        String cityName = nameField != null ? nameField.getText().toString() : "";

        if (load(cityName)) {
            Intent intent = new Intent(NewCityActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private boolean load(String city) {
        showProgressBar();
        Call<CityInfo> call
                = getRest().getCurrentByCityName(city, "metric", Config.APPLICATION_ID);
        call.enqueue(new Callback<CityInfo>() {
            @Override
            public void onResponse(Call<CityInfo> call, final Response<CityInfo> response) {
                if (response.isSuccessful()) {
                    if (response.body().getName() == null || response.body().getName().isEmpty()) {
                        hideProgressBar();
                        Toast.makeText(NewCityActivity.this,
                                R.string.new_city_wrong_city_name_message, Toast.LENGTH_LONG).show();
                    } else {
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm bgRealm) {
                                bgRealm.copyToRealm(response.body());
                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                // Transaction was a success.
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                hideProgressBar();
                                Toast.makeText(NewCityActivity.this,
                                        R.string.new_city_successful_added_message, Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                // Transaction failed and was automatically canceled.
                                hideProgressBar();
                                Toast.makeText(NewCityActivity.this,
                                        R.string.new_city_already_exist_message, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<CityInfo> call, Throwable t) {
                Toast.makeText(NewCityActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });

        return call.isExecuted();
    }


}
