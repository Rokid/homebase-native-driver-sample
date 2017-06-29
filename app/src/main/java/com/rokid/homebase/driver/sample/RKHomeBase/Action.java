package com.rokid.homebase.driver.sample.RKHomeBase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by a123456 on 2017/4/8.
 */

public class Action {
    private String property;
    private String name;
    private String value;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static Action getControlActions(String Jsbody) throws JSONException {
        JSONObject actions = new JSONObject(Jsbody).getJSONObject("action");
        Action action = new Action();
        if (actions.has("property"))action.setProperty("property");
        if (actions.has("name"))action.setName(actions.getString("name"));
        if (actions.has("value"))action.setValue(actions.getString("value"));
        return action;
    }

    @Override
    public String toString() {
        return "Action{" +
                "property='" + property + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
