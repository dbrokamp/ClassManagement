package com.drewbrokamp.classmanagement.ViewControllers.Detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.drewbrokamp.classmanagement.Adapters.CourseAdapter;
import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Model.Course;
import com.drewbrokamp.classmanagement.Model.Term;
import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.Util.CourseRecyclerViewInterface;
import com.drewbrokamp.classmanagement.ViewControllers.AddNew.AddNewCourse;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TermDetail extends AppCompatActivity implements CourseRecyclerViewInterface {
    DBHelper dbHelper;
    Term term;
    List<Course> courses;
    CourseAdapter courseAdapter;
    RecyclerView recyclerView;

    EditText editTextTermTitle;
    EditText editTextTermStartDate;
    EditText editTextTermEndDate;

    DatePickerDialog startDateDialog;
    DatePickerDialog endDateDialog;

    Date updatedStartDate;
    Date updatedEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(TermDetail.this);

        String termID = getIntent().getStringExtra("termID");
        term = dbHelper.getTermFromId(Integer.parseInt(termID));

        courses = dbHelper.getCoursesForTerm(term.getId());
        Collections.sort(courses, Comparator.comparing(Course::getStartDate));

        setIds();

        editTextTermTitle.setText(term.getTitle());


        editTextTermStartDate.setText(term.getStartDateString());
        updatedStartDate = term.getStartDate();


        editTextTermEndDate.setText(term.getEndDateString());
        updatedEndDate = term.getEndDate();

        courseAdapter = new CourseAdapter(this, courses, this, dbHelper);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        editTextTermTitle.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(view);
            }
        });

        editTextTermStartDate.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            startDateDialog = new DatePickerDialog(TermDetail.this, (datePicker, year1, month1, date1) -> {
                int newDay = datePicker.getDayOfMonth();
                int newMonth = datePicker.getMonth() + 1;
                int newYear = datePicker.getYear();
                String newDate = newYear + "-" + newMonth + "-" + newDay;
                updatedStartDate = Date.valueOf(newDate);
                String normalFormat = newMonth + "/" + newDay + "/" + newYear;
                editTextTermStartDate.setText(normalFormat);
            },year,month,day);
            startDateDialog.show();
        });

        editTextTermEndDate.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            endDateDialog = new DatePickerDialog(TermDetail.this, (datePicker, year1, month1, date1) -> {
                int newDay = datePicker.getDayOfMonth();
                int newMonth = datePicker.getMonth() + 1;
                int newYear = datePicker.getYear();
                String newDate = newYear + "-" + newMonth + "-" + newDay;
                updatedEndDate = Date.valueOf(newDate);
                String normalFormat = newMonth + "/" + newDay + "/" + newYear;
                editTextTermEndDate.setText(normalFormat);
            },year,month,day);
            endDateDialog.show();
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        courses = dbHelper.getCoursesForTerm(term.getId());
        Collections.sort(courses, Comparator.comparing(Course::getStartDate));
        courseAdapter = new CourseAdapter(this, courses, this, dbHelper);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setIds() {

        editTextTermTitle = findViewById(R.id.editTextTermDetailName);
        editTextTermStartDate = findViewById(R.id.editTextTermDetailStartDate);
        editTextTermEndDate = findViewById(R.id.editTextTermDetailEndDate);
        recyclerView = findViewById(R.id.recyclerViewTermDetailCourses);

    }

    public void onClickedButtonTermDetailSave(MenuItem item) {
        Term termUpdate = new Term(editTextTermTitle.getText().toString(), updatedStartDate, updatedEndDate);
        dbHelper.updateTerm(term.getId(), termUpdate);
        Toast toast = Toast.makeText(TermDetail.this,"Changes saved.", Toast.LENGTH_SHORT);
        toast.show();

    }

    public void onClickedButtonTermDetailCancel(MenuItem item) {
        Toast toast = Toast.makeText(TermDetail.this,"Changes canceled.", Toast.LENGTH_SHORT);
        toast.show();
        editTextTermTitle.setText(term.getTitle());
        editTextTermStartDate.setText(term.getStartDateString());
        editTextTermEndDate.setText(term.getEndDateString());
    }

    public void onClickedButtonTermDetailDelete(MenuItem item) {
        System.out.println("in onclickbuttontermdetaildelete");
        if (courses.isEmpty()) {
            System.out.println("in courses is empty");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to delete this term?");
            builder.setPositiveButton("Confirm",
                    (dialog, which) -> {
                        dbHelper.deleteTerm(term.getId());
                        Toast toast = Toast.makeText(TermDetail.this, "Term deleted.", Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                Toast toast = Toast.makeText(TermDetail.this, "Term not deleted.", Toast.LENGTH_SHORT);
                toast.show();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            System.out.println("in courses is not empty");
            Toast toast = Toast.makeText(TermDetail.this,"Cannot delete this term. Delete courses assigned to it first.", Toast.LENGTH_LONG);
            toast.show();
        }


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.term_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            backButtonClicked();
        } else if (itemId == R.id.term_detail_menu_add_course) {
            goToAddNewCourse();
        } else if (itemId == R.id.menu_term_detail_save) {
            onClickedButtonTermDetailSave(item);
        } else if (itemId == R.id.menu_term_detail_cancel) {
            onClickedButtonTermDetailCancel(item);
        } else if (itemId == R.id.menu_course_detail_delete) {
            onClickedButtonTermDetailDelete(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void backButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Return To Previous Activity");
        builder.setMessage("Any changes will not be saved. Continue?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    Toast toast = Toast.makeText(TermDetail.this, "Any changes not saved.", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            Toast toast = Toast.makeText(TermDetail.this, "Save changes before returning to previous activity.", Toast.LENGTH_SHORT);
            toast.show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onCourseClick(int position) {
        Course course = courses.get(position);
        Intent intent = new Intent(this, CourseDetail.class);
        intent.putExtra("courseID", course.getId().toString());
        startActivity(intent);

    }

    private void goToAddNewCourse() {
        Intent intent = new Intent(this, AddNewCourse.class);
        startActivity(intent);
    }
}