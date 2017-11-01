package com.ingeniousat.com.attendancetrackerr;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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

    EditText nameEdt,emailEdt,designationEdt,passwordEdt;
    Button submitBtn;
    int i=0;
    int employee_id;
    String name,email,designation,password;
    String url = "http://demo.ingtechbd.com/attendance/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
                                    message.setText("Dear"+"  "+email+"   "+" "+"your login id for attendance is"+employee_id+"and password is"+password);
                                    Transport.send(message);
                                }
                                catch (MessagingException e) {throw new RuntimeException(e);}

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
