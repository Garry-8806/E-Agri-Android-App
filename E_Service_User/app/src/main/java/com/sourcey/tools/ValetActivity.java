package com.sourcey.tools;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ValetActivity extends AppCompatActivity {
    JSONParser jsonParser = new JSONParser();
    EditText etCard, etPin, etCvv, etAmount;
    Button btnAdd;
    TextView txtValetAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valet);
        etCard = (EditText) findViewById(R.id.etCardNumber);
        etCvv = (EditText) findViewById(R.id.etCVV);
        etPin = (EditText) findViewById(R.id.etPin);
        etAmount = (EditText) findViewById(R.id.etAmount);
        btnAdd = (Button) findViewById(R.id.btnAddmoney);
        txtValetAmount = (TextView) findViewById(R.id.txtValetAmount);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAmount.getText().toString().equals("") || etCvv.getText().toString().equals("") || etCard.getText().toString().equals("") || etPin.getText().toString().equals("")) {
                    Toast.makeText(ValetActivity.this, "All Details Required...", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    new UpdateValetInfo().execute();
                }
            }
        });
        new GetValetAmount().execute();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public class UpdateValetInfo extends AsyncTask<String, String, String> {
        List<NameValuePair> params = new ArrayList<>();
        String amount = "0.0";
        boolean flag = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params.add(new BasicNameValuePair("username", ServerUtility.txtEmail));
            params.add(new BasicNameValuePair("amount", etAmount.getText().toString()));
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject jsonObject = jsonParser.makeHttpRequest(ServerUtility.url_update_valet(), "GET", params);
            flag = jsonObject.has(ServerUtility.TAG_SUCCESS);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (flag) {
                Toast.makeText(ValetActivity.this, "Valet Updated successfully..", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ValetActivity.this, "Valet Updatation failed..", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public class GetValetAmount extends AsyncTask<String, String, String> {
        List<NameValuePair> params = new ArrayList<>();
        String amount = "0.0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params.add(new BasicNameValuePair("username", ServerUtility.txtEmail));
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject jsonObject = jsonParser.makeHttpRequest(ServerUtility.url_get_valet(), "GET", params);
            try {
                amount = jsonObject.getString("Amount");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            txtValetAmount.setText("(₹ " + amount + " )");
        }
    }
}
