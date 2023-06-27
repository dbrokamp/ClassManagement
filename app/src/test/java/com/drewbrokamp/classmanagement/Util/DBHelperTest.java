package com.drewbrokamp.classmanagement.Util;

import static org.junit.Assert.*;

import com.drewbrokamp.classmanagement.Model.Term;

import org.junit.Test;

import java.sql.Date;

public class DBHelperTest {
    @Test
    public void addTerm() {
        // Start date of new term is after end date of current term, assert true
        Term newTerm = new Term("February", Date.valueOf("2023-02-01"), Date.valueOf("2023-02-28"));
        Term currentTerm = new Term("January", Date.valueOf("2023-01-01"), Date.valueOf("2023-01-31"));
        assertTrue(newTerm.getStartDate().after(currentTerm.getEndDate()));

        // Start date of new term is before end date of current term, assert false
        newTerm.setStartDate(Date.valueOf("2023-01-30"));
        assertFalse(newTerm.getStartDate().after(currentTerm.getEndDate()));
    }

}