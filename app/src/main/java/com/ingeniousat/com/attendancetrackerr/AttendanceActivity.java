package com.ingeniousat.com.attendancetrackerr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity {

    CheckBox inTime,outTime;
    EditText remarks;
    Button submit;
    Date datetime;
    DateFormat dateFormat;
    Calendar cal,cal1;
    SimpleDateFormat sdf,sdf1;
    String employee_id;
    String remarksEdt,date;
    boolean value;
    String employeeid;
    String urlvalue = "http://ingtechbd.com/demo/attendance/getvalue.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        inTime = (CheckBox) findViewById(R.id.checkBox1);
        outTime = (CheckBox) findViewById(R.id.checkBox2);


        Intent intent = getIntent();
        employee_id = intent.getExtras().getString("employeeid");

        //Toast.makeText(AttendanceActivity.this, ""+easyPuzzle, Toast.LENGTH_SHORT).show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("val", "");
        if(!name.equalsIgnoreCase(""))
        {
            inTime.setEnabled(false);
        }

        //Toast.makeText(AttendanceActivity.this, ""+val, Toast.LENGTH_SHORT).show();

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        datetime = new Date();
        //System.out.println(dateFormat.format(date));


        outTime.setChecked(false);

        remarks = (EditText) findViewById(R.id.remarksEdt);
    }


    public void OnSubmit(View view) {

        //getValue(email);
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString("email",employee_id);
        edt.putBoolean("activity_executed", true);
        edt.commit();

        if (inTime.isChecked()) {

            cal = Calendar.getInstance();
            sdf = new SimpleDateFormat("hh:mm:ss a");
            String t =
            remarksEdt = remarks.getText().toString();
            date = dateFormat.format(datetime);

            RequestQueue queue = Volley.newRequestQueue(AttendanceActivity.this);
            //this is the url where you want to send the request
            //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

            String url = "http://ingtechbd.com/demo/attendance/insert.php";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response);
                            Toast.makeText(AttendanceActivity.this, "in time registered", Toast.LENGTH_SHORT).show();
                            inTime.setChecked(false);
                            inTime.setEnabled(false);
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
                    params.put("employee_id", employee_id);
                    params.put("in_time",sdf.format(cal.getTime()));
                    params.put("out_time", "");
                    params.put("date", date);
                    params.put("remarks", remarksEdt);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }

        if (outTime.isChecked() && !inTime.isEnabled()) {

            cal1 = Calendar.getInstance();
            sdf1 = new SimpleDateFormat("hh:mm:ss a");

            RequestQueue queues = Volley.newRequestQueue(AttendanceActivity.this);
            //this is the url where you want to send the request
            //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

            String url = "http://ingtechbd.com/demo/attendance/update.php";

            // Request a string response from the provided URL.
            StringRequest stringRequests = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response);
                            Toast.makeText(AttendanceActivity.this, "out time registered", Toast.LENGTH_SHORT).show();
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
                    params.put("out_time", sdf1.format(cal1.getTime()));
                    params.put("employee_id", employee_id);
                    params.put("date", date);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queues.add(stringRequests);

        }
    }

    private void getValue(final String email) {

        RequestQueue queues = Volley.newRequestQueue(AttendanceActivity.this);
        String uri = String.format("http://ingtechbd.com/demo/attendance/getvalue.php?email="+email);
        StringRequest stringRequests = new StringRequest(Request.Method.GET, uri,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject rsp = new JSONObject(response);
                            JSONArray array = rsp.getJSONArray("result");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject users = array.getJSONObject(i);
                                employee_id = users.getString("employee_id");
                                //Toast.makeText(AttendanceActivity.this, ""+emplo, Toast.LENGTH_SHORT).show();
                            }
                            // do your work with response object

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //_response.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queues.add(stringRequests);

    }
}
