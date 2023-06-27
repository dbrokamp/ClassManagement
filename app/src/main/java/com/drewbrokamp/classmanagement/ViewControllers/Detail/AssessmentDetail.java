package com.drewbrokamp.classmanagement.ViewControllers.Detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Model.Assessment;
import com.drewbrokamp.classmanagement.R;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AssessmentDetail extends AppCompatActivity {

    DBHelper dbHelper;
    Assessment assessment;

    EditText editTextAssessmentTitle;
    EditText editTextAssessmentStartDate;
    EditText editTextAssessmentEndDate;
    Spinner spinnerAssessmentCourse;
    Spinner spinnerAssessmentType;

    ArrayAdapter<String> courseNamesAdapter;
    ArrayAdapter<String> assessmentTypesAdapter;

    List<String> courseNames;
    List<String> assessmentTypes;
    Date updatedStartDate;
    Date updatedEndDate;

    DatePickerDialog startDateDialog;
    DatePickerDialog endDateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        dbHelper = new DBHelper(this);
        String assessmentID = getIntent().getStringExtra("assessmentID");
        assessment = dbHelper.getAssessmentFromID(Integer.parseInt(assessmentID));

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        setViewIds();
        initializeCourseSpinner();
        initializeAssessmentTypeSpinner();
        initializeAssessmentValues();

        editTextAssessmentStartDate.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            startDateDialog = new DatePickerDialog(AssessmentDetail.this, (datePicker, year1, month1, date1) -> {
                int newDay = datePicker.getDayOfMonth();
                int newMonth = datePicker.getMonth() + 1;
                int newYear = datePicker.getYear();
                String newDate = newYear + "-" + newMonth + "-" + newDay;
                updatedStartDate = Date.valueOf(newDate);
                String normalFormat = newMonth + "/" + newDay + "/" + newYear;
                editTextAssessmentStartDate.setText(normalFormat);
            },year,month,day);
            startDateDialog.show();
        });

        editTextAssessmentEndDate.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            endDateDialog = new DatePickerDialog(AssessmentDetail.this, (datePicker, year1, month1, date1) -> {
                int newDay = datePicker.getDayOfMonth();
                int newMonth = datePicker.getMonth() + 1;
                int newYear = datePicker.getYear();
                String newDate = newYear + "-" + newMonth + "-" + newDay;
                updatedEndDate = Date.valueOf(newDate);
                String normalFormat = newMonth + "/" + newDay + "/" + newYear;
                editTextAssessmentEndDate.setText(normalFormat);
            },year,month,day);
            endDateDialog.show();
        });
    }

    private void setViewIds() {
        editTextAssessmentTitle = findViewById(R.id.editTextAssessmentDetailTitle);
        editTextAssessmentStartDate = findViewById(R.id.editTextAssessmentDetailStartDate);
        editTextAssessmentEndDate = findViewById(R.id.editTextAssessmentDetailEndDate);
        spinnerAssessmentCourse = findViewById(R.id.spinnerAssessmentDetailSelectCourse);
        spinnerAssessmentType = findViewById(R.id.spinnerAssessmentDetailType);
    }

    private void initializeCourseSpinner() {
        courseNames = dbHelper.getCourseNames();
        courseNamesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseNames);
        spinnerAssessmentCourse.setAdapter(courseNamesAdapter);
    }

    private void initializeAssessmentTypeSpinner() {
        assessmentTypes = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.assessment_types)));
        assessmentTypesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, assessmentTypes);
        spinnerAssessmentType.setAdapter(assessmentTypesAdapter);
    }

    private void initializeAssessmentValues() {
        editTextAssessmentTitle.setText(assessment.getTitle());

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        editTextAssessmentStartDate.setText(dateFormat.format(assessment.getStartDate()));
        updatedStartDate = assessment.getStartDate();
        editTextAssessmentEndDate.setText(dateFormat.format(assessment.getEndDate()));
        updatedEndDate = assessment.getEndDate();

        int currentCourseSpinnerPosition = courseNamesAdapter.getPosition(dbHelper.getCourseFromId(assessment.getCourseID()).getTitle());
        spinnerAssessmentCourse.setSelection(currentCourseSpinnerPosition);

        int currentAssessmentTypeSpinnerPosition = assessmentTypesAdapter.getPosition(assessment.getType());
        spinnerAssessmentType.setSelection(currentAssessmentTypeSpinnerPosition);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.assessment_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackButtonClicked();
        } else if (item.getItemId() == R.id.assessment_details_menu_save) {
            onSaveAssessmentChangesButtonClicked();
        } else if (item.getItemId() == R.id.assessment_details_menu_cancel) {
            onCancelAssessmentChangesButtonClicked();
        } else if (item.getItemId() == R.id.assessment_details_menu_delete) {
            onDeleteButtonClicked();
        }


        return super.onOptionsItemSelected(item);
    }

    private void onBackButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Return To Previous Activity");
        builder.setMessage("Any changes will not be saved. Continue?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    Toast toast = Toast.makeText(AssessmentDetail.this, "Any changes not saved.", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            Toast toast = Toast.makeText(AssessmentDetail.this, "Save changes before returning to previous activity.", Toast.LENGTH_SHORT);
            toast.show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onSaveAssessmentChangesButtonClicked() {

        Assessment updatedAssessment = new Assessment(assessment.getId(),
                                                        editTextAssessmentTitle.getText().toString(),
                                                        spinnerAssessmentType.getSelectedItem().toString(),
                                                        updatedStartDate,
                                                        updatedEndDate,
                                                        dbHelper.getCourseIdFromString(courseNames.get(spinnerAssessmentCourse.getSelectedItemPosition())));
        dbHelper.updateAssessment(assessment.getId(), updatedAssessment);
        Toast toast = Toast.makeText(AssessmentDetail.this,"Changes saved.", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onCancelAssessmentChangesButtonClicked() {
        Toast toast = Toast.makeText(AssessmentDetail.this,"Changes canceled.", Toast.LENGTH_SHORT);
        toast.show();
        initializeAssessmentValues();
    }

    private void onDeleteButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this assessment?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    dbHelper.deleteAssessment(assessment.getId());
                    Toast toast = Toast.makeText(AssessmentDetail.this, "Assessment deleted.", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            Toast toast = Toast.makeText(AssessmentDetail.this, "Assessment not deleted.", Toast.LENGTH_SHORT);
            toast.show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}