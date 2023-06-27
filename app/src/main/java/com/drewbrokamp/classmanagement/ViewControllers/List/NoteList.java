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

import com.drewbrokamp.classmanagement.Adapters.NoteAdapter;
import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Model.Note;
import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.Util.NoteRecyclerViewInterface;
import com.drewbrokamp.classmanagement.ViewControllers.AddNew.AddNewNote;
import com.drewbrokamp.classmanagement.ViewControllers.Detail.NoteDetail;

import java.util.List;

public class NoteList extends AppCompatActivity implements NoteRecyclerViewInterface {

    List<Note> noteList;
    DBHelper dbHelper;
    NoteAdapter noteAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);
        noteList = dbHelper.getAllNotes();
        noteAdapter = new NoteAdapter(this, noteList,this);
        recyclerView = findViewById(R.id.noteListRecyclerView);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        } if (item.getItemId() == R.id.note_list_menu_add_note) {
            goToAddNewNote();
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToAddNewNote() {
        Intent intent = new Intent(this, AddNewNote.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        noteList = dbHelper.getAllNotes();
        noteAdapter = new NoteAdapter(this, noteList,this);
        recyclerView = findViewById(R.id.noteListRecyclerView);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onNoteClick(int position) {
        Note note = noteList.get(position);
        Intent intent = new Intent(this, NoteDetail.class);
        intent.putExtra("noteID", note.getId().toString());
        startActivity(intent);

    }
}