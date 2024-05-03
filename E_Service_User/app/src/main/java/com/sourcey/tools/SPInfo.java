package com.sourcey.tools;

/**
 * Created by SDinesh on 14-03-2018.
 */

public class SPInfo {
    private String username;
    private String email;
    private  String mobile;
    private double lat;
    private double lon;

    public SPInfo(String username, String email, String mobile, double lat, double lon) {
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.lat = lat;
        this.lon = lon;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
