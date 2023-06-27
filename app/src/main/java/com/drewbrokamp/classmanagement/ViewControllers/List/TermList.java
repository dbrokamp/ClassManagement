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

import com.drewbrokamp.classmanagement.Adapters.TermAdapter;
import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Model.Term;
import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.Util.TermRecyclerViewInterface;
import com.drewbrokamp.classmanagement.ViewControllers.AddNew.AddNewTerm;
import com.drewbrokamp.classmanagement.ViewControllers.Detail.TermDetail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TermList extends AppCompatActivity implements TermRecyclerViewInterface {

    List<Term> terms = new ArrayList<>();
    DBHelper dbHelper = new DBHelper(this);
    TermAdapter termAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        initializeListAdapter();
    }

    private void initializeListAdapter() {
        terms = dbHelper.getAllTerms();
        Collections.sort(terms, Comparator.comparing(Term::getStartDate));
        termAdapter = new TermAdapter(this, terms, this);
        recyclerView = findViewById(R.id.termListRecyclerView);
        recyclerView.setAdapter(termAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.term_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.finish();
        } else if (itemId == R.id.term_list_menu_add_term) {
            goToAddNewTerm();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onTermClick(int position) {
        Term term = terms.get(position);
        Intent intent = new Intent(this, TermDetail.class);
        intent.putExtra("termID", term.getId().toString());
        startActivity(intent);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initializeListAdapter();
    }

    private void goToAddNewTerm() {
        Intent intent = new Intent(this, AddNewTerm.class);
        startActivity(intent);
    }
}