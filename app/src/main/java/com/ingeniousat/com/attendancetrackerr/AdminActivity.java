package com.ingeniousat.com.attendancetrackerr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    Button employeehistory,addemployee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        employeehistory = (Button) findViewById(R.id.historyBtn);
        addemployee = (Button) findViewById(R.id.addBtns);

        addemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,SignUpActivity.class);
                startActivity(intent);

            }
        });

        employeehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,HistoryActivity.class);
                startActivity(intent);

            }
        });
    }
}
