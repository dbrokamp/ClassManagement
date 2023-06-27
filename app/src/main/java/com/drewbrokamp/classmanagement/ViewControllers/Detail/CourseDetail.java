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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.drewbrokamp.classmanagement.Adapters.AssessmentAdapter;
import com.drewbrokamp.classmanagement.Adapters.NoteAdapter;
import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Model.Assessment;
import com.drewbrokamp.classmanagement.Model.Course;
import com.drewbrokamp.classmanagement.Model.Note;
import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.Util.AssessmentRecyclerViewInterface;
import com.drewbrokamp.classmanagement.Util.NoteRecyclerViewInterface;
import com.drewbrokamp.classmanagement.ViewControllers.AddNew.AddNewAssessment;
import com.drewbrokamp.classmanagement.ViewControllers.AddNew.AddNewNote;


import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

//TODO: sort assessment list by date

public class CourseDetail extends AppCompatActivity implements AssessmentRecyclerViewInterface, NoteRecyclerViewInterface {

    DBHelper dbHelper;
    Course course;

    List<Assessment> assessments;
    AssessmentAdapter assessmentAdapter;
    RecyclerView assessmentRecyclerView;

    List<Note> notes;
    NoteAdapter noteAdapter;
    RecyclerView noteRecyclerView;

    EditText editTextCourseName;
    EditText editTextStartDate;
    EditText editTextEndDate;
    Spinner spinnerTerm;
    ArrayAdapter<String> termNamesAdapter;
    Spinner spinnerStatus;
    ArrayAdapter<String> statusAdapter;
    Spinner spinnerInstructor;
    ArrayAdapter<String> instructorNamesAdapter;

    List<String> termNames;
    List<String> statuses;
    List<String> instructorNames;

    DatePickerDialog startDateDialog;
    DatePickerDialog endDateDialog;

    Date updatedStartDate;
    Date updatedEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        dbHelper = new DBHelper(this);
        String courseID = getIntent().getStringExtra("courseID");
        course = dbHelper.getCourseFromId(Integer.parseInt(courseID));

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        setViewIds();
        initializeTermsSpinner();
        initializeStatusSpinner();
        initializeInstructorSpinner();
        initializeAssessmentsView();
        initializeNotesView();
        initializeCourseValues();

