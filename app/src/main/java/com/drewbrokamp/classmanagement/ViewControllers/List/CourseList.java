package com.drewbrokamp.classmanagement.ViewControllers.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.drewbrokamp.classmanagement.Adapters.CourseAdapter;
import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Model.Course;
import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.Util.CourseRecyclerViewInterface;
import com.drewbrokamp.classmanagement.ViewControllers.AddNew.AddNewCourse;
import com.drewbrokamp.classmanagement.ViewControllers.Detail.CourseDetail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CourseList extends AppCompatActivity implements CourseRecyclerViewInterface {

    List<Course> courses = new ArrayList<>();
    DBHelper dbHelper;
    CourseAdapter courseAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);

        initializeListAdapter();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        initializeListAdapter();
    }

    private void initializeListAdapter() {
        courses = dbHelper.getAllCourses();
        Collections.sort(courses, Comparator.comparing(Course::getStartDate) );
        courseAdapter = new CourseAdapter(this, courses, this, dbHelper);
        recyclerView = findViewById(R.id.courseListRecyclerView);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onCourseClick(int position) {
        Course course = courses.get(position);
        Intent intent = new Intent(this, CourseDetail.class);
        intent.putExtra("courseID", course.getId().toString());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.finish();
        } else if (itemId == R.id.course_list_menu_add_course) {
            goToAddNewCourse();
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToAddNewCourse() {
        Intent intent = new Intent(this, AddNewCourse.class);
        startActivity(intent);
    }
}