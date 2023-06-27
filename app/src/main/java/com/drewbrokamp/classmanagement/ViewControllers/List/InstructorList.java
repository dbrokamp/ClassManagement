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

import com.drewbrokamp.classmanagement.Adapters.InstructorAdapter;
import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Model.Instructor;
import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.Util.InstructorRecyclerViewInterface;
import com.drewbrokamp.classmanagement.ViewControllers.AddNew.AddNewInstructor;
import com.drewbrokamp.classmanagement.ViewControllers.Detail.InstructorDetail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InstructorList extends AppCompatActivity implements InstructorRecyclerViewInterface {

    private List<Instructor> instructorList;
    private DBHelper dbHelper;
    private InstructorAdapter instructorAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_list);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);

        instructorList = new ArrayList<>();
        instructorList = dbHelper.getAllInstructors();
        Collections.sort(instructorList, Comparator.comparing(Instructor::getName));
        instructorAdapter = new InstructorAdapter(this, instructorList, this);
        recyclerView = findViewById(R.id.instructorListRecyclerView);
        recyclerView.setAdapter(instructorAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.instructor_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        } else if (item.getItemId() == R.id.instructor_list_menu_add_instructor) {
            goToAddNewInstructor();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInstructorClick(int position) {
        Instructor instructor = instructorList.get(position);
        Intent intent = new Intent(this, InstructorDetail.class);
        intent.putExtra("instructorID", instructor.getId().toString());
        startActivity(intent);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        instructorList = new ArrayList<>();
        instructorList = dbHelper.getAllInstructors();
        Collections.sort(instructorList, Comparator.comparing(Instructor::getName));
        instructorAdapter = new InstructorAdapter(this, instructorList, this);
        recyclerView = findViewById(R.id.instructorListRecyclerView);
        recyclerView.setAdapter(instructorAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void goToAddNewInstructor() {
        Intent intent = new Intent(this, AddNewInstructor.class);
        startActivity(intent);
    }
}