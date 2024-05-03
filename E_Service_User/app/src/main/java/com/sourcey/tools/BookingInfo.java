package com.sourcey.tools;

/**
 * Created by Dinesh on 4/13/2018.
 */

public class BookingInfo {
    private String email, mobile, status, vehicleno, name, id, lat, lon, amount, pickup_time, return_time, date, description;

    public BookingInfo(String email, String mobile, String status, String vehicleno, String name, String id, String lat, String lon, String amount, String pickup_time, String return_time, String date, String description) {
        this.email = email;
        this.mobile = mobile;
        this.status = status;
        this.vehicleno = vehicleno;
        this.name = name;
        this.date = date;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.amount = amount;
        this.pickup_time = pickup_time;
        this.return_time = return_time;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getPickup_time() {
        return pickup_time;
    }

    public String getReturn_time() {
        return return_time;
    }

    public String getAmount() {
        return amount;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getStatus() {
        return status;
    }

    public String getVehicleno() {
        return vehicleno;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }


}
