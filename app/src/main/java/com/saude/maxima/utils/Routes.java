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

    public static final String[] diaries = {
            getProtocol()+getHost()+getPort()+"/api/diaries",
            getProtocol()+getHost()+getPort()+"/api/diaries/{id}"
    };

    public static final String[] pagSeguro = {
            getProtocol()+getHost()+getPort()+"/pagseguro/get_session_id"
    };

    public static final String[] order = {
            getProtocol()+getHost()+getPort()+"/api/orders/{user_id}"
    };

    public static final String[] verifyDateAndHour = {
            getProtocol()+getHost()+getPort()+"/api/orders/verify_date_and_hour"
    };

    public static final String[] schedules = {
            getProtocol()+getHost()+getPort()+"/api/schedules",
            getProtocol()+getHost()+getPort()+"/api/schedules/get_schedules_user/{id}",
    };

    public static final String[] evaluations = {
            getProtocol()+getHost()+getPort()+"/api/evaluations",
            getProtocol()+getHost()+getPort()+"/api/evaluations/get_evaluations_user/{id}",
    };

}
