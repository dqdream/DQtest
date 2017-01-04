package com.dq.html;

import com.dq.decode.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class HtmlActivity extends Activity {
	private WebView html_web;
	private Button btn;
	String lists[]={"a","b","c","d"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.html_activity);
		btn=(Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				html_web.loadUrl("javascript:androidToHtml([3,2,1])");  
			}
		});
		html_web=(WebView) findViewById(R.id.html_web);
		html_web.loadUrl("file:///android_asset/index.html");
		html_web.getSettings().setJavaScriptEnabled(true);
		html_web.getSettings().setDefaultTextEncodingName("utf-8");
		html_web.addJavascriptInterface(new ActivityJS(), 
				"JsInterface");
		html_web.setWebViewClient(new WebViewClient(){
			 public boolean shouldOverviewUrlLoading(WebView view, String url) {  
		            view.loadUrl(url);  
		            return true;  
		        }  
		});
	}
	
	
	class ActivityJS {    
	    //JavaScript调用此方法
	   @JavascriptInterface     
	   public void callAndroidMethod(String s){
	      Log.d("vv", s+"?????");
	  }
	}
}
