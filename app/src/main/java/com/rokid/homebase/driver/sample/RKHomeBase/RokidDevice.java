package com.rokid.homebase.driver.sample.RKHomeBase;


import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a123456 on 2017/4/6.
 */

public class RokidDevice {
    private String deviceInfo;        // 可以存储 任何Json数据,可选字段       (可选)
    private String deviceId;          // 设备id                            (必填)
    private String name;              // 设备名称                           (必填)
    private String type;              // 设备类型                           (必填)
    private boolean offline;          // 在线状态 true 离线 / false 在线     (必填)
    private List<Prop> actions;     // 设备能力                 (必填)
    private List<Prop> state;     // 设备能力                 (必填)


    public boolean isOffline() {
        return offline;
    }

    public List<Prop> getState() {
        return state;
    }

    public void setState(List<Prop> state) {
        this.state = state;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public List<Prop> getActions() {
        return actions;
    }

    public void setActions(List<Prop> actions) {
        this.actions = actions;
    }

    public void addAction(String name, List<String> action) {
        if (actions == null) actions = new ArrayList<Prop>();
        actions.add(new Prop(name, action));
    }

    public void addState(String name, String stat) {
        if (state == null) state = new ArrayList<Prop>();
        state.add(new Prop(name, stat));
    }

    public JSONObject toJSONArray() throws JSONException {
        if (deviceId == null && name == null) throw new NullPointerException();

        JSONObject obj = new JSONObject();
        obj.put("type", type);
        obj.put("deviceId", deviceId);
        obj.put("name", name);
        if (deviceInfo != null) obj.put("deviceInfo", deviceInfo);
        JSONObject action = new JSONObject();
        JSONObject stat = new JSONObject();
        if (actions != null) {
            for (Prop prop : actions) {
                if (prop == null) continue;
                if (prop.getAllowedValues().size() > 0) {
                    JSONArray array = new JSONArray();
                    for (String act : prop.getAllowedValues()) {
                        array.put(act);
                    }
                    action.put(prop.getName(), array);
                }
            }
        }

        if (state != null) {
            for (Prop prop : state) {
                if (prop == null) continue;
                stat = getJsonRetuls(stat, prop);
            }
        }
        obj.put("offline", offline);
        obj.put("actions", action);
        if (stat != null) obj.put("state", stat);
        return obj;
    }

    public JSONObject toJSONObjects() throws JSONException {
        if (deviceId == null) throw new NullPointerException();

        JSONObject obj = new JSONObject();
        obj.put("deviceId", deviceId);
        JSONObject stat = new JSONObject();
        for (Prop prop : state) {
            stat = getJsonRetuls(stat, prop);
        }
        if (stat != null) obj.put("state", stat);
        obj.put("offline", offline);
        return obj;
    }

    private JSONObject getJsonRetuls(JSONObject stat, Prop prop) throws JSONException {
        if (toInteger(prop.getValue()) == null) {
            stat = prop.getValue() == null ? stat.put(prop.getName(), JSONObject.NULL) : stat.put(prop.getName(), prop.getValue());
        } else {
            stat.put(prop.getName(), toInteger(prop.getValue()));
        }
        return stat;
    }

    public static Integer toInteger(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return (int) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    public static RokidDevice getControleDevice(String Jsbody) throws JSONException {
        JSONObject body = new JSONObject(Jsbody);
        JSONObject device = body.getJSONObject("device");
        RokidDevice rokid = new RokidDevice();
        rokid.setDeviceId(device.getString("deviceId"));
        if (device.has("name")) rokid.setName(device.getString("name"));
        if (device.has("type")) rokid.setType(device.getString("type"));
        if (device.has("offline"))rokid.setOffline(device.getBoolean("offline"));
        if (device.has("deviceInfo")) rokid.setDeviceInfo(device.getString("deviceInfo"));
        if (device.has("actions")) rokid.setActions(getProps(device.getJSONObject("actions")));
        if (device.has("state")) rokid.setState(getProps(device.getJSONObject("state")));

        return rokid;
    }

    @NonNull
    private static List<Prop> getProps(JSONObject jsonObject) throws JSONException {
        List<Prop> statelists = new ArrayList<Prop>();
        for (int i = 0; i < jsonObject.names().length(); i++) {
            String name = jsonObject.names().get(i).toString();
            Prop stateProp = new Prop();
            stateProp.setName(name);
            stateProp.setValue(jsonObject.getString(name));
            statelists.add(stateProp);
        }
        return statelists;
    }

    public static List<Prop> getControlAction(String Jsbody) throws JSONException {
        return getProps(new JSONObject(Jsbody).getJSONObject("action"));
    }


    @Override
    public String toString() {
        return "RokidDevice{" +
                "deviceInfo='" + deviceInfo + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", offline=" + offline +
                ", actions=" + actions +
                ", state=" + state +
                '}';
    }
}
