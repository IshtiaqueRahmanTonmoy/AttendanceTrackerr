package com.ingeniousat.com.attendancetrackerr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.StringTokenizer;

public class HistoryActivity extends AppCompatActivity  implements DateRangePickerFragment.OnDateRangeSelectedListener,View.OnClickListener{

    TextView nametext;
    EditText searchview;
    ListView listview;
    Button searchBtn;
    public static final String GETINFO_URL = "http://demo.ingtechbd.com/attendance/getemphistory.php";
    public static final String GETDATE_URL = "http://demo.ingtechbd.com/attendance/getnamebydate.php";
    public static final String GETDATEPICKER_URL = "http://demo.ingtechbd.com/attendance/getbydatepicker.php";
    public static final String GETNAME_URL = "http://demo.ingtechbd.com/attendance/getname.php";
    String in_time,out_time,remarks,date,name,employee_id,status,totaltime,location;
    private ArrayList<Employee> usersList;
    RecyclerView recyclerView;
    MyAdapter mAdapter;
    private int mYear,month,day,year;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;
    CheckBox searchbydate,searchybyid;
    Button searchdate,search;
    RadioGroup radioGroup;
    private RadioButton radioButton;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        //HistoryActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        usersList = new ArrayList<>();

        nametext = (TextView) findViewById(R.id.textview);
        listview = (ListView) findViewById(R.id.listview);
        search = (Button) findViewById(R.id.searchButton);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        searchview = (EditText) findViewById(R.id.searhText);
        searchview.setFocusable(false);
        searchview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchview.setFocusableInTouchMode(true);
                searchview.requestFocus();
                return false;
            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.rgOpinion);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId) {
                    case R.id.searchbyempid:
                        searchview.setText("");
                        usersList.clear();
                        mAdapter = new MyAdapter(HistoryActivity.this,R.layout.activity_history,usersList);
                        listview.setAdapter(mAdapter);
                        searchview.setFocusableInTouchMode(true);
                        searchview.requestFocus();
                        //Toast.makeText(getApplicationContext(), "id", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.searchbydate:
                        searchview.setText("");
                        usersList.clear();
                        mAdapter = new MyAdapter(HistoryActivity.this,R.layout.activity_history,usersList);
                        listview.setAdapter(mAdapter);
                        searchview.setFocusableInTouchMode(true);
                        searchview.requestFocus();
                        DateRangePickerFragment dateRangePickerFragment= DateRangePickerFragment.newInstance(HistoryActivity.this,false);
                        dateRangePickerFragment.show(getSupportFragmentManager(),"datePicker");

                        //Toast.makeText(getApplicationContext(), "date", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });


        searchview.setOnClickListener(this);
        search.setOnClickListener(this);

        //searchbydate = (CheckBox) findViewById(R.id.searchbyempid);
        //searchybyid = (CheckBox) findViewById(R.id.searchbydate);



        /*
        searchbydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchbydate.isChecked()){
                    searchview.setText("");
                    searchview.setHint("search by id");
                    searchybyid.setChecked(false);
                    searchview.setFocusableInTouchMode(true);
                    searchview.requestFocus();
                    searchview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String value = searchview.getText().toString();
                            if(value!=null) {
                                getname(value);
                                //getsearch(value);
                            }
                            if(searchview.length() == 0){
                                usersList.clear();
                                mAdapter = new MyAdapter(HistoryActivity.this,R.layout.activity_history,usersList);
                                listview.setAdapter(mAdapter);
                            }
                        }
                    });
                }
            }
        });

        searchybyid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchybyid.isChecked()){
                    searchview.setText("");
                    searchview.setHint("search by date");
                    searchbydate.setChecked(false);
                    searchview.setFocusableInTouchMode(true);
                    searchview.requestFocus();
                    searchview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            DateRangePickerFragment dateRangePickerFragment= DateRangePickerFragment.newInstance(HistoryActivity.this,false);
                            dateRangePickerFragment.show(getSupportFragmentManager(),"datePicker");
                            //showDialog(DATE_DIALOG_ID);
                        }
                    });
                }
            }
        });
        */

        /*
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
                    mAdapter = new MyAdapter(HistoryActivity.this,R.layout.activity_history,usersList);
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
        */

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
        //Toast.makeText(HistoryActivity.this, ""+datevalue, Toast.LENGTH_SHORT).show();
        Uri.Builder builder = Uri.parse(GETDATE_URL).buildUpon();
        builder.appendQueryParameter("date",datevalue);
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

                                        usersList.add(new Employee(name, in_time, out_time, remarks, date, location, status, totaltime));
                                        //Toast.makeText(HistoryActivity.this, "name"+name, Toast.LENGTH_SHORT).show();

                                        mAdapter = new MyAdapter(HistoryActivity.this,R.layout.activity_history,usersList);
                                        listview.setAdapter(mAdapter);


                                        listview.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {

                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(searchview.getWindowToken(), 0);

                                                return false;
                                            }
                                        });

                                        mAdapter.notifyDataSetChanged();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            else{
                                Toast.makeText(HistoryActivity.this, "record on this date not found", Toast.LENGTH_SHORT).show();
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
                                    location = json.getString("location");
                                    status = json.getString("status");
                                    totaltime = json.getString("totaltime");

                                    //preferences = PreferenceManager.getDefaultSharedPreferences(HistoryActivity.this);
                                    //name = preferences.getString("Name", "");

                                    //Toast.makeText(HistoryActivity.this, ""+in_time+""+out_time+""+remarks, Toast.LENGTH_SHORT).show();
                                    usersList.add(new Employee(name,in_time,out_time,remarks,date,location,status,totaltime));
                                    //Toast.makeText(HistoryActivity.this, "name"+name, Toast.LENGTH_SHORT).show();

                                    mAdapter = new MyAdapter(HistoryActivity.this,R.layout.activity_history,usersList);
                                    listview.setAdapter(mAdapter);
                                    progressDialog.dismiss();

                                    listview.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {

                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(searchview.getWindowToken(), 0);

                                            return false;
                                        }
                                    });
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
        progressDialog = new ProgressDialog(HistoryActivity.this);
        progressDialog.setMessage("Fetching data....");
        progressDialog.show();
    }

    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        Log.d("range : ","from: "+startDay+"-"+startMonth+"-"+startYear+" to : "+endDay+"-"+endMonth+"-"+endYear );

        String firstdate = startDay+"/"+startMonth+"/"+startYear;
        String lastdate = endDay+"/"+endMonth+"/"+endYear;

        searchview.setText(firstdate+"-"+lastdate);

        /*
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

                                        usersList.add(new Employee(name, in_time, out_time, remarks, date,location, status, totaltime));
                                        //Toast.makeText(HistoryActivity.this, "name"+name, Toast.LENGTH_SHORT).show();

                                        mAdapter = new MyAdapter(HistoryActivity.this,R.layout.activity_history,usersList);
                                        listview.setAdapter(mAdapter);
                                        listview.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {

                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(searchview.getWindowToken(), 0);

                                                return false;
                                            }
                                        });

                                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                //String selectedSweet = listview.getItemAtPosition(position).toString();

                                                TextView textView = (TextView) view.findViewById(R.id.locationValue);
                                                String text = textView.getText().toString();
                                                Toast.makeText(getApplicationContext(), "Selected item: " + text , Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(HistoryActivity.this, "record on this date not found", Toast.LENGTH_SHORT).show();
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
        */
    }

    @Override
    public void onClick(View view) {

        if (view == search) {
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            radioButton = (RadioButton) findViewById(selectedId);

            //Toast.makeText(HistoryActivity.this,
            //        radioButton.getText(), Toast.LENGTH_SHORT).show();
            if(radioButton.getText().equals("Search by employee id")){
                String value = searchview.getText().toString();
                if(value!=null) {
                    getname(value);
                    //getsearch(value);
                }
                if(searchview.length() == 0){
                    usersList.clear();
                    mAdapter = new MyAdapter(HistoryActivity.this,R.layout.activity_history,usersList);
                    listview.setAdapter(mAdapter);
                }
            }
            else if(radioButton.getText().equals("Search by date")){
                 String text = searchview.getText().toString();
                 StringTokenizer st = new StringTokenizer(text, "-");
                 String firstdate = st.nextToken();
                 String lastdate = st.nextToken();
                 getvalue(firstdate,lastdate);

                 //Toast.makeText(HistoryActivity.this, "firstdate"+firstdate+"secondate"+secondate, Toast.LENGTH_SHORT).show();
                 //DateRangePickerFragment dateRangePickerFragment= DateRangePickerFragment.newInstance(HistoryActivity.this,false);
                //dateRangePickerFragment.show(getSupportFragmentManager(),"datePicker");
            }

        }

            //Log.d("opinion",opinion);
            //Toast.makeText(this, "Your Opinion is : " + opinion, Toast.LENGTH_LONG).show(); }
     }

    private void getvalue(String firstdate, String lastdate) {
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

                                        usersList.add(new Employee(name, in_time, out_time, remarks, date,location, status, totaltime));
                                        //Toast.makeText(HistoryActivity.this, "name"+name, Toast.LENGTH_SHORT).show();

                                        mAdapter = new MyAdapter(HistoryActivity.this,R.layout.activity_history,usersList);
                                        listview.setAdapter(mAdapter);
                                        progressDialog.dismiss();

                                        listview.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {

                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(searchview.getWindowToken(), 0);

                                                return false;
                                            }
                                        });

                                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                //String selectedSweet = listview.getItemAtPosition(position).toString();

                                                TextView textView = (TextView) view.findViewById(R.id.locationValue);
                                                String text = textView.getText().toString();
                                                Toast.makeText(getApplicationContext(), "Selected item: " + text , Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(HistoryActivity.this, "record on this date not found", Toast.LENGTH_SHORT).show();
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
        progressDialog = new ProgressDialog(HistoryActivity.this);
        progressDialog.setMessage("Fetching data....");
        progressDialog.show();
    }
}

