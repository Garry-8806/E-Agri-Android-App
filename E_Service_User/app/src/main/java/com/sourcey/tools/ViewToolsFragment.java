package com.sourcey.tools;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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
public class ViewToolsFragment extends Fragment {
    public static List<Tools> tools = new ArrayList<>();
    ToolsAdapter adapter;
    RecyclerView rv_tools;
    JSONParser jsonParser = new JSONParser();


    public ViewToolsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_tools, container, false);
        setHasOptionsMenu(true);
        rv_tools = (RecyclerView) view.findViewById(R.id.rv_tools);
        new LoadTools().execute();
        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_option_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((CustomerHomeActivity) getContext()).getSupportActionBar().getThemedContext());
        item.setShowAsAction(MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }


    public class LoadTools extends AsyncTask<String, String, String> {
        List<NameValuePair> params = new ArrayList<>();
        boolean flag = false;
        ProgressDialog dialog;

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ViewToolsFragment.this.getContext());
            dialog.setMessage("Please wait...");
            dialog.setCancelable(true);
            dialog.show();
            params.add(new BasicNameValuePair("email", ServerUtility.txtEmail));
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject object = jsonParser.makeHttpRequest(ServerUtility.get_tools_info(), "POST", params);
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
                    String sp_name = json.getString("spname");
                    Tools tool = new Tools(sr, title, cost, description, email, image_name, added_on, status, sp_name);
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
            adapter = new ToolsAdapter(tools, ViewToolsFragment.this.getContext());
            rv_tools.setAdapter(adapter);
            rv_tools.setLayoutManager(new LinearLayoutManager(ViewToolsFragment.this.getContext()));
            adapter.notifyDataSetChanged();
        }
    }

}
