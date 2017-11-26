package com.ingeniousat.com.attendancetrackerr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

public class ReminderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setTitle("Reminder Calendar");

        Button allRemindersButton = (Button) findViewById(R.id.allRemindersButton);
        Button backtoMainButton = (Button) findViewById(R.id.backToMainActivity);
        allRemindersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAllReminders = new Intent(getApplicationContext(),ViewActivity.class);
                startActivity(toAllReminders);
            }
        });

        backtoMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAllReminders = new Intent(getApplicationContext(),AttendanceActivity.class);
                toAllReminders.putExtra("employeeid",0);
                startActivity(toAllReminders);
            }
        });

        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                Intent intent = new Intent(getApplicationContext(), ViewingDatesActivity.class);
                month += 1;
                String date = month + "/" + day + "/" + year;
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        FloatingActionButton addReminder = (FloatingActionButton) findViewById(R.id.addReminderButton);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addReminder = new Intent(getApplicationContext(),AddReminderFirstActivity.class);
                startActivity(addReminder);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ReminderActivity.this,AttendanceActivity.class);
        intent.putExtra("stringforreminder","1");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

