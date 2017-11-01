package com.ingeniousat.com.attendancetrackerr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity {

    TextView nametext;
    EditText searchview;
    ListView listview;
    Button searchBtn;
    public static final String GETINFO_URL = "http://demo.ingtechbd.com/attendance/getemphistory.php";
    public static final String GETDATE_URL = "http://demo.ingtechbd.com/attendance/getnamebydate.php";
    public static final String GETNAME_URL = "http://demo.ingtechbd.com/attendance/getname.php";
    String in_time,out_time,remarks,date,name,employee_id,status,totaltime;
    private ArrayList<Employee> usersList;
    RecyclerView recyclerView;
    MyAdapter mAdapter;
    private int mYear,month,day,year;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;

    //SharedPreferences preferences;
    //SharedPreferences.Editor editor;
   Button searchdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        //HistoryActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        usersList = new ArrayList<>();

        nametext = (TextView) findViewById(R.id.textview);
        listview = (ListView) findViewById(R.id.listview);
        searchdate = (Button) findViewById(R.id.searhDate);
        searchdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        //recyclerView = (RecyclerView) findViewById(R.id.rv_my_recycler_view);
        //recyclerView.setHasFixedSize(true);

        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);

        searchview = (EditText) findViewById(R.id.searhText);


        searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String value = searchview.getText().toString();
                if(value!=null) {
                    getname(value);
                    //getsearch(value);
                }
                if(searchview.length() == 0){
                    usersList.clear();
                    mAdapter = new MyAdapter(usersList, HistoryActivity.this);
                    listview.setAdapter(mAdapter);
                    //recyclerView.setAdapter(mAdapter);
                    //preferences.edit().clear().commit();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //HistoryActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;

                    updateDisplay();
                }
            };

     private void updateDisplay() {

                   StringBuilder strbuilder =  new StringBuilder()
                            // Month is 0 based so add 1
                            .append(mDay).append("/")
                            .append(mMonth + 1).append("/")
                            .append(mYear).append(" ");
                   String datevalue = strbuilder.toString();
                   getDate(datevalue);
                  /// /Toast.makeText(HistoryActivity.this, ""+strbuilder.toString(), Toast.LENGTH_SHORT).show();
     }

    private void getDate(final String datevalue) {
        Log.d("datevalue",datevalue);
        Toast.makeText(HistoryActivity.this, ""+datevalue, Toast.LENGTH_SHORT).show();
        Uri.Builder builder = Uri.parse(GETDATE_URL).buildUpon();
        builder.appendQueryParameter("date",datevalue);
        String loginUrl=builder.build().toString();
        Log.d("loginurl",loginUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, loginUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.d("response",response.toString());
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray j = jsonObject.getJSONArray("result");
                            //Toast.makeText(HistoryActivity.this, "length"+j.length(), Toast.LENGTH_SHORT).show();
                            for(int i=0;i<j.length();i++){

                                //Toast.makeText(HistoryActivity.this, "name"+name, Toast.LENGTH_SHORT).show();
                                try {
                                    //Getting json object
                                    JSONObject json = j.getJSONObject(i);
                                    Log.d("json",json.toString());
                                    name = json.getString("name");
                                    //Toast.makeText(HistoryActivity.this, ""+name, Toast.LENGTH_SHORT).show();
                                    in_time = json.getString("in_time");
                                    out_time = json.getString("out_time");
                                    remarks = json.getString("remarks");
                                    date = json.getString("date");
                                    status = json.getString("status");
                                    totaltime = json.getString("totaltime");

                                    usersList.add(new Employee(name,in_time,out_time,remarks,date,status,totaltime));
                                    //Toast.makeText(HistoryActivity.this, "name"+name, Toast.LENGTH_SHORT).show();

                                    mAdapter = new MyAdapter(usersList, HistoryActivity.this);
                                    listview.setAdapter(mAdapter);
                                    //usersList.clear();
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
                        Log.d("error", String.valueOf(error));
                        //You can handle error here if you want
                    }
                }){
        };

        RequestQueue requestQueue = Volley.newRequestQueue(HistoryActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
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

        RequestQueue requestQueue = Volley.newRequestQueue(HistoryActivity.this);
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
                                    status = json.getString("status");
                                    totaltime = json.getString("totaltime");

                                    //preferences = PreferenceManager.getDefaultSharedPreferences(HistoryActivity.this);
                                    //name = preferences.getString("Name", "");

                                    //Toast.makeText(HistoryActivity.this, ""+in_time+""+out_time+""+remarks, Toast.LENGTH_SHORT).show();
                                    usersList.add(new Employee(name,in_time,out_time,remarks,date,status,totaltime));
                                    //Toast.makeText(HistoryActivity.this, "name"+name, Toast.LENGTH_SHORT).show();

                                    mAdapter = new MyAdapter(usersList, HistoryActivity.this);
                                    listview.setAdapter(mAdapter);
                                    //usersList.clear();
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

        RequestQueue requestQueue = Volley.newRequestQueue(HistoryActivity.this);
        requestQueue.add(stringRequest);
    }
}


