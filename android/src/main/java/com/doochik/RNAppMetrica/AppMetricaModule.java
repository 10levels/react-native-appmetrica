package com.doochik.RNAppMetrica;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

import java.lang.Exception;

import org.json.JSONObject;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class AppMetricaModule extends ReactContextBaseJavaModule {
    final static String ModuleName = "AppMetrica";

    public AppMetricaModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return ModuleName;
    }


    @ReactMethod
    public void activateWithApiKey(String key) {
        YandexMetricaConfig.Builder configBuilder = YandexMetricaConfig.newConfigBuilder(key);
        YandexMetrica.activate(getReactApplicationContext().getApplicationContext(), configBuilder.build());

        Activity activity = getCurrentActivity();
        if (activity != null) {
            Application application = activity.getApplication();
            YandexMetrica.enableActivityAutoTracking(application);
        }
    }

    @ReactMethod
    public void reportError(String message, @Nullable String params) {
        if (params != null) {
            YandexMetrica.reportError(message, new Throwable(params));
        }
        else {
            YandexMetrica.reportError(message, new Throwable());
        }
    }

    @ReactMethod
    public void reportEvent(String message, @Nullable ReadableMap params) {
        if (params != null) {
            YandexMetrica.reportEvent(message, convertReadableMapToJson(params));
        } else {
            YandexMetrica.reportEvent(message);
        }
    }

    @ReactMethod
    public void setUserProfileID(String profileID) {
        YandexMetrica.setUserProfileID(profileID);
    }

    private String convertReadableMapToJson(final ReadableMap readableMap) {
		ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
        JSONObject json = new JSONObject();

        try {
            while (iterator.hasNextKey()) {
                String key = iterator.nextKey();

                switch (readableMap.getType(key)) {
                    case Null:
                        json.put(key, null);
                        break;
                    case Boolean:
                        json.put(key, readableMap.getBoolean(key));
                        break;
                    case Number:
                        json.put(key, readableMap.getDouble(key));
                        break;
                    case String:
                        json.put(key, readableMap.getString(key));
                        break;
                    case Array:
                        json.put(key, readableMap.getArray(key));
                        break;
                    case Map:
                        json.put(key, convertReadableMapToJson(readableMap.getMap(key)));
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            Log.d(ModuleName, "convertReadableMapToJson fail: " + ex);
        }

        return json.toString();
    }
}
