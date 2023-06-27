package com.drewbrokamp.classmanagement.ViewControllers.Detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import com.drewbrokamp.classmanagement.Model.Note;
import com.drewbrokamp.classmanagement.R;

import java.util.List;

public class NoteDetail extends AppCompatActivity {

    DBHelper dbHelper;
    Note note;

    EditText editTextNoteTitle;
    Spinner spinnerNoteCourse;
    EditText editTextNoteContent;

    List<String> courseNames;
    ArrayAdapter<String> courseNamesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        dbHelper = new DBHelper(this);
        String noteID = getIntent().getStringExtra("noteID");
        note = dbHelper.getNoteFromID(Integer.parseInt(noteID));

        setViewIds();
        initializeCourseSpinner();
        initializeNoteValues();

        editTextNoteContent.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(view);
            }
        });

        editTextNoteTitle.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(view);
            }
        });


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_details, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackButtonClicked();
        } else if (item.getItemId() == R.id.note_details_menu_share_note) {
            createShareIntent();
        } else if (item.getItemId() == R.id.note_details_menu_save) {
            onSaveNoteChangesButtonClicked();
        } else if (item.getItemId() == R.id.note_details_menu_cancel) {
            onCancelNoteChangesButtonClicked();
        } else if (item.getItemId() == R.id.note_details_menu_delete) {
            onDeleteButtonClicked();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setViewIds() {
        editTextNoteTitle = findViewById(R.id.editTextNoteDetailTitle);
        spinnerNoteCourse = findViewById(R.id.noteDetailSpinnerSelectCourse);
        editTextNoteContent = findViewById(R.id.noteDetailText);
    }

    private void initializeCourseSpinner() {
        courseNames = dbHelper.getCourseNames();
        courseNamesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseNames);
        spinnerNoteCourse.setAdapter(courseNamesAdapter);
    }

    private void initializeNoteValues() {
        editTextNoteTitle.setText(note.getTitle());

        int currentCourseSpinnerPosition = courseNamesAdapter.getPosition(dbHelper.getCourseFromId(note.getCourseID()).getTitle());
        spinnerNoteCourse.setSelection(currentCourseSpinnerPosition);

        editTextNoteContent.setText(note.getContent());

    }

    private void createShareIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String noteTitle = note.getTitle() + " from " + dbHelper.getCourseFromId(note.getCourseID()).getTitle();
        sendIntent.putExtra(Intent.EXTRA_TITLE, noteTitle);
        sendIntent.putExtra(Intent.EXTRA_TEXT, note.getContent());
        sendIntent.setType("text/plain");


        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onSaveNoteChangesButtonClicked() {

        Note updatedNote = new Note (dbHelper.getCourseIdFromString(courseNames.get(spinnerNoteCourse.getSelectedItemPosition())),
                                        editTextNoteTitle.getText().toString(),
                                        editTextNoteContent.getText().toString());

        dbHelper.updateNote(note.getId(), updatedNote);

        Toast toast = Toast.makeText(NoteDetail.this,"Changes saved.", Toast.LENGTH_SHORT);
        toast.show();

    }

    //TODO: change name of method
    public void onCancelNoteChangesButtonClicked() {
        Toast toast = Toast.makeText(NoteDetail.this,"Changes canceled.", Toast.LENGTH_SHORT);
        toast.show();
        initializeNoteValues();
    }

    private void onBackButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Return To Previous Activity");
        builder.setMessage("Any changes will not be saved. Continue?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    Toast toast = Toast.makeText(NoteDetail.this, "Any changes not saved.", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            Toast toast = Toast.makeText(NoteDetail.this, "Save changes before returning to previous activity.", Toast.LENGTH_SHORT);
            toast.show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onDeleteButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    dbHelper.deleteNote(note.getId());
                    Toast toast = Toast.makeText(NoteDetail.this, "Note deleted.", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            Toast toast = Toast.makeText(NoteDetail.this, "Note not deleted.", Toast.LENGTH_SHORT);
            toast.show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }



}