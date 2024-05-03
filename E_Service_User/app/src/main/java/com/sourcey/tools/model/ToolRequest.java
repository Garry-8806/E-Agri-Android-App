package com.sourcey.tools.model;

public class ToolRequest {
    String id;
    String cust_name;
    String cust_email;
    String sp_name;
    String sp_email;
    String tool_name;
    String request_status;
    String request_on;
    String delivered_on;
    String return_on;
    int hours;
    double total_cost;
    double cost;
    String lat;
    String lon;
    String mobile;

    public ToolRequest(String id, String cust_name, String cust_email, String sp_name, String sp_email, String tool_name, String request_status, String request_on, String delivered_on, String return_on, int hours, double total_cost, double cost,String lat,String lon) {
        this.id = id;
        this.cust_name = cust_name;
        this.cust_email = cust_email;
        this.sp_name = sp_name;
        this.sp_email = sp_email;
        this.tool_name = tool_name;
        this.request_status = request_status;
        this.request_on = request_on;
        this.delivered_on = delivered_on;
        this.return_on = return_on;
        this.hours = hours;
        this.total_cost = total_cost;
        this.cost = cost;
        this.lat=lat;
        this.lon=lon;
    }

    public void setSp_name(String sp_name) {
        this.sp_name = sp_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getId() {
        return id;
    }

    public String getCust_name() {
        return cust_name;
    }

    public String getCust_email() {
        return cust_email;
    }

    public String getSp_name() {
        return sp_name;
    }

    public String getSp_email() {
        return sp_email;
    }

    public String getTool_name() {
        return tool_name;
    }

    public String getRequest_status() {
        return request_status;
    }

    public String getRequest_on() {
        return request_on;
    }

    public String getDelivered_on() {
        return delivered_on;
    }

    public String getReturn_on() {
        return return_on;
    }

    public int getHours() {
        return hours;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public double getCost() {
        return cost;
    }
}
