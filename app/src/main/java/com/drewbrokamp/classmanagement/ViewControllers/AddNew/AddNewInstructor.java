package com.drewbrokamp.classmanagement.ViewControllers.AddNew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Model.Instructor;
import com.drewbrokamp.classmanagement.R;

public class AddNewInstructor extends AppCompatActivity {

    private EditText newInstructorName;
    private EditText newInstructorEmail;
    private EditText newInstructorPhoneNumber;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_instructor);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(AddNewInstructor.this);

        newInstructorName = findViewById(R.id.editTextNewInstructorName);
        newInstructorEmail = findViewById(R.id.editTextNewInstructorEmail);
        newInstructorPhoneNumber = findViewById(R.id.editTextNewInstructorPhoneNumber);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_instructor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.finish();
        } else if (itemId == R.id.add_new_instructor_menu_save) {
            onAddNewInstructorButtonClicked();
        } else if (itemId == R.id.add_new_instructor_menu_cancel) {
            clearSelections();
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearSelections() {
        newInstructorName.getText().clear();
        newInstructorPhoneNumber.getText().clear();
        newInstructorEmail.getText().clear();
    }

    private void onAddNewInstructorButtonClicked() {
        if (newInstructorName.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this,"Instructor name required.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (newInstructorEmail.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this,"Instructor email required.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (newInstructorPhoneNumber.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this,"Instructor email required.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            boolean result = dbHelper.addInstructor(new Instructor(newInstructorName.getText().toString(),
                    newInstructorPhoneNumber.getText().toString(),
                    newInstructorEmail.getText().toString()));
            if (result) {
                Toast toast = Toast.makeText(this,"Instructor added.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0 ,0);
                toast.show();
                clearSelections();
            } else {
                Toast toast = Toast.makeText(this,"Instructor not added.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0 ,0);
                toast.show();
            }
        }

    }
}