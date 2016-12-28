package com.dq.retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import android.util.Log;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Converter.Factory;
import retrofit2.Retrofit;

public class RetrofitTestUtil {

	public static void request() {
		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://apis.baidu.com").addConverterFactory(new Factory() {
			
			@Override
			public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
					Retrofit retrofit) {
				return new StringResponseBodyConverter();
			}
			
			@Override
			public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
				return new StringResponseBodyConverter();
			}
			
			class StringResponseBodyConverter implements Converter<ResponseBody, String> {
			    @Override
			    public String convert(ResponseBody value) throws IOException {
			        try {
			            return value.string();
			        } finally {
			            value.close();
			        }
			    }
			}
		}).build();
		Weather w = retrofit.create(Weather.class);
		Call<String> s = w.getResult("运城");
		s.enqueue(new Callback<String>() {

			@Override
			public void onResponse(Call<String> arg0, retrofit2.Response<String> arg1) {
				Log.d("vv", "result = " + arg1.body().toString());

			}

			@Override
			public void onFailure(Call<String> arg0, Throwable arg1) {
				// TODO Auto-generated method stub

			}
		});

		// OkHttpClient httpClient=new OkHttpClient();
		// Request request=new
		// Request.Builder().url("http://apis.baidu.com/heweather/weather/free?city=运城")
		// .addHeader("apikey","9634c0e3c92f1412150d7ce15fe646cd")
		// .build();
		// httpClient.newCall(request).enqueue(new Callback() {
		//
		// @Override
		// public void onResponse(Call arg0, Response arg1) throws IOException {
		// Log.d("vv", "result = " + arg1.body().string());
		// }
		//
		// @Override
		// public void onFailure(Call arg0, IOException arg1) {
		// Log.d("vv", "result onFailure = "+arg1);
		//
		// }
		// });
	}

}
