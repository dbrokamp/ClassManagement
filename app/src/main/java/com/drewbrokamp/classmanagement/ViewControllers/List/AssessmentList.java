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

import com.drewbrokamp.classmanagement.Adapters.AssessmentAdapter;
import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Model.Assessment;
import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.Util.AssessmentRecyclerViewInterface;
import com.drewbrokamp.classmanagement.ViewControllers.AddNew.AddNewAssessment;
import com.drewbrokamp.classmanagement.ViewControllers.Detail.AssessmentDetail;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//TODO: sort assessment list by date

public class AssessmentList extends AppCompatActivity implements AssessmentRecyclerViewInterface {

    private List<Assessment> assessments;
    private DBHelper dbHelper;
    private AssessmentAdapter assessmentAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);
        assessments = dbHelper.getAllAssessments();
        Collections.sort(assessments, Comparator.comparing(Assessment::getStartDate));
        assessmentAdapter = new AssessmentAdapter(this, assessments, this);
        recyclerView = findViewById(R.id.assessmentListRecyclerView);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        assessments = dbHelper.getAllAssessments();
        Collections.sort(assessments, Comparator.comparing(Assessment::getStartDate));
        assessmentAdapter = new AssessmentAdapter(this, assessments, this);
        recyclerView = findViewById(R.id.assessmentListRecyclerView);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.assessment_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        } else if (item.getItemId() == R.id.assessment_list_menu_add_assessment) {
            goToAddNewAssessment();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAssessmentClick(int position) {
        Integer assessmentID = assessments.get(position).getId();
        Intent intent = new Intent(this, AssessmentDetail.class);
        intent.putExtra("assessmentID", assessmentID.toString());
        startActivity(intent);

    }

    private void goToAddNewAssessment() {
        Intent intent = new Intent(this, AddNewAssessment.class);
        startActivity(intent);
    }
}