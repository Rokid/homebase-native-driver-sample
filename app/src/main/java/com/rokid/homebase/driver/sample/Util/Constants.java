package com.rokid.homebase.driver.sample.Util;

/**
 * Created by heshun on 2017/3/8.
 */

public interface Constants {

    String GET_PROT_URL = "http://127.0.0.1:4201/native-driver/get-port?vendor=%1$s";


    //example:lifesmart broadlink orvibo
    String VENDOR="samples";

    int TIME_OUT = 5000;

    String URI_LIST = "/list";

    String URI_EXECUTE = "/execute";

    String URI_COMMAND = "/command";

    String URI_GET = "/get";
}
