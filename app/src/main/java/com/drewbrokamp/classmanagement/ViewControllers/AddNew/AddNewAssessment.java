package com.drewbrokamp.classmanagement.ViewControllers.AddNew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.MainActivity;
import com.drewbrokamp.classmanagement.Model.Assessment;
import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.Util.NotificationReceiver;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddNewAssessment extends AppCompatActivity {

    private DBHelper dbhelper;
    private Assessment assessment;

    private EditText newAssessmentTitle;
    private EditText newAssessmentStartDate;
    private EditText newAssessmentEndDate;
    private SwitchMaterial newAssessmentAlerts;
    private Spinner coursesSpinner;
    private Spinner assessmentTypesSpinner;

    private Date assessmentStartDate;
    private Date assessmentEndDate;

    private DatePickerDialog newAssessmentStartDateDialog;
    private DatePickerDialog newAssessmentEndDateDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_assessment);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbhelper = new DBHelper(this);
        setViewIds();
        setUpSpinners();

        newAssessmentStartDate.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            newAssessmentStartDateDialog = new DatePickerDialog(AddNewAssessment.this, (datePicker, year1, month1, day1) -> {
                int newDay = datePicker.getDayOfMonth();
                int newMonth = datePicker.getMonth() + 1;
                int newYear = datePicker.getYear();
                String newDate = newYear + "-" + newMonth + "-" + newDay;
                assessmentStartDate = Date.valueOf(newDate);
                String normalFormat = newMonth + "/" + newDay + "/" + newYear;
                newAssessmentStartDate.setText(normalFormat);
            },year,month,day);
            newAssessmentStartDateDialog.show();
        });

        newAssessmentEndDate.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            newAssessmentEndDateDialog = new DatePickerDialog(AddNewAssessment.this, (datePicker, year1, month1, day1) -> {
                int newDay = datePicker.getDayOfMonth();
                int newMonth = datePicker.getMonth() + 1;
                int newYear = datePicker.getYear();
                String newDate = newYear + "-" + newMonth + "-" + newDay;
                assessmentEndDate = Date.valueOf(newDate);
                String normalFormat = newMonth + "/" + newDay + "/" + newYear;
                newAssessmentEndDate.setText(normalFormat);
            }, year, month, day);
            newAssessmentEndDateDialog.show();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_assessment_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        } else if (item.getItemId() == R.id.add_new_assessment_menu_save) {
            onSaveNewAssessmentButtonClicked();
        } else if (item.getItemId() == R.id.add_new_assessment_menu_cancel) {
            clearSelections();
            Toast toast = Toast.makeText(this,"Assessment not saved.", Toast.LENGTH_SHORT);
            toast.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setViewIds() {
        newAssessmentTitle = findViewById(R.id.newEditTextAssessmentTitle);
        newAssessmentStartDate = findViewById(R.id.editTextNewAssessmentStartDate);
        newAssessmentEndDate = findViewById(R.id.editTextNewAssessmentEndDate);
        newAssessmentAlerts = findViewById(R.id.switchAddNewAssessmentAlert);
        coursesSpinner = findViewById(R.id.spinnerSelectCourse);
        assessmentTypesSpinner = findViewById(R.id.spinnerSelectType);
    }

    private void setUpSpinners() {
        List<String> courseNames = dbhelper.getCourseNames();
        courseNames.add(0, "Select Course");
        ArrayAdapter<String> coursesSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseNames);
        coursesSpinner.setAdapter(coursesSpinnerAdapter);

        List<String> assessmentTypes;
        assessmentTypes = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.assessment_types)));
        ArrayAdapter<String> assessmentTypesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, assessmentTypes);
        assessmentTypesSpinner.setAdapter(assessmentTypesAdapter);

    }

    private void clearSelections() {
        newAssessmentTitle.getText().clear();
        newAssessmentStartDate.getText().clear();
        newAssessmentEndDate.getText().clear();
        if (newAssessmentAlerts.isChecked()) {
            newAssessmentAlerts.toggle();
        }
        coursesSpinner.setSelection(0);
        assessmentTypesSpinner.setSelection(0);
    }

    private void onSaveNewAssessmentButtonClicked() {
        if (newAssessmentTitle.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this,"Assessment title is required.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (newAssessmentStartDate.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this,"Assessment start date is required.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (newAssessmentEndDate.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this,"Assessment end date is required.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (assessmentEndDate.before(assessmentStartDate)) {
            Toast toast = Toast.makeText(this,"Assessment end date before start date.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (coursesSpinner.getSelectedItemPosition() == 0) {
            Toast toast = Toast.makeText(this, "Assessment course not selected.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (assessmentTypesSpinner.getSelectedItemPosition() == 0) {
            Toast toast = Toast.makeText(this,"Assessment type not selected.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            assessment = new Assessment(dbhelper.getCourseIdFromString(coursesSpinner.getSelectedItem().toString()),
                                        newAssessmentTitle.getText().toString(),
                                        assessmentTypesSpinner.getSelectedItem().toString(),
                                        assessmentStartDate,
                                        assessmentEndDate);
            boolean result = dbhelper.addAssessment(assessment);
            Toast toast;
            if (result) {
                if(newAssessmentAlerts.isChecked()) {
                    scheduleStartDateNotification();
                    scheduleEndDateNotification();
                }
                toast = Toast.makeText(this, "New assessment saved", Toast.LENGTH_SHORT);
                toast.show();
                clearSelections();
            } else {
                toast = Toast.makeText(this, "New assessment not saved", Toast.LENGTH_SHORT);
                toast.show();
            }

        }

    }

    private void scheduleStartDateNotification() {
        Intent intent = new Intent(AddNewAssessment.this, NotificationReceiver.class);
        long notificationTrigger = assessment.getStartDate().getTime();
        String message = assessment.getTitle() + " starts today!";
        intent.putExtra("key", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddNewAssessment.this, MainActivity.numAlert++, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTrigger, pendingIntent);
    }

    private void scheduleEndDateNotification() {
        Intent startIntent = new Intent(AddNewAssessment.this, NotificationReceiver.class);
        long notificationTrigger = assessment.getEndDate().getTime();
        String startDateMessage = assessment.getTitle() + " ends today!";
        startIntent.putExtra("key", startDateMessage);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddNewAssessment.this, MainActivity.numAlert++, startIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTrigger, pendingIntent);
    }


}