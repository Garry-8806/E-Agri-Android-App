package com.sourcey.tools;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private double latitude = 0.0;
    private double longitude = 0.0;
    String utype = "";
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    //    @Bind(R.id.input_server)
//    EditText etServer;
    JSONParser jsonParser = new JSONParser();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        checkAndRequestPermissions();
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {
            this.latitude = gpsTracker.getLatitude();
            this.longitude = gpsTracker.getLongitude();
        }
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int id = radioGroup.getCheckedRadioButtonId();
                if (id == R.id.e_user) {
                    utype = "User";
                }
                if (id == R.id.e_service_provider) {
                    utype = "Service Provider";
                }
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 1000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        new CheckLogin().execute();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    JSONObject json;

    public class CheckLogin extends AsyncTask<String, String, String> {
        boolean isLogin = false;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = ServerUtility.url_login();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("utype", utype));
            params.add(new BasicNameValuePair("latitude", "" + latitude));
            params.add(new BasicNameValuePair("longitude", "" + longitude));
        }

        @Override
        protected String doInBackground(String... strings) {
            json = jsonParser.makeHttpRequest(url, "GET", params);
            try {
                ServerUtility.txtEmail = email;
                ServerUtility.txtUsername = json.get("username").toString();
                isLogin = json.has(ServerUtility.TAG_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (isLogin) {
                ServerUtility.latitude = latitude;
                ServerUtility.longitude = longitude;
                if (utype.equals("User")) {
                    startActivity(new Intent(LoginActivity.this, CustomerHomeActivity.class));
                } else {
                    startActivity(new Intent(LoginActivity.this, GarageHomeActivity.class));
                }
                finish();
                Toast.makeText(LoginActivity.this, "Login Success...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Invalid email or password..", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private boolean checkAndRequestPermissions() {
        int external = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int contacts = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (external != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (contacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_CONTACTS);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
