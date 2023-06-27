package com.drewbrokamp.classmanagement.ViewControllers.Detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Model.Instructor;
import com.drewbrokamp.classmanagement.R;

public class InstructorDetail extends AppCompatActivity {

    DBHelper dbHelper;
    Instructor instructor;

    EditText editTextInstructorName;
    EditText editTextInstructorEmail;
    EditText editTextInstructorPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);
        String instructorID = getIntent().getStringExtra("instructorID");
        instructor = dbHelper.getInstructorFromId(Integer.valueOf(instructorID));
        setViewIds();
        initializeInstructorValues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.instructor_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackButtonClicked();
        } else if (item.getItemId() == R.id.instructor_details_menu_save) {
            onSaveChangesInstructorDetailClicked();
        } else if (item.getItemId() == R.id.instructor_details_menu_cancel) {
            onCancelChangesInstructorDetailClicked();
        } else if (item.getItemId() == R.id.instructor_details_menu_delete) {
            onDeleteButtonClicked();
        }


        return super.onOptionsItemSelected(item);
    }

    private void setViewIds() {
        editTextInstructorName = findViewById(R.id.editTextInstructorDetailName);
        editTextInstructorEmail = findViewById(R.id.editTextInstructorDetailEmail);
        editTextInstructorPhoneNumber = findViewById(R.id.editTextInstructorDetailPhoneNumber);
    }

    private void initializeInstructorValues() {
        editTextInstructorName.setText(instructor.getName());
        editTextInstructorEmail.setText(instructor.getEmail());
        editTextInstructorPhoneNumber.setText(instructor.getPhoneNumber());
    }

    private void onSaveChangesInstructorDetailClicked() {
        Instructor updatedInstructor = new Instructor(editTextInstructorName.getText().toString(),
                                                        editTextInstructorEmail.getText().toString(),
                                                        editTextInstructorPhoneNumber.getText().toString());

        dbHelper.updateInstructor(instructor.getId(), updatedInstructor);

        Toast toast = Toast.makeText(InstructorDetail.this,"Changes saved.", Toast.LENGTH_SHORT);
        toast.show();

    }

    private void onCancelChangesInstructorDetailClicked() {
        Toast toast = Toast.makeText(InstructorDetail.this,"Changes canceled.", Toast.LENGTH_SHORT);
        toast.show();
        initializeInstructorValues();
    }

    private void onDeleteButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this instructor?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    dbHelper.deleteInstructor(instructor.getId());
                    Toast toast = Toast.makeText(InstructorDetail.this, "Instructor deleted.", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            Toast toast = Toast.makeText(InstructorDetail.this, "Instructor not deleted.", Toast.LENGTH_SHORT);
            toast.show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void onBackButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Return To Previous Activity");
        builder.setMessage("Any changes will not be saved. Continue?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    Toast toast = Toast.makeText(InstructorDetail.this, "Any changes not saved.", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            Toast toast = Toast.makeText(InstructorDetail.this, "Save changes before returning to previous activity.", Toast.LENGTH_SHORT);
            toast.show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}