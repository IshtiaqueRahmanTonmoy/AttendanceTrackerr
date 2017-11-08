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

    EditText empidEdt,passwordEdt;
    Button submitBtn;
    String url = "http://demo.ingtechbd.com/attendance/login.php";
    String employee_id,password;
    TextView signupText;
    SharedPreferences pref;
    SharedPreferences.Editor edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        empidEdt = (EditText) findViewById(R.id.employeeid);
        passwordEdt = (EditText) findViewById(R.id.password);

        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        int getid = pref.getInt("id",0);
        String empid = pref.getString("empid",null);
        if(getid == 1){
            Intent intent = new Intent(MainActivity.this,AttendanceActivity.class);
            intent.putExtra("employeeid",empid);
            startActivity(intent);
            finish();
        }


        submitBtn = (Button) findViewById(R.id.login_button);
        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                employee_id = empidEdt.getText().toString();
                password = passwordEdt.getText().toString();

                /*
                Intent intent = new Intent(MainActivity.this, AttendanceActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
                */

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                //this is the url where you want to send the request
                //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("Connected successfully<br>{\"success\":0,\"message\":\"Oops! An error occurred.\"}")){
                                    Toast.makeText(MainActivity.this, "Please enter correct information..", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    if(employee_id.equals("1001")){
                                        Intent intent = new Intent(MainActivity.this,AdminActivity.class);
                                        startActivity(intent);
                                    }

                                    else{
                                        Intent intent = new Intent(MainActivity.this,AttendanceActivity.class);
                                        intent.putExtra("employeeid",employee_id);

                                        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                                        edt = pref.edit();
                                        edt.putInt("id",1);
                                        edt.putString("empid",employee_id);
                                        edt.commit();

                                        startActivity(intent);
                                    }


                                }
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

                        params.put("email", employee_id);
                        params.put("password",password);

                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);

            }
        });
            }


}
