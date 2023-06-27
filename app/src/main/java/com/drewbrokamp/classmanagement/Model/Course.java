package com.drewbrokamp.classmanagement.Model;

import java.sql.Date;

public class Course {

    private Integer id;
    private Integer termID;
    private String title;
    private Date startDate;
    private Date endDate;
    private String status;
    private Integer instructorID;

    public Course(Integer id, String title, Date startDate, Date endDate, String status, Integer termID, Integer instructorID) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.termID = termID;
        this.instructorID = instructorID;
    }

    public Course(String title, Date startDate, Date endDate, String status, Integer termID, Integer instructorID) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.termID = termID;
        this.instructorID = instructorID;
    }

    public Course() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTermID() {
        return termID;
    }

    public void setTermID(Integer termID) {
        this.termID = termID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getInstructorID() {
        return instructorID;
    }

    public void setInstructorID(Integer instructorID) {
        this.instructorID = instructorID;
    }
}
