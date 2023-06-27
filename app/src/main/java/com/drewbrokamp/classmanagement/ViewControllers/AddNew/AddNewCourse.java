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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.MainActivity;
import com.drewbrokamp.classmanagement.Model.Course;
import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.Util.NotificationReceiver;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddNewCourse extends AppCompatActivity {

    private List<String> termNames;
    private List<String> courseStatuses;
    private List<String> instructors;
    private Date courseStartDate;
    private Date courseEndDate;
    private DBHelper dbHelper;
    private Course course;

    private ArrayAdapter<String> instructorsAdapter;
    private ArrayAdapter<String> termsAdapter;
    private ArrayAdapter<String> courseStatusAdapter;

    private EditText editTextNewCourseTitle;
    private EditText newCourseStartDate;
    private DatePickerDialog newCourseStartDatePicker;
    private EditText newCourseEndDate;
    private DatePickerDialog newCourseEndDatePicker;
    private SwitchMaterial newCourseAlerts;
    private Spinner courseTerm;
    private Spinner courseStatus;
    private Spinner courseInstructor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);

        setViewIds();

        createLists();

        System.out.println(courseTerm);
        setAdapters();

        newCourseStartDate.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            newCourseStartDatePicker = new DatePickerDialog(AddNewCourse.this, (datePicker, year1, month1, date1) -> {
                int newDay = datePicker.getDayOfMonth();
                int newMonth = datePicker.getMonth() + 1;
                int newYear = datePicker.getYear();
                String newDate = newYear + "-" + newMonth + "-" + newDay;
                courseStartDate = Date.valueOf(newDate);
                String normalFormat = newMonth + "/" + newDay + "/" + newYear;
                newCourseStartDate.setText(normalFormat);
            }, year, month, day);
            newCourseStartDatePicker.show();
        });


        newCourseEndDate.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            newCourseEndDatePicker = new DatePickerDialog(AddNewCourse.this, (datePicker, year1, month1, day1) -> {
                int newDay = datePicker.getDayOfMonth();
                int newMonth = datePicker.getMonth() + 1;
                int newYear = datePicker.getYear();
                String newDate = newYear + "-" + newMonth + "-" + newDay;
                courseEndDate = Date.valueOf(newDate);
                String normalFormat = newMonth + "/" + newDay + "/" + newYear;
                newCourseEndDate.setText(normalFormat);
            }, year, month, day);
            newCourseEndDatePicker.show();
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_course_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.finish();
        } else if (itemId == R.id.item_add_new_course_add_term) {
            goToAddNewTerm();
        } else if (itemId == R.id.item_add_new_course_add_instructor) {
            goToAddNewInstructor();
        } else if (itemId == R.id.add_new_course_menu_save) {
            onAddNewCourseSaveButtonClicked();
        } else if (itemId == R.id.add_new_course_menu_cancel) {
            clearSelections();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        instructors.clear();
        termNames.clear();

        createLists();
        setAdapters();
    }

    private void setViewIds() {
        editTextNewCourseTitle = findViewById(R.id.editTextAddNewCourseTitle);
        newCourseStartDate = findViewById(R.id.editTextAddNewCourseStartDate);
        newCourseEndDate = findViewById(R.id.editTextAddNewCourseEndDate);
        newCourseAlerts = findViewById(R.id.switchAddNewCourseAlert);
        courseTerm = findViewById(R.id.spinnerAddNewCourseTerm);
        courseStatus = findViewById(R.id.spinnerAddNewCourseStatus);
        courseInstructor = findViewById(R.id.spinnerAddNewCourseInstructor);
    }

    private void createLists() {
        termNames = dbHelper.getTermNames();
        termNames.add(0, "Select");

        courseStatuses = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.course_statuses)));
        courseStatuses.add(0, "Select");

        instructors = dbHelper.getInstructorNames();
        instructors.add(0, "Select");

    }

    private void setAdapters() {
        termsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, termNames);
        courseTerm.setAdapter(termsAdapter);

        courseStatusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseStatuses);
        courseStatus.setAdapter(courseStatusAdapter);

        instructorsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, instructors);
        courseInstructor.setAdapter(instructorsAdapter);
    }


    private void goToAddNewInstructor() {
        Intent intent = new Intent(this, AddNewInstructor.class);
        startActivity(intent);
    }

    private void goToAddNewTerm() {
        Intent intent = new Intent(this, AddNewTerm.class);
        startActivity(intent);
    }


    private void onAddNewCourseSaveButtonClicked() {
        closeKeyboard();
        if (editTextNewCourseTitle.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this, "Course title is required.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (newCourseStartDate.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this, "Course start date is required.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (newCourseEndDate.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this, "Course end date is required.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (courseEndDate.before(courseStartDate)) {
            Toast toast = Toast.makeText(this, "Course end date set before start date.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (courseTerm.getSelectedItemPosition() == 0) {
            Toast toast = Toast.makeText(this, "Course term not selected.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (courseStatus.getSelectedItemPosition() == 0) {
            Toast toast = Toast.makeText(this, "Course status not selected.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (courseInstructor.getSelectedItemPosition() == 0) {
            Toast toast = Toast.makeText(this, "Course instructor not selected.", Toast.LENGTH_SHORT);
            toast.show();
        } else {

            course = new Course(editTextNewCourseTitle.getText().toString(),
                    courseStartDate,
                    courseEndDate,
                    courseStatus.getSelectedItem().toString(),
                    dbHelper.getTermIdFromString(courseTerm.getSelectedItem().toString()),
                    dbHelper.getInstructorIdFromString(courseInstructor.getSelectedItem().toString()));
            boolean result = dbHelper.addCourse(course);

            Toast toast;

            if (result && newCourseAlerts.isChecked()) {
                scheduleStartDateNotification();
                scheduleEndDateNotification();
                toast = Toast.makeText(this, "New course saved.", Toast.LENGTH_SHORT);
                clearSelections();
            } else if (result && !newCourseAlerts.isChecked()) {
                toast = Toast.makeText(this, "New course saved.", Toast.LENGTH_SHORT);
                clearSelections();
            } else {
                toast = Toast.makeText(this, "New course not saved.", Toast.LENGTH_SHORT);
            }

            toast.show();

        }

    }

    private void scheduleStartDateNotification() {
        Intent intent = new Intent(AddNewCourse.this, NotificationReceiver.class);
        long notificationTrigger = course.getStartDate().getTime();
        String message = course.getTitle() + " starts today!";
        intent.putExtra("key", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddNewCourse.this, MainActivity.numAlert++, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTrigger, pendingIntent);
    }

    private void scheduleEndDateNotification() {
        Intent startIntent = new Intent(AddNewCourse.this, NotificationReceiver.class);
        long notificationTrigger = course.getEndDate().getTime();
        String startDateMessage = course.getTitle() + " ends today!";
        startIntent.putExtra("key", startDateMessage);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddNewCourse.this, MainActivity.numAlert++, startIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTrigger, pendingIntent);
    }

    private void clearSelections() {
        editTextNewCourseTitle.getText().clear();
        newCourseStartDate.getText().clear();
        newCourseEndDate.getText().clear();
        if (newCourseAlerts.isChecked()) {
            newCourseAlerts.toggle();
        }
        courseTerm.setSelection(0);
        courseStatus.setSelection(0);
        courseInstructor.setSelection(0);
    }

    private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }
}