        editTextStartDate.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            startDateDialog = new DatePickerDialog(CourseDetail.this, (datePicker, year1, month1, date1) -> {
                int newDay = datePicker.getDayOfMonth();
                int newMonth = datePicker.getMonth() + 1;
                int newYear = datePicker.getYear();
                String newDate = newYear + "-" + newMonth + "-" + newDay;
                updatedStartDate = Date.valueOf(newDate);
                String normalFormat = newMonth + "/" + newDay + "/" + newYear;
                editTextStartDate.setText(normalFormat);
            }, year, month, day);
            startDateDialog.show();
        });


        editTextEndDate.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            endDateDialog = new DatePickerDialog(CourseDetail.this, (datePicker, year1, month1, date1) -> {
                int newDay = datePicker.getDayOfMonth();
                int newMonth = datePicker.getMonth() + 1;
                int newYear = datePicker.getYear();
                String newDate = newYear + "-" + newMonth + "-" + newDay;
                updatedEndDate = Date.valueOf(newDate);
                String normalFormat = newMonth + "/" + newDay + "/" + newYear;
                editTextEndDate.setText(normalFormat);
            }, year, month, day);
            endDateDialog.show();
        });

    }

    private void setViewIds() {
        editTextCourseName = findViewById(R.id.editTextDetailCourseName);
        editTextStartDate = findViewById(R.id.editTextDetailCourseStartDate);
        editTextEndDate = findViewById(R.id.editTextDetailCourseEndDate);
        spinnerTerm = findViewById(R.id.spinnerCourseDetailTerm);
        spinnerStatus = findViewById(R.id.spinnerCourseDetailStatus);
        spinnerInstructor = findViewById(R.id.spinnerCourseDetailInstructor);
        assessmentRecyclerView = findViewById(R.id.courseDetailAssessmentList);
        noteRecyclerView = findViewById(R.id.courseDetailNotesList);
    }

    private void initializeCourseValues() {
        editTextCourseName.setText(course.getTitle());

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        editTextStartDate.setText(dateFormat.format(course.getStartDate()));
        updatedStartDate = course.getStartDate();
        editTextEndDate.setText(dateFormat.format(course.getEndDate()));
        updatedEndDate = course.getEndDate();

        int currentTermSpinnerPosition = termNamesAdapter.getPosition(dbHelper.getTermFromId(course.getTermID()).getTitle());
        spinnerTerm.setSelection(currentTermSpinnerPosition);

        int currentStatusSpinnerPosition = statusAdapter.getPosition(course.getStatus());
        spinnerStatus.setSelection(currentStatusSpinnerPosition);

        int currentInstructorSpinnerPosition = instructorNamesAdapter.getPosition(dbHelper.getInstructorFromId(course.getInstructorID()).getName());
        spinnerInstructor.setSelection(currentInstructorSpinnerPosition);

    }

    private void initializeTermsSpinner() {
        termNames = dbHelper.getTermNames();
        termNamesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, termNames);
        spinnerTerm.setAdapter(termNamesAdapter);
    }

    private void initializeStatusSpinner() {
        statuses = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.course_statuses)));
        statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        spinnerStatus.setAdapter(statusAdapter);
    }

    private void initializeInstructorSpinner() {
        instructorNames = dbHelper.getInstructorNames();
        instructorNamesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, instructorNames);
        spinnerInstructor.setAdapter(instructorNamesAdapter);
    }

    private void initializeAssessmentsView() {
        assessments = dbHelper.getAssessmentsForCourseID(course.getId());
        assessmentAdapter = new AssessmentAdapter(this, assessments, this);

        assessmentRecyclerView.setAdapter(assessmentAdapter);
        assessmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void initializeNotesView() {
        notes = dbHelper.getNotesForCourseID(course.getId());
        noteAdapter = new NoteAdapter(this, notes, this);

        noteRecyclerView.setAdapter(noteAdapter);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public void onRestart() {
        super.onRestart();
        initializeAssessmentsView();
        initializeNotesView();
    }

    @Override
    public void onAssessmentClick(int position) {
        Assessment assessment = assessments.get(position);
        Intent intent = new Intent(this, AssessmentDetail.class);
        intent.putExtra("assessmentID", assessment.getId().toString());
        startActivity(intent);

    }

    @Override
    public void onNoteClick(int position) {
        Note note = notes.get(position);
        Intent intent = new Intent(this, NoteDetail.class);
        intent.putExtra("noteID", note.getId().toString());
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_course_detail_add_new_assessment) {
            goToAddNewAssessment();
        } else if (itemId == R.id.menu_course_detail_add_new_note) {
            goToAddNewNote();
        } else if (itemId == R.id.menu_course_detail_save) {
            courseDetailSaveButtonClicked();
        } else if (itemId == R.id.menu_course_detail_cancel) {
            courseDetailCancelButtonClicked();
        } else if (itemId == R.id.menu_course_detail_delete) {
            courseDetailDeleteButtonClicked();
        } else if (itemId == android.R.id.home) {
            backButtonClicked();
        }

        return super.onOptionsItemSelected(item);

    }

    public void courseDetailSaveButtonClicked() {
        Course updatedCourse = new Course(course.getId(),
                editTextCourseName.getText().toString(),
                updatedStartDate,
                updatedEndDate,
                spinnerStatus.getSelectedItem().toString(),
                dbHelper.getTermIdFromString(spinnerTerm.getSelectedItem().toString()),
                dbHelper.getInstructorIdFromString(spinnerInstructor.getSelectedItem().toString()));
        dbHelper.updateCourse(updatedCourse);
        Toast toast = Toast.makeText(CourseDetail.this, "Changes saved.", Toast.LENGTH_SHORT);
        toast.show();

    }

    public void courseDetailCancelButtonClicked() {
        Toast toast = Toast.makeText(CourseDetail.this, "Changes canceled.", Toast.LENGTH_SHORT);
        toast.show();
        initializeCourseValues();

    }

    public void courseDetailDeleteButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this course?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    dbHelper.deleteCourse(course.getId());
                    Toast toast = Toast.makeText(CourseDetail.this, "Course deleted.", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            Toast toast = Toast.makeText(CourseDetail.this, "Course not deleted.", Toast.LENGTH_SHORT);
            toast.show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void backButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Return To Previous Activity");
        builder.setMessage("Any changes will not be saved. Continue?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    Toast toast = Toast.makeText(CourseDetail.this, "Any changes not saved.", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            Toast toast = Toast.makeText(CourseDetail.this, "Save changes before returning to previous activity.", Toast.LENGTH_SHORT);
            toast.show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void goToAddNewAssessment() {
        Intent intent = new Intent(this, AddNewAssessment.class);
        startActivity(intent);
    }

    private void goToAddNewNote() {
        Intent intent = new Intent(this, AddNewNote.class);
        startActivity(intent);
    }
}