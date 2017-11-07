package com.ingeniousat.com.attendancetrackerr;

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

public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        ListView reminderListView = (ListView) findViewById(R.id.reminderListView);

        DBHandler dbHandler = new DBHandler(this,null,null,1);

        ArrayList<Reminder> remindersArray = dbHandler.allReminders();

        List<Map<String, String>> allRemindersList = new ArrayList<Map<String, String>>(2);

        for (int i = 0; i < remindersArray.size(); i++) {
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("id", Integer.toString(remindersArray.get(i).getID()));
            datum.put("name", remindersArray.get(i).getName());
            datum.put("context", remindersArray.get(i).getContext());
            datum.put("date", remindersArray.get(i).getDate());
            datum.put("time", remindersArray.get(i).getTime());
            allRemindersList.add(datum);
        }

        SimpleAdapter allRemindersAdapter = new SimpleAdapter(ViewActivity.this, allRemindersList, android.R.layout.simple_list_item_1, new String[]{"name"}, new int[] {android.R.id.text1});
        reminderListView.setAdapter(allRemindersAdapter);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, String> datum = (Map<String, String>) adapterView.getItemAtPosition(i);
                String id = datum.get("id");
                String name = datum.get("name");
                String context = datum.get("context");
                String date = datum.get("date");
                String time = datum.get("time");
                Intent intent = new Intent(getApplicationContext(), EditReminderActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("context", context);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                startActivity(intent);
                finish();
            }
        });
    }
}
