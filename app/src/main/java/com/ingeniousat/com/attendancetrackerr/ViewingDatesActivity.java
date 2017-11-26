package com.ingeniousat.com.attendancetrackerr;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewingDatesActivity extends AppCompatActivity {

    final public static String ONE_TIME = "onetime";

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_dates);

        ViewingDatesActivity.this.setTitle("iAttendance");
        Bundle extras = getIntent().getExtras();
        String date = extras.getString("date");

        ListView reminderListView = (ListView) findViewById(R.id.listView);

        DBHandler dbHandler = new DBHandler(this,null,null,1);

        ArrayList<Reminder> remindersArray = dbHandler.allReminders();

        List<Map<String, String>> dailyRemindersList = new ArrayList<Map<String, String>>(2);

        for (int i = 0; i < remindersArray.size(); i++) {
            Map<String, String> datum2 = new HashMap<String, String>(2);
            datum2.put("id", Integer.toString(remindersArray.get(i).getID()));
            datum2.put("name", remindersArray.get(i).getName());
            datum2.put("date", remindersArray.get(i).getDate());
            datum2.put("time", remindersArray.get(i).getTime());
            datum2.put("context", remindersArray.get(i).getContext());
            String date2 = datum2.get("date");
            if (date2.equals(date)) {

                dailyRemindersList.add(datum2);
            }
        }


            SimpleAdapter eachDateAdapter = new SimpleAdapter(ViewingDatesActivity.this, dailyRemindersList,R.layout.list_item, new String[]{"name","date","time"}, new int[]{R.id.name,R.id.date,R.id.time});
            reminderListView.setAdapter(eachDateAdapter);

            reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Map<String, String> datum2 = (Map<String, String>) adapterView.getItemAtPosition(i);

                    String name = datum2.get("name");
                    String date = datum2.get("date");
                    String time = datum2.get("time");
                    String context = datum2.get("context");
                    String id = datum2.get("id");
                    Intent intent = new Intent(getApplicationContext(), EditReminderActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putExtra("context", context);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    finish();
                }
            });
    }
    public void onBackPressed() {
        Intent backtoHome = new Intent(getApplicationContext(),ReminderActivity.class);
        startActivity(backtoHome);
        finish();
        return;
    }
}


