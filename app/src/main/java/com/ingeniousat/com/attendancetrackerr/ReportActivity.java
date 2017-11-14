package com.ingeniousat.com.attendancetrackerr;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity implements DateRangePickerFragment.OnDateRangeSelectedListener{

    SharedPreferences pref;
    EditText searhText;
    ListView listview;
    TextView nametext;
    MyAdapter mAdapter;
    private ArrayList<Employee> usersList;
    public static final String GETNAME_URL = "http://demo.ingtechbd.com/attendance/getname.php";
    public static final String GETINFO_URL = "http://demo.ingtechbd.com/attendance/getemphistory.php";
    public static final String GETDATEPICKER_URL = "http://demo.ingtechbd.com/attendance/getbydatepicker.php";
    String in_time,out_time,remarks,date,name,employee_id,location,status,totaltime;
    CheckBox searchybydate,searchbyid;
    String empid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        usersList = new ArrayList<Employee>();
        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        empid = pref.getString("empid",null);

        listview = (ListView) findViewById(R.id.listview);
        nametext = (TextView) findViewById(R.id.textview);
        searhText = (EditText) findViewById(R.id.searhText);
        searchybydate = (CheckBox) findViewById(R.id.searchbydate);
        searchbyid = (CheckBox) findViewById(R.id.searchbyempid);
        searhText.setText(empid);
        searhText.setTextIsSelectable(false);

        if(empid!=null) {
            getname(empid);
            searchybydate.setEnabled(true);
            searchbyid.setEnabled(false);
            //getsearch(value);
        }
        if(searhText.length() == 0){
            usersList.clear();
            mAdapter = new MyAdapter(ReportActivity.this,R.layout.activity_history,usersList);
            listview.setAdapter(mAdapter);
        }


        searchybydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchybydate.isChecked()){
                    searchbyid.setChecked(false);

                    DateRangePickerFragment dateRangePickerFragment= DateRangePickerFragment.newInstance(ReportActivity.this,false);
                    dateRangePickerFragment.show(getSupportFragmentManager(),"datePicker");

                    searhText.setText("");
                    searhText.setHint("pick date");
                    usersList.clear();
                    mAdapter = new MyAdapter(ReportActivity.this,R.layout.activity_history,usersList);
                    listview.setAdapter(mAdapter);
                }
            }
        });


        searchbyid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchbyid.isChecked()){
                    searchybydate.setChecked(false);
                    searhText.setText("");
                    searhText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    searhText.setHint("employee id");
                    searhText.setText(empid);

                    usersList.clear();
                    mAdapter = new MyAdapter(ReportActivity.this,R.layout.activity_history,usersList);
                    listview.setAdapter(mAdapter);

                    searhText.setFocusableInTouchMode(false);
                    searhText.setClickable(false);
                    searhText.setFocusable(false);
                    String emplid = searhText.getText().toString();
                    getname(emplid);
                    //Toast.makeText(ReportActivity.this, ""+emplid, Toast.LENGTH_SHORT).show();
                }
            }
        });
       // searchybydate.setEnabled(false);
    }

    private void getname(final String value) {

        Uri.Builder builder = Uri.parse(GETNAME_URL).buildUpon();
        builder.appendQueryParameter("employee_id", value);
        String loginUrl=builder.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, loginUrl,
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
                                    name = json.getString("name");
                                    nametext.setText(name);
                                    getsearch(value,name);
                                    //Toast.makeText(HistoryActivity.this, "name is"+name, Toast.LENGTH_SHORT).show();
                                    //preferences = PreferenceManager.getDefaultSharedPreferences(HistoryActivity.this);
                                    //editor = preferences.edit();
                                    //editor.putString("Name",name);
                                    //editor.apply();


                                    //Toast.makeText(HistoryActivity.this, ""+name, Toast.LENGTH_SHORT).show();
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
                        //You can handle error here if you want
                    }
                }){
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ReportActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getsearch(String value, final String name) {
        Uri.Builder builder = Uri.parse(GETINFO_URL).buildUpon();
        builder.appendQueryParameter("employee_id", value);
        String loginUrl=builder.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, loginUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray j = jsonObject.getJSONArray("result");
                            //Toast.makeText(HistoryActivity.this, "empid"+value, Toast.LENGTH_SHORT).show();
                            for(int i=0;i<j.length();i++){

                                //Toast.makeText(HistoryActivity.this, "name"+name, Toast.LENGTH_SHORT).show();
                                try {
                                    //Getting json object
                                    JSONObject json = j.getJSONObject(i);
                                    in_time = json.getString("in_time");
                                    out_time = json.getString("out_time");
                                    remarks = json.getString("remarks");
                                    date = json.getString("date");
                                    location = json.getString("location");
                                    status = json.getString("status");
                                    totaltime = json.getString("totaltime");

                                    Log.d("in_time",in_time);
                                    usersList.add(new Employee(name,in_time,out_time,remarks,date,location,status,totaltime));

                                    mAdapter = new MyAdapter(ReportActivity.this,R.layout.activity_history,usersList);
                                    listview.setAdapter(mAdapter);


                                    listview.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {

                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(searhText.getWindowToken(), 0);

                                            return false;
                                        }
                                    });
                                    mAdapter.notifyDataSetChanged();


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
                        //You can handle error here if you want
                    }
                }){
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ReportActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        Log.d("range : ","from: "+startDay+"-"+startMonth+"-"+startYear+" to : "+endDay+"-"+endMonth+"-"+endYear );

        String firstdate = startDay+"/"+startMonth+"/"+startYear;
        String lastdate = endDay+"/"+endMonth+"/"+endYear;

        searhText.setText(firstdate+"-"+lastdate);

        //Toast.makeText(HistoryActivity.this, ""+datevalue, Toast.LENGTH_SHORT).show();
        Uri.Builder builder = Uri.parse(GETDATEPICKER_URL).buildUpon();
        builder.appendQueryParameter("firstdate",firstdate);
        builder.appendQueryParameter("lastdate",lastdate);
        String loginUrl=builder.build().toString();
        Log.d("loginurl",loginUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, loginUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("response", response.toString());
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray j = jsonObject.getJSONArray("result");
                            //Toast.makeText(HistoryActivity.this, "length"+j.length(), Toast.LENGTH_SHORT).show();
                            if (j != null && j.length() > 0){

                                for (int i = 0; i < j.length(); i++) {
                                    try {
                                        //Getting json object
                                        JSONObject json = j.getJSONObject(i);
                                        Log.d("json", json.toString());
                                        name = json.getString("name");
                                        //Toast.makeText(HistoryActivity.this, ""+name, Toast.LENGTH_SHORT).show();
                                        in_time = json.getString("in_time");
                                        out_time = json.getString("out_time");
                                        remarks = json.getString("remarks");
                                        date = json.getString("date");
                                        location = json.getString("location");
                                        status = json.getString("status");
                                        totaltime = json.getString("totaltime");

                                        usersList.add(new Employee(name, in_time, out_time, remarks, date, location,status, totaltime));
                                        //Toast.makeText(HistoryActivity.this, "name"+name, Toast.LENGTH_SHORT).show();

                                        mAdapter = new MyAdapter(ReportActivity.this,R.layout.activity_history,usersList);
                                        listview.setAdapter(mAdapter);
                                        searchbyid.setEnabled(true);

                                        listview.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {

                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(searhText.getWindowToken(), 0);

                                                return false;
                                            }
                                        });
                                        //usersList.clear();
                                        mAdapter.notifyDataSetChanged();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            else{
                                Toast.makeText(ReportActivity.this, "record on this date not found", Toast.LENGTH_SHORT).show();
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
                        Log.d("error", String.valueOf(error));
                        //You can handle error here if you want
                    }
                }){
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ReportActivity.this);
        requestQueue.add(stringRequest);
    }
}
