package com.rokid.homebase.driver.sample;

import android.util.Log;

import com.rokid.homebase.driver.sample.Util.Constants;
import com.rokid.homebase.driver.sample.Util.RKUtils;

import org.json.JSONException;

import java.io.IOException;

import nanohttpd.protocols.http.IHTTPSession;
import nanohttpd.protocols.http.NanoHTTPD;
import nanohttpd.protocols.http.request.Method;
import nanohttpd.protocols.http.response.Response;
import nanohttpd.protocols.http.response.Status;


/**
 * Created by heshun on 2017/3/8.
 */

/**
 * this class is used for start a server and receive all request from Rokid homebase application,
 * this server depends on NANOHTTPD;
 * when driverservice start , and then get the port from server(url:
 */
public class NativeDriverServer extends NanoHTTPD {

    private static NativeDriverServer instance = null;

    private static int port;

    public static final String RESPONSE_MIME_TYPE = "application/json";

    private NativeDriverServer(int port) {
        super(port);
        Log.i(NativeDriverService.TAG, "NativeDriverServer: start server port = " + port);
    }

    public static NativeDriverServer getInstance(int port) {
        NativeDriverServer.port = port;
        synchronized (NativeDriverServer.class) {
            if (instance == null) {
                instance = new NativeDriverServer(port);
                Log.i(NativeDriverService.TAG, "getInstance: nativedriverserver is initialize");
            }
        }
        return instance;
    }


    @Override
    public synchronized void closeAllConnections() {
        super.closeAllConnections();
        instance = null;
    }

    public void startServer() {
        if (instance == null) {
            Log.i(NativeDriverService.TAG, "startServer: invalid nativedriverserver and init again");
            instance = new NativeDriverServer(port);
        }

        if (!instance.isAlive()) {
            try {
                instance.start(Constants.TIME_OUT, false);
                Log.i(NativeDriverService.TAG, "startServer: ----");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

            Log.i(NativeDriverService.TAG, "startServer: was started");
        }
    }


    /**
     * request entrance
     *
     * @param session contains request url and request body
     * @return
     */
    @Override
    public Response serve(IHTTPSession session)  {
        super.serve(session);
        if (session != null && Method.POST.equals(session.getMethod())) {
            Response executeResult = null;
            Log.i(NativeDriverService.TAG, "serve: requestJson = " + session.getRawJSON());
            try {
                executeResult = parseRequest(session.getUri(), session.getRawJSON());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return executeResult;
        } else {
            Log.i(NativeDriverService.TAG, "serve: request error");
            return Response.newFixedLengthResponse(Status.BAD_REQUEST, RESPONSE_MIME_TYPE, "bad request");
        }
    }

    /**
     * @param uri    从client过来的request一共有四种类型，分别是:
     *               1、"/list":获取所有设备列表，驱动需要返回当前所有列表设备信息，返回数据格式请参考工程根目录的md文档说明
     *               2、"/execute":用来控制指定的设备行为，每一次请求只会针对一个设备进行控制，并且驱动需要支持client端的并发请求
     *               3、"/command":检验账号是否可用，当驱动控制设备需要用到账号，那么需要在rokid手机app智能家居页面进行账号登录授权，
     *               这个时候会触发驱动的账号验证，此接口必须实现，否则client端认为该账号是错误的账号
     *               4、"/get":获取指定设备的当前的最新状态
     * @param requestBody request data from client
     * @return 返回格式可以参考下面代码,
     */
    private Response parseRequest(String uri, String requestBody) throws JSONException{
        Log.i(NativeDriverService.TAG, "parseRequest: requesturi = " + uri + " requestbody = " + requestBody);
        Response responseData = null;

        if (uri.equals(Constants.URI_COMMAND)) {        // 登录
            responseData = makeFakeUtil.login();
        } else if (uri.equals(Constants.URI_LIST)) {    // 获取所有设备
            responseData = makeFakeUtil.getDevices();
        } else if (uri.equals(Constants.URI_GET)) {     // 获取单个设备 状态
            responseData = makeFakeUtil.getDevice();
        } else if (uri.equals(Constants.URI_EXECUTE)) { // 控制设备
            responseData = makeFakeUtil.controlDevice(requestBody);
        } else {
            //// TODO: 2017/3/14 wrong request
            responseData = RKUtils.customizedResponse(1,"no such command ...","E_DRIVER_ERROR");
        }
        return responseData;
    }

    @Override
    public void stop() {
        super.stop();
    }
}
