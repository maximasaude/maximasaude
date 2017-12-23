package com.saude.maxima.utils;

/**
 * Created by Junnyor on 18/10/2017.
 */

public class Config {

    private static final String protocol = "http://";
    private static final String host = "10.0.0.102";
    private static final String port = ":8000";

    public static String getHost(){
        return host;
    }

    public static String getPort(){
        return port;
    }

    public static String getProtocol(){
        return protocol;
    }

}
