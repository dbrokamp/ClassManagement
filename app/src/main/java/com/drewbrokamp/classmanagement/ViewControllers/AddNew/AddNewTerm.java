package com.drewbrokamp.classmanagement.ViewControllers.AddNew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Model.Term;
import com.drewbrokamp.classmanagement.R;

import java.sql.Date;
import java.util.Calendar;

public class AddNewTerm extends AppCompatActivity {

    private EditText newTermTitle;
    private EditText newTermStartDate;
    private EditText newTermEndDate;

    private DatePickerDialog newTermStartDatePicker;
    private DatePickerDialog newTermEndDatePicker;

    private Date startDate;
    private Date endDate;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_term);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);

        setViewIds();

        newTermStartDate.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            newTermStartDatePicker = new DatePickerDialog(AddNewTerm.this, (datePicker, year1, month1, day1) -> {
                int newDay = datePicker.getDayOfMonth();
                int newMonth = datePicker.getMonth() + 1;
                int newYear = datePicker.getYear();
                String newDate = newYear + "-" + newMonth + "-" + newDay;
                startDate = Date.valueOf(newDate);
                String normalFormat = newMonth + "/" + newDay + "/" + newYear;
                newTermStartDate.setText(normalFormat);
            },year,month,day);
            newTermStartDatePicker.show();
        });



        newTermEndDate.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            newTermEndDatePicker = new DatePickerDialog(AddNewTerm.this, (datePicker, year2, month2, date2) -> {
                int newDay = datePicker.getDayOfMonth();
                int newMonth = datePicker.getMonth() + 1;
                int newYear = datePicker.getYear();
                String newDate = newYear + "-" + newMonth + "-" + newDay;
                endDate = Date.valueOf(newDate);
                String normalFormat = newMonth + "/" + newDay + "/" + newYear;
                newTermEndDate.setText(normalFormat);
            }, year, month, day);
            newTermEndDatePicker.show();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_term_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.finish();
        } else if (item.getItemId() == R.id.add_new_term_menu_save) {
            onAddNewTermButtonSaveClicked();
        } else if (item.getItemId() == R.id.add_new_term_menu_cancel) {
            clearSelections();
            Toast toast = Toast.makeText(this,"Assessment not saved.", Toast.LENGTH_SHORT);
            toast.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setViewIds() {
        newTermTitle = findViewById(R.id.editTextAddNewTermTitle);
        newTermStartDate = findViewById(R.id.editTextAddNewTermStartDate);
        newTermEndDate = findViewById(R.id.editTextAddNewTermEndDate);
    }

    private void clearSelections() {
        newTermTitle.getText().clear();
        newTermStartDate.getText().clear();
        newTermEndDate.getText().clear();
    }

    private void onAddNewTermButtonSaveClicked() {
        closeKeyboard();
        if (newTermTitle.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this, "Term title is required.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (newTermStartDate.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this, "Term start date is required.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (newTermEndDate.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this, "Term start date is required.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            boolean result = dbHelper.addTerm(new Term(newTermTitle.getText().toString(),
                                                startDate,
                                                endDate));
            Toast toast;
            if (result) {
                toast = Toast.makeText(this, "Term added.", Toast.LENGTH_SHORT);
                clearSelections();
            } else {
                toast = Toast.makeText(this, "Term not added. Term dates cannot overlap.", Toast.LENGTH_SHORT);
            }
            toast.show();
        }
    }


    private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }
}