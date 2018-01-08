package de.smacsoftwares.aplanapp.util;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import de.smacsoftwares.aplanapp.fragment.FragmentProject;


public class JavaScriptInterface
{
    protected FragmentProject parentActivity;
    protected WebView mWebView;
    
    
    public JavaScriptInterface(FragmentProject _activity, WebView _webView)  {
        parentActivity = _activity;
        mWebView = _webView;
        
    }
    
    @JavascriptInterface
	public void loadURL(String url) {
		final String u = url;

		/*parentActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mWebView.loadUrl(u);
			}
		});*/
	}
    
    @JavascriptInterface
    public void setResult(int val){
        Log.v("mylog","JavaScriptHandler.setResult is called : " + val);
       /* this.parentActivity.javascriptCallFinished(val);*/
    }
    
    @JavascriptInterface
    public void calcSomething(int x, int y){
        this.parentActivity.changeText("Result is : " + (x * y));
    }
    
    @JavascriptInterface
    public String testString() {
    	return "test string from java";
    }
}
