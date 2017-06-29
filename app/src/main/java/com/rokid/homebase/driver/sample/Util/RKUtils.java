package com.rokid.homebase.driver.sample.Util;

import android.util.Log;

import com.rokid.homebase.driver.sample.NativeDriverService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nanohttpd.protocols.http.response.Response;
import nanohttpd.protocols.http.response.Status;


public class RKUtils {
    private static final String JSON_MIME_TYPE = "application/json";
    private static final Status RESP_STATUS_OK = Status.lookup(200);
    public static final int DRIVER_RESP_OKAY = 0;
    public static final int DRIVER_RESP_FAILED = 1;
    public static final int DRIVER_RESP_TIMEOUT = 2;
    private static final String TAG = NativeDriverService.TAG;


    /**
     * 驱动的标准错误返回 errorName 里面填写错误类型
     E_DRIVER_ERROR 通用错误
     E_DRIVER_SIGN_ERROR 签名错误（Token 超时时使用）提醒用户去　App 授权
     E_DRIVER_DEVICE_NO_FOUND 设备不存在
     E_DRIVER_TIMEOUT 设备控制超时
     E_DRIVER_LOGIN_FAILED 登录失败（登录 command 内使用）
     E_DRIVER_WRONG_USERNAME_PASSWORD 用户名密码错误（登录 command 内使用）
     *
     * @param code  错误码, 非0 都可以
     * @param message  这个随便填写.用来后期你们区分到底是什么问题
     * @param errorName 这个按照上面的格式进行返回
     * @return
     */
    public static Response customizedResponse(int code, String message,String errorName) {
        JSONObject resp = new JSONObject();
        try {
            resp.put("status", code);
            resp.put("message", message);
            resp.put("errorName", errorName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "customizedResponse: " + resp);
        return Response.newFixedLengthResponse(
                RKUtils.RESP_STATUS_OK, RKUtils.JSON_MIME_TYPE, resp.toString());
    }


    public static Response okResponse(JSONArray array) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("status", DRIVER_RESP_OKAY);
            obj.put("data", array);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "-------------Response:" + obj.toString());
        return Response.newFixedLengthResponse(
                RKUtils.RESP_STATUS_OK,
                RKUtils.JSON_MIME_TYPE,
                obj.toString());
    }

    public static Response okResponse(JSONObject device) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("status", DRIVER_RESP_OKAY);
            obj.put("data", device);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "-------------Response:" + obj.toString());
        return Response.newFixedLengthResponse(
                RKUtils.RESP_STATUS_OK,
                RKUtils.JSON_MIME_TYPE,
                obj.toString());
    }


    public static JSONObject setMessage(String object){
        JSONObject respObj = new JSONObject();
        try {
            respObj.put("message", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respObj;
    }

}