package com.dq.okhttp;

import java.io.IOException;

import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpTestUtil {
	
	public static void request(){
		OkHttpClient httpClient=new OkHttpClient();
		Request request=new Request.Builder().url("http://apis.baidu.com/heweather/weather/free?city=ткЁг")
				.addHeader("apikey","9634c0e3c92f1412150d7ce15fe646cd")
				.build();
		httpClient.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				Log.d("vv", "result = " + arg1.body().string());
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				Log.d("vv", "result onFailure = "+arg1);
				
			}
		});
	}
	
}
