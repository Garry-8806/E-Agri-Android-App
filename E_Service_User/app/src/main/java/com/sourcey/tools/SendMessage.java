package com.sourcey.tools;

/**
 * Created by Dell on 01-05-2017.
 */

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SendMessage {
    String postData = "";
    String retval = "";
    String Username = "richhelps1234";
    String Password = "richhelps1234";
    String MobileNo = "9503986854";
    String Message = "Testing message";
    String SenderID = "PRJCTP";
    String priority = "ndnd";
    String stype = "normal";
    JSONParser jParser = new JSONParser();

    public SendMessage(String mobile, String message) {
        this.MobileNo = mobile;
        this.Message = message;
        new SendData().execute();
    }


    public class SendData extends AsyncTask<String, String, String> {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params.add(new BasicNameValuePair("user", Username));
            params.add(new BasicNameValuePair("pass", Password));
            params.add(new BasicNameValuePair("phone", MobileNo));
            params.add(new BasicNameValuePair("sender", SenderID));
            params.add(new BasicNameValuePair("text", Message));
            params.add(new BasicNameValuePair("priority", priority));
            params.add(new BasicNameValuePair("stype", stype));
            url = "http://bhashsms.com/api/sendmsg.php";
        }

        @Override
        protected String doInBackground(String... args) {

            JSONObject json;
            json = jParser.makeHttpRequest(url, "GET", params);
            return null;
        }
    }


}
