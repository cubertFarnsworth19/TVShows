package com.example.android.tvshows;

import com.example.android.tvshows.data.rest.ApiService;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = NetworkModule.class)
public class ApiServiceModule {

    @Provides
    @ShowsApplicationScope
    public ApiService geApiService(Retrofit retrofit){
        return retrofit.create(ApiService.class);
    }

    @Provides
    @ShowsApplicationScope
    public Retrofit getRetrofit(OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
