package com.ingeniousat.com.attendancetrackerr;

import android.content.SharedPreferences;
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
import android.widget.EditText;
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

public class HistoryActivity extends AppCompatActivity {

    EditText searchview;
    Button searchBtn;
    public static final String GETINFO_URL = "http://ingtechbd.com/demo/attendance/getemphistory.php";
    public static final String GETNAME_URL = "http://ingtechbd.com/demo/attendance/getname.php";
    String in_time,out_time,remarks,date,name;
    private ArrayList<Employee> usersList;
    RecyclerView recyclerView;
    MyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        usersList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rv_my_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchview = (EditText) findViewById(R.id.searhText);
        searchBtn = (Button) findViewById(R.id.buttonSearch);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = searchview.getText().toString();
                getsearch(value);
                getname(value);
            }
        });
    }

    private void getsearch(final String value) {
        Uri.Builder builder = Uri.parse(GETINFO_URL).buildUpon();
        builder.appendQueryParameter("employee_id", value);
        String loginUrl=builder.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, loginUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonarray = jsonObject.getJSONArray("result");
                            getInfo(jsonarray,value);

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

        private void getInfo(JSONArray j,String value) {
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

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    name = preferences.getString("Name", "");

                    //Toast.makeText(HistoryActivity.this, ""+in_time+""+out_time+""+remarks, Toast.LENGTH_SHORT).show();
                    usersList.add(new Employee(name,in_time,out_time,remarks,date));
                    //Toast.makeText(HistoryActivity.this, "name"+name, Toast.LENGTH_SHORT).show();

                    mAdapter = new MyAdapter(usersList, this);
                    recyclerView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    private void getname(JSONArray j) {
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                name = json.getString("name");

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HistoryActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Name",name);
                editor.apply();


                //Toast.makeText(HistoryActivity.this, ""+name, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getname(String value) {
        Uri.Builder builder = Uri.parse(GETNAME_URL).buildUpon();
        builder.appendQueryParameter("employee_id", value);
        String loginUrl=builder.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, loginUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonarray = jsonObject.getJSONArray("result");
                            getname(jsonarray);

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


