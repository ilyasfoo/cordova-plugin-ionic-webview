package com.ionicframework.cordova.webview;

import android.app.Activity;
import android.content.SharedPreferences;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;
import android.util.Log;

public class IonicWebView extends CordovaPlugin  {

  public static final String WEBVIEW_PREFS_NAME = "WebViewSettings";
  public static final String CDV_SERVER_PATH = "serverBasePath";
  public static CallbackContext callbackContext;

  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
  
    if (action.equals("setServerBasePath")) {
      final String path = args.getString(0);
      cordova.getActivity().runOnUiThread(new Runnable() {
        public void run() {
          ((IonicWebViewEngine)webView.getEngine()).setServerBasePath(path);
        }
      });
      return true;
    } else if (action.equals("getServerBasePath")) {
      callbackContext.success(((IonicWebViewEngine)webView.getEngine()).getServerBasePath());
      return true;
    } else if (action.equals("persistServerBasePath")) {
      String path = ((IonicWebViewEngine)webView.getEngine()).getServerBasePath();
      SharedPreferences prefs = cordova.getActivity().getApplicationContext().getSharedPreferences(WEBVIEW_PREFS_NAME, Activity.MODE_PRIVATE);
      SharedPreferences.Editor editor = prefs.edit();
      editor.putString(CDV_SERVER_PATH, path);
      editor.apply();
      return true;
    } else if (action.equals("networkRequestCallback")) {
      ((IonicWebViewEngine)webView.getEngine()).setIonicWebView(this);
      this.callbackContext = callbackContext;
      return true;
    }

    return false;
  }

  public static void sendEvent(JSONObject _json) {
    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, _json);
    pluginResult.setKeepCallback(true);
    if (callbackContext != null) {
      callbackContext.sendPluginResult(pluginResult);
    }
  }

}

