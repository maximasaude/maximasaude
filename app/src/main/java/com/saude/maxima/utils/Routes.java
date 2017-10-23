package com.saude.maxima.utils;

import org.json.JSONObject;

/**
 * Created by Junnyor on 16/10/2017.
 */

public class Routes extends Config{

    public static final String home = getProtocol()+getHost()+getPort()+"/api/home";
    public static final String takeToken = getProtocol()+getHost()+getPort()+"/oauth/token";
    public static final String takeUser = getProtocol()+getHost()+getPort()+"/api/user";
    public static final String createUser = getProtocol()+ getHost()+getPort()+"/api/users";

    public static final String[] packages = {
            getProtocol()+getHost()+getPort()+"/api/packages",
            getProtocol()+getHost()+getPort()+"/api/packages/{id}"
    };


}
