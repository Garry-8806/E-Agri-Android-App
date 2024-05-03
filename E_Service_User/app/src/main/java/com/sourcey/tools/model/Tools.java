package com.sourcey.tools.model;

public class Tools {
    private String sr;
    private String title;
    private String cost;
    private String description;
    private String email;
    private String image_name;
    private String added_on;
    private String status;
    private String sp_name;

    public Tools(String sr, String title, String cost, String description, String email, String image_name, String added_on, String status, String sp_name) {
        this.sr = sr;
        this.title = title;
        this.cost = cost;
        this.description = description;
        this.email = email;
        this.image_name = image_name;
        this.added_on = added_on;
        this.status = status;
        this.sp_name = sp_name;
    }

    public String getSp_name() {
        return sp_name;
    }

    public String getSr() {
        return sr;
    }

    public String getTitle() {
        return title;
    }

    public String getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getImage_name() {
        return image_name;
    }

    public String getAdded_on() {
        return added_on;
    }

    public String getStatus() {
        return status;
    }
}
