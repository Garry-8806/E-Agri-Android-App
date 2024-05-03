package com.sourcey.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jaja on 28-12-2015.
 */
public class ServerUtility {
    public static Boolean Isservicelogin = false;
    public static String Server_URL = "http://192.168.43.148:8084/EToolsAdmin/";
    public static String username = "";
    public static String companyName = "";
    public static String status = "";
    public static String VALET = "VALET";
    public static String txtEmail;
    public static String txtUsername;
    public static double latitude = 0.0;
    public static double longitude = 0.0;

    public static String url_confirm_booking() {
        return Server_URL + "ConfirmBooking";
    }

    public static String url_login() {
        return Server_URL + "UserLogin";
    }

    public static String url_register() {
        return Server_URL + "RegisterUser";
    }

    public static String url_get_sp_info() {
        return Server_URL + "GetSPInfo";
    }

    public static String url_view_requests() {
        return Server_URL + "NewBookings";
    }

    public static String url_view_garage_bookings() {
        return Server_URL + "ViewGarageBooking";
    }

    public static String url_view_garage_services() {
        return Server_URL + "ViewGarageServices";
    }

    public static String url_update_garage_services() {
        return Server_URL + "UpdateGarageServices";
    }

    public static String url_change_request_status() {
        return Server_URL + "ChangeRequestStatus";
    }

    public static String url_view_booking() {
        return Server_URL + "ViewBooking";
    }

    public static String url_get_valet() {
        return Server_URL + "GetValetAmount";
    }

    public static String url_update_valet() {
        return Server_URL + "UpdateValetAmount";
    }

    public static String url_get_customer_payment_history() {
        return Server_URL + "GetCustomerPaymentHisory";
    }

    public static String url_get_sp_payment_history() {
        return Server_URL + "GetSPPaymentHistory";
    }

    public static final String TAG_SUCCESS = "success";

    public static String url_get_user_profile() {
        return Server_URL + "UserProfile";
    }

    public static String url_update_user_profile() {
        return Server_URL + "UpdateProfile";
    }

    public static String url_add_tool_details() {
        return Server_URL + "AddToolDetails";
    }


    public static String get_tools_info() {
        return Server_URL + "GetToolDetails";
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");

    }

    public static void setDefaults(String str_key, String value, Context context) {
        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = shre.edit();
        edit.putString(str_key, value);
        edit.apply();
    }

    public static String url_add_tool_request() {
        return Server_URL + "AddToolRequest";
    }
}
