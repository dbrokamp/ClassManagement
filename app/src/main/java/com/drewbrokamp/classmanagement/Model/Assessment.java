package com.drewbrokamp.classmanagement.Model;

import java.sql.Date;

public class Assessment {

    private Integer id;
    private Integer courseID;
    private String title;
    private String type;
    private Date startDate;
    private Date endDate;

    public Assessment(Integer id, Integer courseID, String title, String type, Date startDate, Date endDate) {
        this.id = id;
        this.courseID = courseID;
        this.title = title;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Assessment(Integer id, String title, String type, Date startDate, Date endDate, Integer courseID) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.courseID = courseID;
    }


    public Assessment(Integer courseID, String title, String type, Date startDate, Date endDate) {
        this.courseID = courseID;
        this.title = title;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Assessment() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseID() {
        return courseID;
    }

    public void setCourseID(Integer courseID) {
        this.courseID = courseID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
