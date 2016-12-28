package com.dq.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Weather {

	@GET("heweather/weather/free")
	@Headers("apikey:9634c0e3c92f1412150d7ce15fe646cd")
	Call<String> getResult(@Query("city") String city);
 	
}
