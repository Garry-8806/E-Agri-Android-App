package com.sourcey.tools;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.sourcey.tools.adapter.SPToolsAdapter;
import com.sourcey.tools.adapter.ToolsAdapter;
import com.sourcey.tools.model.Tools;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesFragment extends Fragment {
    public static List<Tools> tools = new ArrayList<>();
    SPToolsAdapter adapter;
    RecyclerView rv_tools;
    JSONParser jsonParser = new JSONParser();

    public ServicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        setHasOptionsMenu(true);
        rv_tools = (RecyclerView) view.findViewById(R.id.rv_tools);
        new LoadTools().execute();
        return view;
    }

    public class LoadTools extends AsyncTask<String, String, String> {
        List<NameValuePair> params = new ArrayList<>();
        boolean flag = false;
        ProgressDialog dialog;

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ServicesFragment.this.getContext());
            dialog.setMessage("Please wait...");
            dialog.setCancelable(true);
            dialog.show();
            params.add(new BasicNameValuePair("email", ServerUtility.txtEmail));
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject object = jsonParser.makeHttpRequest(ServerUtility.url_view_garage_services(), "POST", params);
            try {
                JSONArray array = object.getJSONArray("ToolsInfo");
                tools = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    String sr = json.getString("sr");
                    String title = json.getString("title");
                    String cost = json.getString("cost");
                    String description = json.getString("description");
                    String email = json.getString("email");
                    String image_name = json.getString("image_name");
                    String added_on = json.getString("added_on");
                    String status = json.getString("status");
                    Tools tool = new Tools(sr, title, cost, description, email, image_name, added_on, status, ServerUtility.txtUsername);
                    tools.add(tool);


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            adapter = new SPToolsAdapter(tools, ServicesFragment.this.getContext());
            rv_tools.setAdapter(adapter);
            rv_tools.setLayoutManager(new LinearLayoutManager(ServicesFragment.this.getContext()));
            adapter.notifyDataSetChanged();
        }
    }
}
