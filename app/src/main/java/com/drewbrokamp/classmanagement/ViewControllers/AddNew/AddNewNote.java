package com.drewbrokamp.classmanagement.ViewControllers.AddNew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Model.Note;
import com.drewbrokamp.classmanagement.R;

import java.util.List;

public class AddNewNote extends AppCompatActivity {

    private DBHelper dbHelper;

    private EditText newNoteContentEditText;
    private EditText newNoteTitleEditText;

    Spinner coursesSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);

        newNoteTitleEditText = findViewById(R.id.editTextNoteTitle);
        newNoteContentEditText = findViewById(R.id.noteText);
        coursesSpinner = findViewById(R.id.spinnerAddNewNoteSelectCourse);


        List<String> courseNames = dbHelper.getCourseNames();
        courseNames.add(0, "Courses");

        ArrayAdapter<String> courseTypesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseNames);

        System.out.println(coursesSpinner);
        coursesSpinner.setAdapter(courseTypesAdapter);

    }

    private void clearSelections() {
        newNoteTitleEditText.getText().clear();
        coursesSpinner.setSelection(0);
        newNoteContentEditText.getText().clear();
    }

    public void onAddNewNoteButtonClicked() {
        if (newNoteTitleEditText.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this, "Note title is required.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (coursesSpinner.getSelectedItemPosition() == 0) {
            Toast toast = Toast.makeText(this, "Course not selected.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (newNoteContentEditText.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this, "Note content is required.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            boolean result = dbHelper.addNote(new Note(dbHelper.getCourseIdFromString(coursesSpinner.getSelectedItem().toString()),
                    newNoteTitleEditText.getText().toString(),
                    newNoteContentEditText.getText().toString()));

            Toast toast;
            if (result) {
                toast = Toast.makeText(this, "New note created.", Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(this, "Failed to create note.", Toast.LENGTH_SHORT);
            }
            toast.show();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        } else if (item.getItemId() == R.id.add_new_note_menu_save) {
            onAddNewNoteButtonClicked();
        } else if (item.getItemId() == R.id.add_new_note_menu_cancel) {
            clearSelections();
        }

        return super.onOptionsItemSelected(item);
    }

}