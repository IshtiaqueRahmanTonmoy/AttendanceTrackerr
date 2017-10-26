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
    //SharedPreferences preferences;
    //SharedPreferences.Editor editor;

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
                    recyclerView.setAdapter(mAdapter);
                    //preferences.edit().clear().commit();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

                                    //preferences = PreferenceManager.getDefaultSharedPreferences(HistoryActivity.this);
                                    //name = preferences.getString("Name", "");

                                    //Toast.makeText(HistoryActivity.this, ""+in_time+""+out_time+""+remarks, Toast.LENGTH_SHORT).show();
                                    usersList.add(new Employee(name,in_time,out_time,remarks,date));
                                    //Toast.makeText(HistoryActivity.this, "name"+name, Toast.LENGTH_SHORT).show();

                                    mAdapter = new MyAdapter(usersList, HistoryActivity.this);
                                    recyclerView.setAdapter(mAdapter);
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


