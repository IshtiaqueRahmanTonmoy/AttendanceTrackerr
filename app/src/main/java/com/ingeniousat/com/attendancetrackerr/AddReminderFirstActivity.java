package com.ingeniousat.com.attendancetrackerr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;

public class AddReminderFirstActivity extends AppCompatActivity {

    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder_first);

        AddReminderFirstActivity.this.setTitle("iAttendance");
        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                year = datePicker.getYear();
                month = datePicker.getMonth() + 1;
                day = datePicker.getDayOfMonth();

                Intent nextStep = new Intent(getApplicationContext(), AddReminderActivitySecond.class);
                nextStep.putExtra("day", day);
                nextStep.putExtra("month", month);
                nextStep.putExtra("year", year);
                startActivity(nextStep);
                finish();
            }
        });
    }

}
