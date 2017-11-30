package com.ingeniousat.com.attendancetrackerr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.Context;
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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import android.content.DialogInterface;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.view.LayoutInflater;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class SignUpActivity extends AppCompatActivity {

    EditText nameEdt,emailEdt,designationEdt,passwordEdt,addressEdt,genderEdt,confirmpasswordEdt;
    Button submitBtn, updateBtn;
    TextView idvalue,idoutputvalue;
    int i=0;
    int employee_id;
    String name,email,designation,password,address,gender,passwords,confirmpassword;
    String url = "http://demo.ingtechbd.com/attendance/register.php";
    public static final String GETINFO_URL = "http://demo.ingtechbd.com/attendance/getinformationemployee.php";
    private StringRequest stringRequest;
    Context context;
    private ProgressDialog progressDialog;
    String employeeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
        employeeid = intent.getExtras().getString("employeeid");

        context = this;
        SignUpActivity.this.setTitle("iAttendance");
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

        idoutputvalue = (TextView) findViewById(R.id.idvalue);
        idoutputvalue.setText(employeeid);

        idvalue = (TextView) findViewById(R.id.id);

        passwordEdt = (EditText) findViewById(R.id.password);
        confirmpasswordEdt = (EditText) findViewById(R.id.confirmpassword);
        submitBtn = (Button)findViewById(R.id.submit);
        updateBtn = (Button) findViewById(R.id.update);

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
                                progressDialog.dismiss();
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
                        params.put("password", passwords);

                        Log.d("params", String.valueOf(params));
                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
                progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("Registering please wait....");
                progressDialog.show();
            }
        });


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameEdt.getText().toString();
                email = emailEdt.getText().toString();
                address = addressEdt.getText().toString();
                designation = designationEdt.getText().toString();
                gender = genderEdt.getText().toString();
                password = passwordEdt.getText().toString();
                confirmpassword = confirmpasswordEdt.getText().toString();

                if(!password.equals(confirmpassword)){
                    Toast.makeText(SignUpActivity.this, "password and confirm password do not match..", Toast.LENGTH_SHORT).show();
                }
                else{
                  update(name,email,address,designation,gender,password,confirmpassword);
                }
                //Toast.makeText(SignUpActivity.this, ""+value+""+idoutputvalue.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void update(final String name, final String email, final String address, final String designation, final String gender, final String password, String confirmpassword) {
        RequestQueue queues = Volley.newRequestQueue(SignUpActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        String url = "http://demo.ingtechbd.com/attendance/updateuser.php";

        // Request a string response from the provided URL.
        StringRequest stringRequests = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(SignUpActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        Toast.makeText(SignUpActivity.this, "updated informations", Toast.LENGTH_SHORT).show();

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
                            message.setSubject("password change for employee..");
                            message.setText("Dear"+"  "+email+"   "+" new password is "+" "+passwords);
                            Transport.send(message);
                        }
                        catch (MessagingException e) {throw new RuntimeException(e);}


                        progressDialog.dismiss();
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
                params.put("employee_id", idoutputvalue.getText().toString());
                params.put("name",name);
                params.put("email", email);
                params.put("address", address);
                params.put("designation",designation);
                params.put("gender",gender);
                params.put("password",password);
                Log.d("paramsss",params.toString());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queues.add(stringRequests);
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Updating Please wait....");
        progressDialog.show();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        submitBtn.setEnabled(false);
        int id = item.getItemId();
        if (id == R.id.action_update) {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.prompts, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text

                                    String value = userInput.getText().toString();
                                    Toast.makeText(SignUpActivity.this, ""+value, Toast.LENGTH_SHORT).show();
                                    getAllInfo(value);
                                    //result.setText(userInput.getText());

                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAllInfo(String value) {

        idoutputvalue.setText(value);
        Uri.Builder builder = Uri.parse(GETINFO_URL).buildUpon();
        builder.appendQueryParameter("employee_id",value);
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

                                        String ename = json.getString("name");
                                        String eemail = json.getString("email");
                                        String eaddress = json.getString("address");
                                        String edesignation = json.getString("designation");
                                        String egender = json.getString("gender");

                                        nameEdt.setText(ename);
                                        emailEdt.setText(eemail);
                                        addressEdt.setText(eaddress);
                                        designationEdt.setText(edesignation);
                                        genderEdt.setText(egender);

                                        progressDialog.dismiss();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(SignUpActivity.this, "record on this date not found", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
        requestQueue.add(stringRequest);
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Fetching data....");
        progressDialog.show();

    }

    private static String getmd5Hash(String epassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(epassword.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String md5 = number.toString(16);

            while (md5.length() < 32)
                md5 = "0" + md5;

            return md5;
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", e.getLocalizedMessage());
            return null;
        }
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
