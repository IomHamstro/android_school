package ru.google.studyjam.bestweather.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.google.studyjam.bestweather.BuildConfig;
import ru.google.studyjam.bestweather.Config;

public class SessionRestManager {

    private static volatile SessionRestManager sInstance;

    private SessionRestManager() {
    }

    public static SessionRestManager getInstance() {
        if (sInstance == null)
            synchronized (SessionRestManager.class) {
                if (sInstance == null)
                    sInstance = new SessionRestManager();
            }
        return sInstance;
    }

    private OkHttpClient setupHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        // Задаём "уровень" логирования запросов
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);

        if (BuildConfig.DEBUG) httpClient.addNetworkInterceptor(new StethoInterceptor());

        return httpClient.build();
    }

    private final Retrofit REST_ADAPTER = new Retrofit.Builder()
            .baseUrl(Config.ENDPOINT)
            .client(setupHttpClient())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    public BestWeatherRest getRest() {
        return REST_ADAPTER.create(BestWeatherRest.class);
    }
}
