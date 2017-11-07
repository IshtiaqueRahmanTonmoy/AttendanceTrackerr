package com.ingeniousat.com.attendancetrackerr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TimePicker;

public class AddReminderActivitySecond extends AppCompatActivity {

    int hour;
    int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder_activity_second);

        Bundle extras = getIntent().getExtras();
        final int year = extras.getInt("year");
        final int month = extras.getInt("month");
        final int day = extras.getInt("day");
        final String date = month + "/" + day + "/" + year;

        setTitle(date);

        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();

                Intent nextStep = new Intent(getApplicationContext(),AddReminderActivity.class);
                nextStep.putExtra("hour", hour);
                nextStep.putExtra("minute", minute);
                nextStep.putExtra("date", date);
                nextStep.putExtra("year", year);
                nextStep.putExtra("month", month);
                nextStep.putExtra("day", day);
                startActivity(nextStep);
                finish();
            }
        });
    }

}
