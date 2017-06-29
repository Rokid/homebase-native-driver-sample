package com.rokid.homebase.driver.sample;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.rokid.homebase.driver.sample.Util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by heshun on 2017/2/9.
 */

public class RequestPortTask implements Runnable {

    private final Handler mHandler;


    private static final int TIMEOUT = 5000;

    private static final int DEFAULT_MSG_ARG1 = -1;

    public RequestPortTask(@NonNull Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void run() {

        try {
            URL url = new URL(String.format(Constants.GET_PROT_URL, Constants.VENDOR));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIMEOUT);
            conn.connect();
            int code = conn.getResponseCode();
            Log.d(NativeDriverService.TAG, "-----code:" + code);
            switch (code) {
                case HttpURLConnection.HTTP_OK: {
                    String responseData = parseResponseStream(conn.getInputStream());
                    Log.i(NativeDriverService.TAG, "run: response data = " + responseData);
                    JSONObject portJsonObj = new JSONObject(responseData);
                    if (checkPortJson(responseData)) {

                        sendMessage(NativeDriverService.REQUEST_PORT_SUCCESS, portJsonObj.getJSONObject("data").getInt("port"));
                    } else {

                        sendMessage(NativeDriverService.REQUEST_PORT_FAILED, DEFAULT_MSG_ARG1);
                    }
                }
                break;

                case 500:{
                    sendMessage(NativeDriverService.REQUEST_PORT_SUCCESS,19999);
                }
                break;

                default:
                    Log.e(NativeDriverService.TAG, "run: code = " + code);
                    sendMessage(NativeDriverService.CONN_ERROR, DEFAULT_MSG_ARG1);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            sendMessage(NativeDriverService.IOEXCEPTION_ERROR, DEFAULT_MSG_ARG1);
        } catch (JSONException e) {
            e.printStackTrace();
            sendMessage(NativeDriverService.JSONEXCEPTION_ERROR, DEFAULT_MSG_ARG1);
        }


    }

    private void sendMessage(int what, int arg1) {

        if (null != mHandler) {
            Message message = mHandler.obtainMessage(what);
            if (arg1 != -1) {
                message.arg1 = arg1;
            }
            message.sendToTarget();
        } else {
            Log.e(NativeDriverService.TAG, "sendMessage: mHandler was not initialized");
        }
    }

    private boolean checkPortJson(String portJsonString) throws JSONException {
        if (TextUtils.isEmpty(portJsonString)) {
            return false;
        }
        JSONObject portJsonObj = new JSONObject(portJsonString);
        return portJsonObj.has("status") && portJsonObj.getInt("status") == 0
                && portJsonObj.has("data") && portJsonObj.getJSONObject("data") != null
                && portJsonObj.getJSONObject("data").has("port")
                && portJsonObj.getJSONObject("data").getInt("port") > 0;
    }


    private String parseResponseStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = reader.readLine()) != null) {
            result += line;
        }
        return result;
    }


}
