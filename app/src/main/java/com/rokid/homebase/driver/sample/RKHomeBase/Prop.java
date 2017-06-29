package com.rokid.homebase.driver.sample.RKHomeBase;

import java.util.List;

/**
 * Created by demo on 10/23/16.
 */

public class Prop {
    private String name;
    private String value;

    //RO,RW
    private List<String> allowedValues;

    public Prop() {
    }
    public Prop(String name, List<String> allowedValues) {
        this.name = name;
        this.allowedValues = allowedValues;
    }

    public Prop(String name,String state) {
        this.name = name;
        this.value = state;
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

    public List<String> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(List<String> allowedValues) {
        this.allowedValues = allowedValues;
    }

    @Override
    public String toString() {
        return "Prop{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", allowedValues=" + allowedValues +
                '}';
    }
}
