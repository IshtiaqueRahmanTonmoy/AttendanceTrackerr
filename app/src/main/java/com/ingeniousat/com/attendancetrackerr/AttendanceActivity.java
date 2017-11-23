package com.ingeniousat.com.attendancetrackerr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.Manifest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.view.MenuItem;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity {

    CheckBox inTime, outTime;
    EditText remarks;
    Button submit,event,report;
    Date datetime;
    DateFormat dateFormat;
    Calendar cal, cal1;
    SimpleDateFormat sdf, sdf1;
    String employee_id;
    String remarksEdt, date, status, totaltime, hour, minute, hourminute, hours, minutes, hourmintues;
    boolean value;
    String employeeid;
    String urlvalue = "http://demo.ingtechbd.com/attendance/getvalue.php";
    long diff, diffMinutes, diffHours;
    SharedPreferences pref,prefs;
    SharedPreferences.Editor edt;
    String currentdate;
    WifiManager wifiManager;
    WifiInfo info;
    private LocationManager locationManager;
    private String provider;
    double latitude,longitude;
    GPSTracker gps;
    AppLocationService appLocationService;
    Button btnGPSShowLocation;
    Button btnNWShowLocation;
    String location,dateget;
    private StringRequest stringRequest;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        dateFormat = new SimpleDateFormat("d/M/yyyy");
        datetime = new Date();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);



        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.event_dashboard:
                                Intent intent = new Intent(AttendanceActivity.this,ReminderActivity.class);
                                startActivity(intent);
                                return true;
                            case R.id.report:
                                Intent intent1 = new Intent(AttendanceActivity.this,ReportActivity.class);
                                intent1.putExtra("empid",employee_id);
                                startActivity(intent1);
                        }
                        return false;
                    }
                });

        inTime = (CheckBox) findViewById(R.id.checkBox1);
        outTime = (CheckBox) findViewById(R.id.checkBox2);
        remarks = (EditText) findViewById(R.id.remarksEdt);

        outTime.setEnabled(false);

        submit = (Button)findViewById(R.id.login_button);


        final String currentdate = dateFormat.format(datetime);

        //Log.d("getthevalue",dateget);
        //Log.d("currentdate",currentdate);

        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("h:mm a");

        gps = new GPSTracker(this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            getAddress(latitude,longitude);
            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        Intent intent = getIntent();
        employee_id = intent.getExtras().getString("employeeid");


        /*
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceActivity.this,ReminderActivity.class);
                startActivity(intent);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceActivity.this,ReportActivity.class);
                intent.putExtra("empid",employee_id);
                startActivity(intent);
            }
        });
        */


        //inTime.setEnabled(true);
        //outTime.setEnabled(false);

        //Toast.makeText(AttendanceActivity.this, ""+easyPuzzle, Toast.LENGTH_SHORT).show();

        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        int getid = pref.getInt("activity_executed",0);
        if(getid == 2){
            inTime.setEnabled(false);
            outTime.setEnabled(true);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int val = preferences.getInt("getouttimevalue", 0);
        if(val == 3)
        {
            inTime.setEnabled(false);
            outTime.setEnabled(false);
        }
        //outTime.setChecked(false);


        getlastdate(new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                //Toast.makeText(AttendanceActivity.this, ""+dateget, Toast.LENGTH_SHORT).show();

                Log.d("dateget",dateget);
                Log.d("currentdate",currentdate);

                if(!dateget.equals(currentdate)){
                    //inTime.setEnabled(true);
                    //outTime.setEnabled(false);
                    //remarks.setEnabled(true);
                }
                else{
                }

            }
        });


    }


    private void getlastdate(final VolleyCallback callback) {
        stringRequest = new StringRequest("http://demo.ingtechbd.com/attendance/getlastdate.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray j = jsonObject.getJSONArray("result");
                            for(int i=0;i<j.length();i++){
                                try {
                                    //Getting json object
                                    JSONObject json = j.getJSONObject(i);
                                    dateget = json.getString("date");
                                    callback.onSuccess(dateget);

                                    //Toast.makeText(AttendanceActivity.this, "dateget"+dateget, Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("response",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AttendanceActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceActivity.this);
        requestQueue.add(stringRequest);


    }

    private void getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address obj = addresses.get(0);
            location = obj.getAddressLine(0);
            Log.d("location",location);
            location = location + "\n" + obj.getCountryName();
            location = location + "\n" + obj.getCountryCode();
            location = location + "\n" + obj.getAdminArea();
            location = location + "\n" + obj.getPostalCode();
            location = location + "\n" + obj.getSubAdminArea();
            location = location + "\n" + obj.getLocality();
            location = location + "\n" + obj.getSubThoroughfare();

            //Toast.makeText(AttendanceActivity.this, ""+location, Toast.LENGTH_SHORT).show();
            Log.v("IGA", "Address" + location);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                AttendanceActivity.this);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        AttendanceActivity.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
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

    private void getEnable(String ssid) {
        Log.d("ssid",ssid.toString());
        //Toast.makeText(AttendanceActivity.this, ""+ssid, Toast.LENGTH_SHORT).show();
        if(ssid.equals(info.getSSID())){
            //Toast.makeText(AttendanceActivity.this, "wifi name"+info.getSSID(), Toast.LENGTH_SHORT).show();
            inTime.setEnabled(true);
            outTime.setEnabled(true);
        }
        else{
            Toast.makeText(AttendanceActivity.this, "other", Toast.LENGTH_SHORT).show();
            inTime.setEnabled(false);
            outTime.setEnabled(false);
        }
    }



    public void OnSubmit(View view) {


        if (inTime.isChecked()) {

            SimpleDateFormat simpleDateFormats = new SimpleDateFormat("HH:mm");
            Date startDate = null;
            Date endDate = null;

            Date dt = new Date();
            int hm = dt.getHours();
            int mm = dt.getMinutes();
            String time = ""+hm+":"+mm;
            try {
                startDate = simpleDateFormats.parse(time);
                //Toast.makeText(AttendanceActivity.this, ""+startDate, Toast.LENGTH_SHORT).show();
                Log.d("startDate", String.valueOf(startDate));
                endDate = simpleDateFormats.parse("10:30");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long difference = endDate.getTime() - startDate.getTime();
            int days = (int) (difference / (1000*60*60*24));
            int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            hourminute = ""+hours+":"+min;
            Log.i("log_tag","Hours: "+hours+", Mins: "+min);


            String t = remarksEdt = remarks.getText().toString();
            date = dateFormat.format(datetime);

            pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
            edt = pref.edit();
            edt.putString("email",employee_id);
            edt.putInt("activity_executed",2);
            edt.putString("intime",sdf.format(cal.getTime()));
            edt.commit();


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
                            Toast.makeText(AttendanceActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                            Toast.makeText(AttendanceActivity.this, "in time registered", Toast.LENGTH_SHORT).show();
                            inTime.setChecked(false);
                            inTime.setEnabled(false);
                            outTime.setEnabled(true);
                            remarks.setEnabled(false);
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
                    params.put("location",location);
                    params.put("status",hourminute);
                    params.put("remarks", remarksEdt);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AttendanceActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("date",date);
                    editor.apply();

                    Log.d("params",params.toString());
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }

        if (outTime.isChecked() && !inTime.isEnabled()) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("getouttimevalue",3);
            editor.apply();

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
                hourmintues = diffHours+"h"+diffMinutes+"m";
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
                            remarks.setText("");
                            submit.setEnabled(false);
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