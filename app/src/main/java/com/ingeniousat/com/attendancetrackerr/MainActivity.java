package com.ingeniousat.com.attendancetrackerr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText emailEdt,passwordEdt;
    Button submitBtn;
    String url = "http://192.168.0.103/AttendancePhp/login.php";
    String email,password;
    TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEdt = (EditText) findViewById(R.id.email);
        passwordEdt = (EditText) findViewById(R.id.password);
        signupText = (TextView) findViewById(R.id.creteaccount);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);

        String emailvalue = pref.getString("email","defaul_value");
        if (pref.getBoolean("activity_executed", false)) {
            Intent intent = new Intent(this, AttendanceActivity.class);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("val","1");
            editor.putString("email",emailvalue);
            editor.apply();

            startActivity(intent);
            finish();
        }

        submitBtn = (Button) findViewById(R.id.login_button);
        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                email = emailEdt.getText().toString();
                password = passwordEdt.getText().toString();

                Intent intent = new Intent(MainActivity.this, AttendanceActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
                /*
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                //this is the url where you want to send the request
                //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Intent intent = new Intent(MainActivity.this, AttendanceActivity.class);
                                intent.putExtra("email",email);
                                startActivity(intent);
                                finish();
                                // Display the response string.
                                //_response.setText(response);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //_response.setText("That didn't work!");
                    }
                }) {
                    //adding parameters to the request
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("email", email);
                        params.put("password",password);

                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
                */
            }
        });
            }


}
