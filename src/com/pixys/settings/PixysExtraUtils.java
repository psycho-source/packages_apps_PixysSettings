package com.pixys.settings;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;


public class PixysExtraUtils {

    private static final String DEVICES_JSON = "https://raw.githubusercontent.com/PixysOS/official_devices/eleven/devices.json";

    public static JSONArray parseJsonFromFile(Context context, String json) {
        JSONArray res = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput(json)));
            StringBuilder str = new StringBuilder();
            String l;
            while ((l = reader.readLine()) != null) {
                str.append(l).append("\n");
            }
            str.deleteCharAt(str.length() - 1);
            reader.close();
            res = new JSONArray(str.toString());
        } catch (Exception e) {
            Log.e("PixysSettings", "parseJsonFromFile: Failed to parse JSON", e);
        }
        return res;
    }

    public static JSONArray parseJsonFromAssets(String assetName, Context context) {
        JSONArray res = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(assetName)));
            StringBuilder str = new StringBuilder();
            String l;
            while ((l = reader.readLine()) != null) {
                str.append(l).append("\n");
            }
            str.deleteCharAt(str.length() - 1);
            reader.close();
            res = new JSONArray(str.toString());
        } catch (Exception e) {
            Log.e("PixysSettings", "parseJsonFromFile: Failed to parse JSON", e);
        }
        return res;
    }

    public static JSONArray parseDevicesJson(Context context) {
        JSONArray res = null;
        try {
            URL url = new URL(DEVICES_JSON);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(1000);
            InputStream is = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder str = new StringBuilder();
            String l;
            while ((l = reader.readLine()) != null) {
                str.append(l).append("\n");
            }
            str.deleteCharAt(str.length() - 1);
            reader.close();
            is.close();
            res = new JSONArray(str.toString());
            OutputStreamWriter jsonWriter = new OutputStreamWriter(context.openFileOutput("devices.json", Context.MODE_PRIVATE));
            jsonWriter.write(str.toString());
            jsonWriter.close();
        } catch (Exception e) {
            Log.e("PixysSettings", "parseDevicesJson: Failed to parse devices JSON", e);
        }
        return res;
    }

}