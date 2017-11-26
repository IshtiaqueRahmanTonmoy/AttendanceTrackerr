package com.ingeniousat.com.attendancetrackerr;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class SignUpActivity extends AppCompatActivity {

    EditText nameEdt,emailEdt,designationEdt,passwordEdt,addressEdt,genderEdt;
    Button submitBtn;
    int i=0;
    int employee_id;
    String name,email,designation,password,address,gender,passwords;
    String url = "http://demo.ingtechbd.com/attendance/register.php";
    private StringRequest stringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getvalue();
        nameEdt = (EditText) findViewById(R.id.name);
        emailEdt = (EditText) findViewById(R.id.email);
        addressEdt = (EditText) findViewById(R.id.address);
        designationEdt = (EditText) findViewById(R.id.desingnation);
        genderEdt = (EditText) findViewById(R.id.gender);
        genderEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(genderEdt);
            }
        });

        passwordEdt = (EditText) findViewById(R.id.password);

        submitBtn = (Button)findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                employee_id++;
                //i++;
                //Random rand = new Random();

                // Generate random integers in range 0 to 999
                //employee_id = Integer.parseInt("100"+i);

                name = nameEdt.getText().toString();
                email = emailEdt.getText().toString();
                address = addressEdt.getText().toString();
                designation = designationEdt.getText().toString();
                gender = genderEdt.getText().toString();
                passwords = passwordEdt.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
                //this is the url where you want to send the request
                //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial



                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("response", response);

                                final String from="ingeniousit18@gmail.com";//change accordingly
                                String password="121212";//change accordingly

//Get the session object
                                Properties props = new Properties();
                                props.put("mail.smtp.host", "smtp.gmail.com");
                                props.put("mail.smtp.socketFactory.port", "465");
                                props.put("mail.smtp.socketFactory.class",
                                        "javax.net.ssl.SSLSocketFactory");
                                props.put("mail.smtp.auth", "true");
                                props.put("mail.smtp.port", "465");

                                Session session = Session.getDefaultInstance(props,
                                        new javax.mail.Authenticator() {
                                            protected PasswordAuthentication getPasswordAuthentication() {
                                                return new PasswordAuthentication(from,"Shantinagar");
                                            }
                                        });

//compose message
                                try {
                                    MimeMessage message = new MimeMessage(session);
                                    message.setFrom(new InternetAddress(from));
                                    message.addRecipient(Message.RecipientType.TO,new InternetAddress(email));
                                    message.setSubject("Login id for employee..");
                                    message.setText("Dear"+"  "+email+"   "+" "+"your login id for attendance is"+employee_id+"and password is"+passwords);
                                    Transport.send(message);
                                }
                                catch (MessagingException e) {throw new RuntimeException(e);}

                                nameEdt.setText("");
                                emailEdt.setText("");
                                addressEdt.setText("");
                                designationEdt.setText("");
                                genderEdt.setText("");
                                passwordEdt.setText("");

                                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                startActivity(intent);
                                //Toast.makeText(SignUpActivity.this, ""+response, Toast.LENGTH_SHORT).show();
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
                        params.put("address",address);
                        params.put("designation", designation);
                        params.put("gender", gender);
                        params.put("password", password);

                        Log.d("params", String.valueOf(params));
                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
    }

    private void getvalue() {
        stringRequest = new StringRequest("http://demo.ingtechbd.com/attendance/getlastid.php",
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
                                employee_id = json.getInt("employee_id");
                                //Toast.makeText(SignUpActivity.this, "employeeid"+employee_id, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(SignUpActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });


        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
        requestQueue.add(stringRequest);

    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(SignUpActivity.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.test, popupMenu.getMenu());

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        genderEdt.setText(item.toString());
                        //Toast.makeText(SignUpActivity.this, item.toString(),
                        //        Toast.LENGTH_LONG).show();
                        return true;
                    }
                });

        popupMenu.show();
    }
}
