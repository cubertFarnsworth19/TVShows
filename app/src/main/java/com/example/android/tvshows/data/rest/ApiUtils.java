package com.example.android.tvshows.data.rest;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://api.themoviedb.org";

    public static ApiService getApiRequestData(){
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }

}
