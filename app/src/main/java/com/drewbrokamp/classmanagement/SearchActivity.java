package com.drewbrokamp.classmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.drewbrokamp.classmanagement.Adapters.AssessmentAdapter;
import com.drewbrokamp.classmanagement.Adapters.CourseAdapter;
import com.drewbrokamp.classmanagement.Adapters.InstructorAdapter;
import com.drewbrokamp.classmanagement.Adapters.NoteAdapter;
import com.drewbrokamp.classmanagement.Adapters.TermAdapter;
import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Model.Assessment;
import com.drewbrokamp.classmanagement.Model.Course;
import com.drewbrokamp.classmanagement.Model.Instructor;
import com.drewbrokamp.classmanagement.Model.Note;
import com.drewbrokamp.classmanagement.Model.Term;
import com.drewbrokamp.classmanagement.Util.AssessmentRecyclerViewInterface;
import com.drewbrokamp.classmanagement.Util.CourseRecyclerViewInterface;
import com.drewbrokamp.classmanagement.Util.InstructorRecyclerViewInterface;
import com.drewbrokamp.classmanagement.Util.NoteRecyclerViewInterface;
import com.drewbrokamp.classmanagement.Util.TermRecyclerViewInterface;
import com.drewbrokamp.classmanagement.ViewControllers.Detail.AssessmentDetail;
import com.drewbrokamp.classmanagement.ViewControllers.Detail.CourseDetail;
import com.drewbrokamp.classmanagement.ViewControllers.Detail.NoteDetail;
import com.drewbrokamp.classmanagement.ViewControllers.Detail.TermDetail;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements TermRecyclerViewInterface, CourseRecyclerViewInterface, AssessmentRecyclerViewInterface, NoteRecyclerViewInterface, InstructorRecyclerViewInterface {

    private TextView querySearch;
    private String query;
    private DBHelper dbHelper;

    private List<Term> termsList;
    private RecyclerView termsRecyclerView;
    private TermAdapter termAdapter;

    private List<Course> coursesList;
    private RecyclerView coursesRecyclerView;
    private CourseAdapter courseAdapter;

    private List<Assessment> assessmentsList;
    private RecyclerView assessmentsRecyclerView;
    private AssessmentAdapter assessmentAdapter;

    private List<Note> notesList;
    private RecyclerView notesRecyclerView;
    private NoteAdapter noteAdapter;

    private List<Instructor> instructorsList;
    private RecyclerView instructorsRecyclerView;
    private InstructorAdapter instructorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbHelper = new DBHelper(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        setIds();

        getQueryFromIntent();

        getResultLists();
        initializeAdapters();
        setAdapters();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            backButtonClicked();
        }

        return super.onOptionsItemSelected(item);

    }

    private void setIds() {
        querySearch = findViewById(R.id.queryText);
        termsRecyclerView = findViewById(R.id.termSearchResultsList);
        coursesRecyclerView = findViewById(R.id.courseSearchResultsList);
        assessmentsRecyclerView = findViewById(R.id.assessmentSearchResultsList);
        notesRecyclerView = findViewById(R.id.noteSearchResultsList);
        instructorsRecyclerView = findViewById(R.id.instructorSearchResultsList);
    }

    private void getQueryFromIntent() {
        query = getIntent().getStringExtra("searchQuery");
        String text = "Searched for: " + query;
        querySearch.setText(text);
    }

    private void getResultLists() {
        termsList = dbHelper.searchTerms(query);
        Collections.sort(termsList, Comparator.comparing(Term::getTitle));

        coursesList = dbHelper.searchCourses(query);
        Collections.sort(coursesList, Comparator.comparing(Course::getTitle));

        assessmentsList = dbHelper.searchAssessments(query);
        Collections.sort(assessmentsList, Comparator.comparing(Assessment::getTitle));

        notesList = dbHelper.searchNotes(query);
        Collections.sort(notesList, Comparator.comparing(Note::getTitle));

        instructorsList = dbHelper.searchInstructors(query);
        Collections.sort(instructorsList, Comparator.comparing(Instructor::getName));

    }

    private void initializeAdapters() {
        termAdapter = new TermAdapter(this, termsList, this);
        courseAdapter = new CourseAdapter(this, coursesList, this, dbHelper);
        assessmentAdapter = new AssessmentAdapter(this, assessmentsList, this);
        noteAdapter = new NoteAdapter(this, notesList, this);
        instructorAdapter = new InstructorAdapter(this, instructorsList, this);
    }

    private void setAdapters() {
        termsRecyclerView.setAdapter(termAdapter);
        termsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        coursesRecyclerView.setAdapter(courseAdapter);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        assessmentsRecyclerView.setAdapter(assessmentAdapter);
        assessmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        notesRecyclerView.setAdapter(noteAdapter);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        instructorsRecyclerView.setAdapter(instructorAdapter);
        instructorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void backButtonClicked() {
        finish();
    }


    @Override
    public void onCourseClick(int position) {
        Course course = coursesList.get(position);
        Intent intent = new Intent(this, CourseDetail.class);
        intent.putExtra("courseID", course.getId().toString());
        startActivity(intent);
    }

    @Override
    public void onTermClick(int position) {
        Term term = termsList.get(position);
        Intent intent = new Intent(this, TermDetail.class);
        intent.putExtra("termID", term.getId().toString());
        startActivity(intent);

    }

    @Override
    public void onAssessmentClick(int position) {
        Integer assessmentID = assessmentsList.get(position).getId();
        Intent intent = new Intent(this, AssessmentDetail.class);
        intent.putExtra("assessmentID", assessmentID.toString());
        startActivity(intent);


    }

    @Override
    public void onNoteClick(int position) {
        Note note = notesList.get(position);
        Intent intent = new Intent(this, NoteDetail.class);
        intent.putExtra("noteID", note.getId().toString());
        startActivity(intent);
    }

    @Override
    public void onInstructorClick(int position) {

    }

}