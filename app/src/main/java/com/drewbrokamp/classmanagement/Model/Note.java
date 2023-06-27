package com.drewbrokamp.classmanagement.Model;

public class Note {

    private Integer id;
    private Integer courseID;
    private String title;
    private String content;

    public Note(Integer id, String title, String content, Integer courseID) {
        this.id = id;
        this.courseID = courseID;
        this.title = title;
        this.content = content;
    }

    public Note(Integer courseID, String title, String content) {
        this.courseID = courseID;
        this.title = title;
        this.content = content;
    }

    public Note() { }


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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
