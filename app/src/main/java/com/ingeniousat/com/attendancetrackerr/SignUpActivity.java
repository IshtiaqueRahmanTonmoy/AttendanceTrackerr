package com.ingeniousat.com.attendancetrackerr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SignUpActivity extends AppCompatActivity {

    EditText nameEdt,emailEdt,designationEdt,passwordEdt;
    Button submitBtn;
    int i=0;
    int employee_id;
    String name,email,designation,password;
    String url = "http://ingtechbd.com/demo/attendance/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameEdt = (EditText) findViewById(R.id.name);
        emailEdt = (EditText) findViewById(R.id.email);
        designationEdt = (EditText) findViewById(R.id.desingnation);
        passwordEdt = (EditText) findViewById(R.id.password);

        submitBtn = (Button)findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i++;
                Random rand = new Random();

                // Generate random integers in range 0 to 999
                employee_id = rand.nextInt(1000+i);

                name = nameEdt.getText().toString();
                email = emailEdt.getText().toString();
                designation = designationEdt.getText().toString();
                password = passwordEdt.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
                //this is the url where you want to send the request
                //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial



                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("response", response);
                                Toast.makeText(SignUpActivity.this, ""+response, Toast.LENGTH_SHORT).show();
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

                        params.put("employee_id", String.valueOf(employee_id));
                        params.put("name",name);
                        params.put("email",email);
                        params.put("designation", designation);
                        params.put("password", password);

                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
    }
}
