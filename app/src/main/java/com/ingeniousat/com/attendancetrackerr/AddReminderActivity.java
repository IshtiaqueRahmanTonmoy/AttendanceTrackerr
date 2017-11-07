package com.ingeniousat.com.attendancetrackerr;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddReminderActivity extends AppCompatActivity {

    String time;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        final LayoutInflater layoutInflater = this.getLayoutInflater();

        final EditText nameEditText = (EditText) findViewById(R.id.editName);
        final EditText contextEditText = (EditText) findViewById(R.id.editContext);
        Bundle extras = getIntent().getExtras();
        final int year = extras.getInt("year");
        final int month = extras.getInt("month");
        final int day = extras.getInt("day");
        int hour = extras.getInt("hour");
        int minute = extras.getInt("minute");
        String minuteString = String.valueOf(minute);
        String AMPM = "";

        if (hour>=13) {
            AMPM = "PM";
            hour -= 12;
        }
        if (hour <= 12) {
            AMPM = "AM";
        }

        if (minute<10){
            minuteString = "0" + minuteString;
        }

        date = month + "/" + day + "/" + year;
        time = hour + ":" + minuteString + " " + AMPM;

        setTitle(date + "                      " + time);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String context = contextEditText.getText().toString();

                if (name.equals("")){
                    AlertDialog.Builder nameDialogBuilder = new AlertDialog.Builder(AddReminderActivity.this);
                    nameDialogBuilder.setCancelable(false);
                    View nameDialogView = layoutInflater.inflate(R.layout.alert_layout, null);
                    nameDialogBuilder.setTitle("Please Enter A Name").setView(nameDialogView);
                    nameDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    nameDialogBuilder.show();
                }

                else {
                    newReminder(name, date, time, context);
                }
            }
        });
    }

    public void newReminder (String name, String date, String time, String context) {
        DBHandler dbhandler = new DBHandler(this,null,null,1);

        Reminder reminder = new Reminder(date, name, time, context);

        dbhandler.addReminder(reminder);
        dbhandler.close();

        Intent backtoViewDate = new Intent (getApplicationContext(),ViewingDatesActivity.class);
        backtoViewDate.putExtra("date", date);

        startActivity(backtoViewDate);
        finish();

    }
}
