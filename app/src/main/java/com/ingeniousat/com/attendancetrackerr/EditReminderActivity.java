package com.ingeniousat.com.attendancetrackerr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

public class EditReminderActivity extends AppCompatActivity {

    final static int DIALOG_ID = 0;
    int newyear, newmonth, newday;
    String date, hour, minute, AMPM, time, idString;
    int hourInt, minuteInt, id, month, day, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        EditReminderActivity.this.setTitle("iAttendance");
        Bundle extras = getIntent().getExtras();

        String name = extras.getString("name");
        date = extras.getString("date");
        time = extras.getString("time");
        final String context = extras.getString("context");
        idString = extras.getString("id");
        id = Integer.parseInt(idString);

        String[] dateparts = date.split("/");
        String monthString = dateparts[0];
       String dayString = dateparts[1];
        String yearString = dateparts[2];
        month = Integer.parseInt(monthString);
        day = Integer.parseInt(dayString);
        year = Integer.parseInt(yearString);

        final DBHandler dbhandler = new DBHandler(this,null,null,id);

        setTitle("Edit " + name);

        final EditText editName = (EditText) findViewById(R.id.editName);
        final EditText editDescription = (EditText) findViewById(R.id.editContext);
        final Button changeTimeButton = (Button) findViewById(R.id.changeTimeButton);
        final Button changeDateButton = (Button) findViewById(R.id.changeDateButton);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteImageButton);

        editDescription.setText(context);
        editName.setText(name);
        changeTimeButton.setText(time);
        changeDateButton.setText(date);

        showDateDialogOnButtonClick();

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String updatedname = editName.getText().toString();
                setTitle("Edit " + updatedname);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int newhour, int newminute) {
                String minuteString = String.valueOf(newminute);
                String newAMPM = "AM";
                if (newhour>=13) {
                    newAMPM = "PM";
                    newhour -= 12;
                }

                if (newminute<10){
                    minuteString = "0" + minuteString;
                }
                time = newhour + ":" + minuteString + " " + newAMPM;
                changeTimeButton.setText(time);
            }
        };
        changeTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] parts = time.split(":");
                hour = parts[0];
                String minuteAMPM = parts[1];
                String[] parts2 = minuteAMPM.split(" ");
                minute = parts2[0];
                AMPM = parts2[1];
                hourInt = Integer.parseInt(hour);
                minuteInt = Integer.parseInt(minute);
                if (AMPM.equals("PM")) {
                    hourInt += 12;
                }
                new TimePickerDialog(EditReminderActivity.this, onTimeSetListener, hourInt, minuteInt, false).show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                dbhandler.deleteContent(id);
                dbhandler.close();
                Intent backtoHome = new Intent(getApplicationContext(), ViewingDatesActivity.class);
                backtoHome.putExtra("date", date);
                startActivity(backtoHome);
                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newname = editName.getText().toString();
                String description = editDescription.getText().toString();
                dbhandler.editReminder(id,date,newname,time,description);
                dbhandler.close();
                finish();
            }
        });
    }



    public void showDateDialogOnButtonClick() {
        Button changeDate = (Button) findViewById(R.id.changeDateButton);
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }
        });

    }
    public Dialog onCreateDialog(int id) {
        if (id==DIALOG_ID)
            return new DatePickerDialog(this, dpickerListener, year, month, day);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            newyear = i;
            newmonth = i1 + 1;
            newday = i2;
            date = newmonth + "/" + newday + "/" + newyear;
            Button changeDateButton = (Button) findViewById(R.id.changeDateButton);
            changeDateButton.setText(date);
        }
    };

}


