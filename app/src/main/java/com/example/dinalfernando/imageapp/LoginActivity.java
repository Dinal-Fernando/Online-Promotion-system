package com.example.dinalfernando.imageapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    EditText userNameEt;
    EditText passwordEt;
    Spinner userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameEt = (EditText) findViewById(R.id.etUserName);
        passwordEt = (EditText) findViewById(R.id.etPassWord);
        userType = (Spinner) findViewById(R.id.tSpinner);

    }

    public void onLogin(View view) {



        String email = userNameEt.getText().toString().trim();
        String password = passwordEt.getText().toString();

        //email and password validation
        if (!validateEmail(email)) {
            userNameEt.setError("Invalid email");
            userNameEt.requestFocus();
        }
        if (!validatePassword(password)) {
            passwordEt.setError("Invalid password");
            passwordEt.requestFocus();
        }

        login();

    }

    // return true if email validates
    protected boolean validateEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //return true if password is valid
    protected boolean validatePassword(String password) {
        if (password != null && password.length() > 6) {
            return true;
        } else {
            return false;
        }
    }


    public void openDeviceRegActivity() {

        Intent intent = new Intent(this, DeviceRegActivity.class);
        startActivity(intent);

    }



    public void login() {


        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.108/ProjectHeshan/public/api/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("1")) {

                    Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_LONG).show();
                    if(userType.getSelectedItem().equals("technician")){
                        openDeviceRegActivity();
                    }else if(userType.getSelectedItem().equals("user")){
                        startActivity(new Intent(LoginActivity.this,Main2Activity.class));//user activity
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("username", userNameEt.getText().toString());
                params.put("password", passwordEt.getText().toString());
                params.put("uType", userType.getSelectedItem().toString());
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(request);
    }

}
