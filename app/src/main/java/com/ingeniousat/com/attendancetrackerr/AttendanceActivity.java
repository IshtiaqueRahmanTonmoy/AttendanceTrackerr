package com.ingeniousat.com.attendancetrackerr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.ParseException;
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
    String remarksEdt,date,status,totaltime,hour,minute,hourminute,hours,minutes,hourmintues;
    boolean value;
    String employeeid;
    String urlvalue = "http://demo.ingtechbd.com/attendance/getvalue.php";
    long diff,diffMinutes,diffHours;
    SharedPreferences pref;
    SharedPreferences.Editor edt;
    String currentdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        inTime = (CheckBox) findViewById(R.id.checkBox1);
        outTime = (CheckBox) findViewById(R.id.checkBox2);

        Boolean value = isNetAvailable(AttendanceActivity.this);
        if(value == true){
            WifiManager wifiManager = (WifiManager) getSystemService (Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo ();
            String ssid  = info.getSSID();
            //Log.d("ssid",ssid);

            if(ssid.equals("TP-LINK_F20C")) {
                inTime.setEnabled(true);
                outTime.setEnabled(true);
            }
        }
        else{
            inTime.setEnabled(false);
            outTime.setEnabled(false);
        }
        //Toast.makeText(AttendanceActivity.this, ""+value, Toast.LENGTH_SHORT).show();

        dateFormat = new SimpleDateFormat("d/M/yyyy");
        datetime = new Date();



        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("hh:mm a");

        Intent intent = getIntent();
        employee_id = intent.getExtras().getString("employeeid");

        //Toast.makeText(AttendanceActivity.this, ""+easyPuzzle, Toast.LENGTH_SHORT).show();

        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        int getid = pref.getInt("activity_executed",0);
        if(getid == 2){
            inTime.setEnabled(false);
        }

        outTime.setChecked(false);
        remarks = (EditText) findViewById(R.id.remarksEdt);

    }

    public Boolean isNetAvailable(Context con) {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if(wifiInfo.isConnected()) {
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void OnSubmit(View view) {

        if (inTime.isChecked()) {

            String t = remarksEdt = remarks.getText().toString();
            date = dateFormat.format(datetime);

            pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
            edt = pref.edit();
            edt.putString("email",employee_id);
            edt.putInt("activity_executed",2);
            edt.putString("intime",sdf.format(cal.getTime()));
            edt.commit();

            try {
                Date d1 = sdf.parse(sdf.format(cal.getTime()));
                Date d2 = sdf.parse("10:30 AM");
                Log.d("intime", String.valueOf(d1));
                Log.d("outtime", String.valueOf(d2));
                diff = d2.getTime() - d1.getTime();
                diffHours = Math.abs(diff / (60 * 60 * 1000) % 24);
                diffMinutes = Math.abs(diff/(60 * 1000) % 60);
                hour = String.valueOf(diffHours);
                minute = String.valueOf(diffMinutes);
                hourminute = ""+hour+":"+minute+"";
                //Log.d("hourminute",hourminute);
                //Toast.makeText(AttendanceActivity.this, ""+hourminute, Toast.LENGTH_SHORT).show();
                //Toast.makeText(AttendanceActivity.this, ""+diffHours+""+diffMinutes, Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            RequestQueue queue = Volley.newRequestQueue(AttendanceActivity.this);
                //this is the url where you want to send the request
                //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

                String url = "http://demo.ingtechbd.com/attendance/insert.php";

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
                        params.put("in_time", sdf.format(cal.getTime()));
                        params.put("out_time", "");
                        params.put("date", date);
                        params.put("status", hourminute);
                        params.put("remarks", remarksEdt);
                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }

            if (outTime.isChecked() && !inTime.isEnabled()) {

                date = dateFormat.format(datetime);
                pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                String intime = pref.getString("intime",null);
                //Toast.makeText(AttendanceActivity.this, ""+intime, Toast.LENGTH_SHORT).show();

                cal1 = Calendar.getInstance();
                sdf1 = new SimpleDateFormat("hh:mm a");

                try {
                    Date d1 = sdf.parse(intime);
                    //Toast.makeText(AttendanceActivity.this, "date d1"+d1, Toast.LENGTH_SHORT).show();
                    Date d2 = sdf.parse(sdf1.format(cal1.getTime()));
                    Log.d("intime", String.valueOf(d1));
                    Log.d("outtime", String.valueOf(d2));
                    diff = d2.getTime() - d1.getTime();
                    Log.d("differenece", String.valueOf(diff));
                    //Toast.makeText(AttendanceActivity.this, ""+diff, Toast.LENGTH_SHORT).show();
                    diffHours = Math.abs(diff / (60 * 60 * 1000) % 24);
                    diffMinutes = Math.abs(diff/(60 * 1000) % 60);
                    hours = String.valueOf(diffHours);
                    minutes = String.valueOf(diffMinutes);
                    hourmintues = ""+diffHours+"Hour"+":"+""+diffMinutes+""+"Minute";
                    Log.d("totaltimeofhourandime",hourmintues);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                RequestQueue queues = Volley.newRequestQueue(AttendanceActivity.this);
                //this is the url where you want to send the request
                //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

                String url = "http://demo.ingtechbd.com/attendance/update.php";

                // Request a string response from the provided URL.
                StringRequest stringRequests = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("response", response);
                                outTime.setEnabled(false);
                                Toast.makeText(AttendanceActivity.this, "out time registered", Toast.LENGTH_SHORT).show();
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
                        //Log.d("allresultvalue",sdf1.format(cal1.getTime())+hourmintues+employee_id+date);
                        params.put("out_time", sdf1.format(cal1.getTime()));
                        params.put("totaltime", hourmintues);
                        params.put("employee_id", employee_id);
                        params.put("date", date);
                        Log.d("params",params.toString());
                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queues.add(stringRequests);

            }
       }

    private String getWifiName(Context context) {

        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.getSSID();
                }
            }
        }
        return null;

    }

    private void getValue(final String email) {

        RequestQueue queues = Volley.newRequestQueue(AttendanceActivity.this);
        String uri = String.format("http://demo.ingtechbd.com/attendance/getvalue.php?email="+email);
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
