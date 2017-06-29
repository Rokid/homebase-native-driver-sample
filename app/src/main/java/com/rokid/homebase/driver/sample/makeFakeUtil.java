package com.rokid.homebase.driver.sample;


import android.util.Log;

import com.rokid.homebase.driver.sample.RKHomeBase.Action;
import com.rokid.homebase.driver.sample.RKHomeBase.RokidDevice;
import com.rokid.homebase.driver.sample.Util.RKUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import nanohttpd.protocols.http.response.Response;


/**
 * Created by heshun on 2017/3/14.
 */

public class makeFakeUtil {
    //-----登录
    public static Response login()  throws JSONException{
        JSONObject js = new JSONObject();
        js.put("userId", "110");
        js.put("userToken", "123456");
        return RKUtils.okResponse(js);
    }

    // -----获取多个设备
    public static Response getDevices() throws JSONException {
        JSONArray json = new JSONArray();
        json.put(getLightDevice("110").toJSONArray());
        json.put(getLightDevice("001").toJSONArray());
        json.put(getLightDevice("007").toJSONArray());
        return RKUtils.okResponse(json);
    }

    // -----获取单个设备状态
    public static Response getDevice() throws JSONException {
        return RKUtils.okResponse(getLightDevice("001").toJSONObjects());
    }

    /**
     * 获取所有设备填写参数 可以参考 https://rokid.github.io/rokid-homebase-docs/device/device.html
     *
     * deviceId          设备唯一ID 用来区分不同设备的唯一凭证
     * name              设备名称   定义在rokid显示设备的名称
     * offline           在线状态   如不知道设备在线状态.默认填写 false
     * type              设备类型   https://rokid.github.io/rokid-homebase-docs/device/type.html
     * rkDevice.addAction 第1个参数是设备能力类型, 第2个参数是支持的能力,第3个参数是该能力的状态属性
     */
    public static RokidDevice getLightDevice(String id) {
        RokidDevice rkDevice = new RokidDevice();
        rkDevice.setDeviceId(id);
        rkDevice.setName("测试灯泡 " + id);
        rkDevice.setOffline(false);
        rkDevice.setType("light");

        // 设置设备能力  用来告诉rokid,支持什么类型指令,如没有填写.则不会下发指令到 nativer driver
        // 可以参考文档  https://rokid.github.io/rokid-homebase-docs/device/actions-and-state.html
        rkDevice.addAction("switch",Arrays.asList("on", "off"));//例如支持 开关
        rkDevice.addState("switch","on");                       //当前开关 是开的状态

        rkDevice.addAction("color",Arrays.asList("num"));      //例如支持 调节颜色
//        rkDevice.addState("color","1333333");                //当前颜色值不知道 ,则不填写

        //如果写了 num, rokid会根据上一次num 自动计算num,下发给 nativer driver.
        //如需要自己实现 调高/调低/最高/最低 rkDevice.addAction("brightness", Arrays.asList("up","down","min,"max"));
        rkDevice.addAction("brightness", Arrays.asList("num")); //例如支持 调节亮度
        rkDevice.addState("brightness","30");                   //当前亮度 30

        return rkDevice;
    }

    // 判断设备类型
    /**
     * // body:
     {
         "env":{"deviceId":"rokid/0201021702000187"},
         "device":
                {
                "offline":false,
                "name":"light sample",
                "type":"light",
                "state":{
                        "switch":"on",
                        "color":7856,
                        "color_temperature":null},
                "deviceId":"110",
                "vendor":"sample",
                "userAuth":{"userId":"sample"}},
         "action":{"switch":"on"}
     }
     */
    //-----控制设备
    public static Response controlDevice(String Jsbody) throws JSONException {
        RokidDevice device = RokidDevice.getControleDevice(Jsbody);
        Action controlActions = Action.getControlActions(Jsbody);
        Log.d("sample", "device: " + device.toString());
        Log.d("sample", "controlAction: " + controlActions.toString());
        int code = getRandom(3);
        switch (code) {
            case 0://成功
                JSONObject expObj = new JSONObject();
                expObj.put("switch", "off");
                return RKUtils.okResponse(expObj);
            case 1://失败
                return RKUtils.customizedResponse(RKUtils.DRIVER_RESP_FAILED, "control failed","E_DRIVER_ERROR");
            case 2://超时
                return RKUtils.customizedResponse(RKUtils.DRIVER_RESP_TIMEOUT, "tiem out","E_DRIVER_TIMEOUT");
        }
        return RKUtils.customizedResponse(RKUtils.DRIVER_RESP_FAILED, null,"E_DRIVER_DEVICE_NO_FOUND");
    }

    public static int getRandom(int i){
        return (int) ((Math.random()) * i);
    }



}
