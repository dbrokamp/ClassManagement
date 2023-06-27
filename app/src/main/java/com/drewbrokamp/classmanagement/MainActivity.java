package com.drewbrokamp.classmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.drewbrokamp.classmanagement.Adapters.MainAdapter;
import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Util.RecyclerViewInterface;
import com.drewbrokamp.classmanagement.ViewControllers.AddNew.AddNewAssessment;
import com.drewbrokamp.classmanagement.ViewControllers.AddNew.AddNewCourse;
import com.drewbrokamp.classmanagement.ViewControllers.AddNew.AddNewInstructor;
import com.drewbrokamp.classmanagement.ViewControllers.AddNew.AddNewNote;
import com.drewbrokamp.classmanagement.ViewControllers.AddNew.AddNewTerm;
import com.drewbrokamp.classmanagement.ViewControllers.List.AssessmentList;
import com.drewbrokamp.classmanagement.ViewControllers.List.CourseList;
import com.drewbrokamp.classmanagement.ViewControllers.List.InstructorList;
import com.drewbrokamp.classmanagement.ViewControllers.List.NoteList;
import com.drewbrokamp.classmanagement.ViewControllers.List.TermList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    public static int numAlert;
    List<String> titles;
    List<String> subTitles;
    MainAdapter mainAdapter;
    RecyclerView recyclerView;
    int[] icons;

    DBHelper dbHelper;

    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dbHelper = new DBHelper(this);

        icons = new int[]{R.drawable.icon_term,
                R.drawable.icon_course,
                R.drawable.icon_assessment,
                R.drawable.icon_notes_48,
                R.drawable.icon_instructor};


        titles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.main_titles)));
        subTitles = new ArrayList<>();
        subTitles.add(String.valueOf(dbHelper.getAllTerms().size()));
        subTitles.add(String.valueOf(dbHelper.getAllCourses().size()));
        subTitles.add(String.valueOf(dbHelper.getAllAssessments().size()));
        subTitles.add(String.valueOf(dbHelper.getAllNotes().size()));
        subTitles.add(String.valueOf(dbHelper.getAllInstructors().size()));



        mainAdapter = new MainAdapter(this, icons, titles, subTitles, this);
        recyclerView = findViewById(R.id.recyclerViewMainLinks);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                goToSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        titles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.main_titles)));
        subTitles = new ArrayList<>();
        subTitles.add(String.valueOf(dbHelper.getAllTerms().size()));
        subTitles.add(String.valueOf(dbHelper.getAllCourses().size()));
        subTitles.add(String.valueOf(dbHelper.getAllAssessments().size()));
        subTitles.add(String.valueOf(dbHelper.getAllNotes().size()));
        subTitles.add(String.valueOf(dbHelper.getAllInstructors().size()));



        mainAdapter = new MainAdapter(this, icons, titles, subTitles, this);
        recyclerView = findViewById(R.id.recyclerViewMainLinks);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClick(int position) {
        if (position == 0 ) {
            goToTermList();
        }

        if (position == 1) {
            goToCourseList();
        }

        if (position == 2) {
            goToAssessmentList();
        }

        if (position == 3) {
            goToNotesList();
        }

        if (position == 4) {
            goToInstructorList();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.main_add_new_term) {
            goToAddNewTerm();
        } else if (itemId == R.id.main_add_new_class) {
            goToAddNewCourse();
        } else if (itemId == R.id.main_add_new_instructor) {
            goToAddNewInstructor();
        } else if (itemId == R.id.main_add_new_assessment) {
            goToAddNewAssessment();
        } else if (itemId == R.id.main_add_new_note) {
            goToAddNewNote();
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToSearch(String query) {
        Intent intent = new Intent(this,SearchActivity.class);
        intent.putExtra("searchQuery", query);
        startActivity(intent);
    }

    private void goToTermList() {
        Intent intent = new Intent(this, TermList.class);
        startActivity(intent);

    }

    private void goToCourseList() {
        Intent intent = new Intent(this, CourseList.class);
        startActivity(intent);
    }

    private void goToAssessmentList() {
        Intent intent = new Intent(this, AssessmentList.class);
        startActivity(intent);
    }

    private void goToNotesList() {
        Intent intent = new Intent(this, NoteList.class);
        startActivity(intent);
    }

    private void goToInstructorList() {
        Intent intent = new Intent(this, InstructorList.class);
        startActivity(intent);
    }

    private void goToAddNewTerm() {
        Intent intent = new Intent(this, AddNewTerm.class);
        startActivity(intent);
    }

    private void goToAddNewCourse() {
        Intent intent = new Intent(this, AddNewCourse.class);
        startActivity(intent);
    }

    private void goToAddNewInstructor() {
        Intent intent = new Intent(this, AddNewInstructor.class);
        startActivity(intent);
    }


    private void goToAddNewAssessment() {
        Intent intent = new Intent(this, AddNewAssessment.class);
        startActivity(intent);
    }

    private void goToAddNewNote() {
        Intent intent = new Intent(this, AddNewNote.class);
        startActivity(intent);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}