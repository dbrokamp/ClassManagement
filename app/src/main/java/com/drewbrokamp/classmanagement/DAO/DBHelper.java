package com.drewbrokamp.classmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.drewbrokamp.classmanagement.Model.Assessment;
import com.drewbrokamp.classmanagement.Model.Course;
import com.drewbrokamp.classmanagement.Model.Instructor;
import com.drewbrokamp.classmanagement.Model.Note;
import com.drewbrokamp.classmanagement.Model.Term;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "classes.db";
    private static final int DB_VERSION = 1;
    private final static String SQL_CREATE_TERMS_TABLE =
            "CREATE TABLE " + DatabaseContract.Terms.TABLE_NAME + " (" +
                    DatabaseContract.Terms._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.Terms.COLUMN_NAME_TITLE + " TEXT," +
                    DatabaseContract.Terms.COLUMN_NAME_START_DATE + " DATE," +
                    DatabaseContract.Terms.COLUMN_NAME_END_DATE + " DATE)";
    private final static String SQL_DELETE_TERMS_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.Terms.TABLE_NAME;
    private final static String SQL_CREATE_INSTRUCTORS_TABLE =
            "CREATE TABLE " + DatabaseContract.Instructors.TABLE_NAME + "(" +
                    DatabaseContract.Instructors._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.Instructors.COLUMN_NAME_NAME + " TEXT," +
                    DatabaseContract.Instructors.COLUMN_NAME_PHONE_NUMBER + " TEXT," +
                    DatabaseContract.Instructors.COLUMN_NAME_EMAIL + " TEXT)";
    private final static String SQL_DELETE_INSTRUCTORS_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.Instructors.TABLE_NAME;
    private final static String SQL_CREATE_COURSES_TABLE =
            "CREATE TABLE " + DatabaseContract.Courses.TABLE_NAME + " (" +
                    DatabaseContract.Courses._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.Courses.COLUMN_NAME_TITLE + " TEXT," +
                    DatabaseContract.Courses.COLUMN_NAME_START_DATE + " DATE," +
                    DatabaseContract.Courses.COLUMN_NAME_END_DATE + " DATE," +
                    DatabaseContract.Courses.COLUMN_NAME_STATUS + " TEXT," +
                    DatabaseContract.Courses.COLUMN_NAME_TERM_ID + " INTEGER NOT NULL," +
                    DatabaseContract.Courses.COLUMN_NAME_INSTRUCTOR_ID + " INTEGER NOT NULL," +
                    " FOREIGN KEY(" + DatabaseContract.Courses.COLUMN_NAME_TERM_ID + ") REFERENCES " + DatabaseContract.Terms._ID + "," +
                    " FOREIGN KEY(" + DatabaseContract.Courses.COLUMN_NAME_INSTRUCTOR_ID + ") REFERENCES " + DatabaseContract.Instructors._ID + ");";
    private final static String SQL_DELETE_COURSES_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.Courses.TABLE_NAME;
    private final static String SQL_CREATE_ASSESSMENTS_TABLE =
            "CREATE TABLE " + DatabaseContract.Assessments.TABLE_NAME + " (" +
                    DatabaseContract.Assessments._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.Assessments.COLUMN_NAME_TITLE + " TEXT," +
                    DatabaseContract.Assessments.COLUMN_NAME_TYPE + " TEXT," +
                    DatabaseContract.Assessments.COLUMN_NAME_START_DATE + " TEXT," +
                    DatabaseContract.Assessments.COLUMN_NAME_END_DATE + " TEXT," +
                    DatabaseContract.Assessments.COLUMN_NAME_COURSE_ID + " INTEGER NOT NULL," +
                    " FOREIGN KEY(" + DatabaseContract.Assessments.COLUMN_NAME_COURSE_ID + ") REFERENCES " + DatabaseContract.Courses._ID + ");";
    private final static String SQL_DELETE_ASSESSMENTS_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.Assessments.TABLE_NAME;
    private final static String SQL_CREATE_NOTES_TABLE =
            "CREATE TABLE " + DatabaseContract.Notes.TABLE_NAME + " (" +
                    DatabaseContract.Notes._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.Notes.COLUMN_NAME_TITLE + " TEXT," +
                    DatabaseContract.Notes.COLUMN_NAME_CONTENT + " TEXT," +
                    DatabaseContract.Notes.COLUMN_NAME_COURSE_ID + " INTEGER NOT NULL," +
                    " FOREIGN KEY(" + DatabaseContract.Notes.COLUMN_NAME_COURSE_ID + ") REFERENCES " + DatabaseContract.Courses._ID + ");";
    private final static String SQL_DELETE_NOTES_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.Notes.TABLE_NAME;


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TERMS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INSTRUCTORS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_COURSES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ASSESSMENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_NOTES_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_TERMS_TABLE);
        db.execSQL(SQL_DELETE_INSTRUCTORS_TABLE);
        db.execSQL(SQL_DELETE_COURSES_TABLE);
        db.execSQL(SQL_DELETE_ASSESSMENTS_TABLE);
        db.execSQL(SQL_DELETE_NOTES_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public boolean addTerm(Term term) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.Terms.COLUMN_NAME_TITLE, term.getTitle());
        contentValues.put(DatabaseContract.Terms.COLUMN_NAME_START_DATE, term.getStartDate().toString());
        contentValues.put(DatabaseContract.Terms.COLUMN_NAME_END_DATE, term.getEndDate().toString());

        List<Term> allTerms = getAllTerms();
        List<Integer> results = new ArrayList<>();
        long result;

        if (allTerms.isEmpty()) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            System.out.println("ALL TERMS IS EMPTY, NEW TERM SHOULD BE ADDED");
            result = sqLiteDatabase.insertOrThrow(DatabaseContract.Terms.TABLE_NAME, null, contentValues);
            sqLiteDatabase.close();
        } else {
            System.out.println("ALL TERM IS NOT EMPTY, CHECK DATES");
            for (Term currentTerm : allTerms) {
                if (term.getStartDate().after(currentTerm.getEndDate())) {
                    results.add(1);
                } else {
                    results.add(0);
                }
            }
            if (results.contains(0)) {
                System.out.println("DO  NOT ADD NEW TERM");
                result = -1;
            } else {
                SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
                System.out.println("DATES ARE CORRECT, NEW TERM SHOULD BE ADDED");
                result = sqLiteDatabase.insertOrThrow(DatabaseContract.Terms.TABLE_NAME, null, contentValues);
                sqLiteDatabase.close();
            }
        }

        return result != -1;

    }


    public boolean addInstructor(Instructor instructor) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.Instructors.COLUMN_NAME_NAME, instructor.getName());
        contentValues.put(DatabaseContract.Instructors.COLUMN_NAME_EMAIL, instructor.getEmail());
        contentValues.put(DatabaseContract.Instructors.COLUMN_NAME_PHONE_NUMBER, instructor.getPhoneNumber());


        long result = sqLiteDatabase.insertOrThrow(DatabaseContract.Instructors.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        return result != -1;
    }

    public List<Term> getAllTerms() {
        List<Term> terms = new ArrayList<>();

        String queryString = "SELECT * FROM " + DatabaseContract.Terms.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int termID = cursor.getInt(0);
                String termName = cursor.getString(1);
                String termStartDate = cursor.getString(2);
                String termEndDate = cursor.getString(3);

                Term term = new Term(termID, termName, Date.valueOf(termStartDate), Date.valueOf(termEndDate));
                terms.add(term);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return terms;
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();

        String queryString = "SELECT * FROM " + DatabaseContract.Courses.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int courseID = cursor.getInt(0);
                String courseName = cursor.getString(1);
                String courseStartDate = cursor.getString(2);
                String courseEndDate = cursor.getString(3);
                String courseState = cursor.getString(4);
                int termID = cursor.getInt(5);
                int courseInstructorID = cursor.getInt(6);

                Course course = new Course(courseID, courseName, Date.valueOf(courseStartDate), Date.valueOf(courseEndDate), courseState, termID, courseInstructorID);
                courses.add(course);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();


        return courses;

    }

    public List<Course> getCoursesForTerm(Integer termID) {
        List<Course> courses = new ArrayList<>();

        String sql = "SELECT * FROM " + DatabaseContract.Courses.TABLE_NAME + " WHERE " + DatabaseContract.Courses.COLUMN_NAME_TERM_ID + " = ?";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{termID.toString()});

        if (cursor.moveToFirst()) {
            do {
                int courseID = cursor.getInt(0);
                String courseName = cursor.getString(1);
                String courseStartDate = cursor.getString(2);
                String courseEndDate = cursor.getString(3);
                String courseState = cursor.getString(4);
                int courseTermID = cursor.getInt(5);
                int courseInstructorID = cursor.getInt(6);

                Course course = new Course(courseID, courseName, Date.valueOf(courseStartDate), Date.valueOf(courseEndDate), courseState, courseTermID, courseInstructorID);
                courses.add(course);
            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();

        return courses;
    }

    public void updateTerm(Integer termID, Term newTerm) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.Terms.COLUMN_NAME_TITLE, newTerm.getTitle());
        cv.put(DatabaseContract.Terms.COLUMN_NAME_START_DATE, newTerm.getStartDate().toString());
        cv.put(DatabaseContract.Terms.COLUMN_NAME_END_DATE, newTerm.getEndDate().toString());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(DatabaseContract.Terms.TABLE_NAME, cv, "_id = ?", new String[]{termID.toString()});
        db.close();
    }

    public void updateCourse(Course course) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.Courses.COLUMN_NAME_TITLE, course.getTitle());
        cv.put(DatabaseContract.Courses.COLUMN_NAME_START_DATE, course.getStartDate().toString());
        cv.put(DatabaseContract.Courses.COLUMN_NAME_END_DATE, course.getEndDate().toString());
        cv.put(DatabaseContract.Courses.COLUMN_NAME_STATUS, course.getStatus());
        cv.put(DatabaseContract.Courses.COLUMN_NAME_TERM_ID, course.getTermID());
        cv.put(DatabaseContract.Courses.COLUMN_NAME_INSTRUCTOR_ID, course.getInstructorID());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(DatabaseContract.Courses.TABLE_NAME, cv, "_id = ?", new String[]{course.getId().toString()});
        db.close();

    }

    public void updateAssessment(Integer assessmentID, Assessment assessment) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.Assessments.COLUMN_NAME_TITLE, assessment.getTitle());
        cv.put(DatabaseContract.Assessments.COLUMN_NAME_TYPE, assessment.getType());
        cv.put(DatabaseContract.Assessments.COLUMN_NAME_START_DATE, assessment.getStartDate().toString());
        cv.put(DatabaseContract.Assessments.COLUMN_NAME_END_DATE, assessment.getEndDate().toString());
        cv.put(DatabaseContract.Assessments.COLUMN_NAME_COURSE_ID, assessment.getCourseID());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(DatabaseContract.Assessments.TABLE_NAME, cv, "_id = ?", new String[]{assessmentID.toString()});
        db.close();
    }

    public void updateNote(Integer noteID, Note note) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.Notes.COLUMN_NAME_TITLE, note.getTitle());
        cv.put(DatabaseContract.Notes.COLUMN_NAME_CONTENT, note.getContent());
        cv.put(DatabaseContract.Notes.COLUMN_NAME_COURSE_ID, note.getCourseID());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(DatabaseContract.Notes.TABLE_NAME, cv, "_id = ?", new String[]{noteID.toString()});
        db.close();
    }

    public void updateInstructor(Integer instructorID, Instructor instructor) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.Instructors.COLUMN_NAME_NAME, instructor.getName());
        cv.put(DatabaseContract.Instructors.COLUMN_NAME_PHONE_NUMBER, instructor.getPhoneNumber());
        cv.put(DatabaseContract.Instructors.COLUMN_NAME_EMAIL, instructor.getEmail());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(DatabaseContract.Instructors.TABLE_NAME, cv, "_id = ?", new String[]{instructorID.toString()});
        db.close();
    }

    public List<String> getCourseNames() {
        List<Course> courses = getAllCourses();
        List<String> courseNames = new ArrayList<>();

        for (Course course : courses) {
            if (course.getTitle() != null) {
                courseNames.add(course.getTitle());
            }
        }

        return courseNames;
    }

    public List<String> getTermNames() {
        List<Term> terms = getAllTerms();
        List<String> termNames = new ArrayList<>();

        for (Term term : terms) {
            if (term.getTitle() != null) {
                termNames.add(term.getTitle());
            }
        }

        return termNames;
    }


    public List<Instructor> getAllInstructors() {
        List<Instructor> instructors = new ArrayList<>();

        String queryString = "SELECT * FROM " + DatabaseContract.Instructors.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToNext()) {
            do {

                int instructorID = cursor.getInt(0);
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                String phoneNumber = cursor.getString(3);

                Instructor instructor = new Instructor(instructorID, name, email, phoneNumber);
                instructors.add(instructor);


            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return instructors;
    }

    public List<String> getInstructorNames() {
        List<Instructor> instructors = getAllInstructors();
        List<String> names = new ArrayList<>();

        for (Instructor instructor : instructors) {
            String name = instructor.getName();
            names.add(name);
        }
        return names;
    }

    public Instructor getInstructorFromId(int id) {
        Instructor instructor = new Instructor();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.Instructors.TABLE_NAME + " WHERE " + DatabaseContract.Instructors._ID + " = " + id;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            instructor.setId(cursor.getInt(0));
            instructor.setName(cursor.getString(1));
            instructor.setPhoneNumber(cursor.getString(2));
            instructor.setEmail(cursor.getString(3));
        }
        cursor.close();
        sqLiteDatabase.close();
        return instructor;
    }

    public Term getTermFromId(int id) {
        Term term = new Term();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.Terms.TABLE_NAME + " WHERE " + DatabaseContract.Terms._ID + " = " + id;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            term.setId(cursor.getInt(0));
            term.setTitle(cursor.getString(1));
            term.setStartDate(Date.valueOf(cursor.getString(2)));
            term.setEndDate(Date.valueOf(cursor.getString(3)));
        }

        cursor.close();
        sqLiteDatabase.close();
        return term;
    }

    public Course getCourseFromId(int id) {
        Course course = new Course();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.Courses.TABLE_NAME + " WHERE " + DatabaseContract.Courses._ID + " = " + id;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            int courseID = cursor.getInt(0);
            String courseName = cursor.getString(1);
            String courseStartDate = cursor.getString(2);
            String courseEndDate = cursor.getString(3);
            String courseState = cursor.getString(4);
            int courseTermID = cursor.getInt(5);
            int courseInstructorID = cursor.getInt(6);

            course = new Course(courseID, courseName, Date.valueOf(courseStartDate), Date.valueOf(courseEndDate), courseState, courseTermID, courseInstructorID);

        }

        cursor.close();
        sqLiteDatabase.close();
        return course;
    }

    public Integer getTermIdFromString(String termName) {
        Term term = new Term();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.Terms.TABLE_NAME + " WHERE " + DatabaseContract.Terms.COLUMN_NAME_TITLE + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{termName});

        if (cursor.moveToFirst()) {
            term.setId(cursor.getInt(0));
            term.setTitle(cursor.getString(1));
            term.setStartDate(Date.valueOf(cursor.getString(2)));
            term.setEndDate(Date.valueOf(cursor.getString(3)));
        }
        cursor.close();
        sqLiteDatabase.close();
        return term.getId();

    }

    public Integer getInstructorIdFromString(String instructorName) {
        Integer instructorID = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.Instructors.TABLE_NAME + " WHERE " + DatabaseContract.Instructors.COLUMN_NAME_NAME + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{instructorName});

        if (cursor.moveToFirst()) {
            instructorID = cursor.getInt(0);
        }

        cursor.close();
        sqLiteDatabase.close();
        return instructorID;

    }

    public Integer getCourseIdFromString(String courseName) {
        Integer courseID = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.Courses.TABLE_NAME + " WHERE " + DatabaseContract.Courses.COLUMN_NAME_TITLE + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{courseName});

        if (cursor.moveToFirst()) {
            courseID = cursor.getInt(0);
        }

        cursor.close();
        sqLiteDatabase.close();
        return courseID;
    }

    public boolean addCourse(Course course) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.Courses.COLUMN_NAME_TITLE, course.getTitle());
        contentValues.put(DatabaseContract.Courses.COLUMN_NAME_START_DATE, String.valueOf(course.getStartDate()));
        contentValues.put(DatabaseContract.Courses.COLUMN_NAME_END_DATE, String.valueOf(course.getEndDate()));
        contentValues.put(DatabaseContract.Courses.COLUMN_NAME_STATUS, String.valueOf(course.getStatus()));
        contentValues.put(DatabaseContract.Courses.COLUMN_NAME_TERM_ID, course.getTermID());
        contentValues.put(DatabaseContract.Courses.COLUMN_NAME_INSTRUCTOR_ID, course.getInstructorID());

        long result = sqLiteDatabase.insertOrThrow(DatabaseContract.Courses.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        return result != -1;
    }

    public List<Assessment> getAssessmentsForCourseID(int courseID) {
        List<Assessment> assessments = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.Assessments.TABLE_NAME + " WHERE " + DatabaseContract.Assessments.COLUMN_NAME_COURSE_ID + " = " + courseID;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                Assessment assessment = new Assessment(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Date.valueOf(cursor.getString(3)),
                        Date.valueOf(cursor.getString(4)),
                        cursor.getInt(5));

                assessments.add(assessment);
            } while (cursor.moveToNext());
        }


        cursor.close();
        sqLiteDatabase.close();

        return assessments;
    }

    public Assessment getAssessmentFromID(int assessmentID) {
        Assessment assessment = new Assessment();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.Assessments.TABLE_NAME + " WHERE " + DatabaseContract.Assessments._ID + " = " + assessmentID;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            assessment.setId(cursor.getInt(0));
            assessment.setTitle(cursor.getString(1));
            assessment.setType(cursor.getString(2));
            assessment.setStartDate(Date.valueOf(cursor.getString(3)));
            assessment.setEndDate(Date.valueOf(cursor.getString(4)));
            assessment.setCourseID(cursor.getInt(5));

        }

        cursor.close();
        sqLiteDatabase.close();

        return assessment;
    }

    public List<Assessment> getAllAssessments() {
        List<Assessment> assessments = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.Assessments.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                assessments.add(new Assessment(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Date.valueOf(cursor.getString(3)),
                        Date.valueOf(cursor.getString(4)),
                        cursor.getInt(5)));

            } while (cursor.moveToNext());

        }

        cursor.close();
        sqLiteDatabase.close();

        return assessments;
    }

    public List<Note> getNotesForCourseID(int courseID) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.Notes.TABLE_NAME + " WHERE " + DatabaseContract.Notes.COLUMN_NAME_COURSE_ID + " = " + courseID;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                notes.add(new Note(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();

        return notes;
    }

    public Note getNoteFromID(int noteID) {
        Note note = new Note();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.Notes.TABLE_NAME + " WHERE " + DatabaseContract.Notes._ID + " = " + noteID;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            note.setId(cursor.getInt(0));
            note.setTitle(cursor.getString(1));
            note.setContent(cursor.getString(2));
            note.setCourseID(cursor.getInt(3));
        }

        cursor.close();
        sqLiteDatabase.close();

        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.Notes.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                notes.add(new Note(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();

        return notes;
    }

    public boolean addAssessment(Assessment assessment) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.Assessments.COLUMN_NAME_TITLE, assessment.getTitle());
        contentValues.put(DatabaseContract.Assessments.COLUMN_NAME_START_DATE, String.valueOf(assessment.getStartDate()));
        contentValues.put(DatabaseContract.Assessments.COLUMN_NAME_END_DATE, String.valueOf(assessment.getEndDate()));
        contentValues.put(DatabaseContract.Assessments.COLUMN_NAME_TYPE, assessment.getType());
        contentValues.put(DatabaseContract.Assessments.COLUMN_NAME_COURSE_ID, assessment.getCourseID());

        long result = sqLiteDatabase.insertOrThrow(DatabaseContract.Assessments.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        return result != -1;
    }

    public boolean addNote(Note note) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.Notes.COLUMN_NAME_TITLE, note.getTitle());
        contentValues.put(DatabaseContract.Notes.COLUMN_NAME_CONTENT, note.getContent());
        contentValues.put(DatabaseContract.Notes.COLUMN_NAME_COURSE_ID, note.getCourseID());

        long result = sqLiteDatabase.insertOrThrow(DatabaseContract.Notes.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        return result != -1;

    }

    public void deleteTerm(Integer termId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String sql = "DELETE FROM " + DatabaseContract.Terms.TABLE_NAME + " WHERE " + DatabaseContract.Terms._ID + " = " + termId;
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close();
    }

    public void deleteCourse(Integer courseId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String sql = "DELETE FROM " + DatabaseContract.Courses.TABLE_NAME + " WHERE " + DatabaseContract.Courses._ID + " = " + courseId;
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close();
    }

    public void deleteAssessment(Integer assessmentId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String sql = "DELETE FROM " + DatabaseContract.Assessments.TABLE_NAME + " WHERE " + DatabaseContract.Assessments._ID + " = " + assessmentId;
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close();
    }

    public void deleteNote(Integer noteId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String sql = "DELETE FROM " + DatabaseContract.Notes.TABLE_NAME + " WHERE " + DatabaseContract.Notes._ID + " = " + noteId;
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close();
    }

    public void deleteInstructor(Integer instructorId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String sql = "DELETE FROM " + DatabaseContract.Instructors.TABLE_NAME + " WHERE " + DatabaseContract.Instructors._ID + " = " + instructorId;
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close();
    }


    public List<Term> searchTerms(String query) {
        query = "%" + query + "%";
        List<Term> terms = new ArrayList<>();

        String sql = "SELECT * FROM " + DatabaseContract.Terms.TABLE_NAME + " WHERE " + DatabaseContract.Terms.COLUMN_NAME_TITLE + " LIKE " + " ?";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{query});

        if (cursor.moveToFirst()) {
            do {
                int termID = cursor.getInt(0);
                String termName = cursor.getString(1);
                String termStartDate = cursor.getString(2);
                String termEndDate = cursor.getString(3);

                Term term = new Term(termID, termName, Date.valueOf(termStartDate), Date.valueOf(termEndDate));
                terms.add(term);

            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return terms;

    }

    public List<Course> searchCourses(String query) {
        query = "%" + query + "%";
        List<Course> courses = new ArrayList<>();

        String sql = "SELECT * FROM " + DatabaseContract.Courses.TABLE_NAME + " WHERE " + DatabaseContract.Courses.COLUMN_NAME_TITLE + " LIKE " + " ?";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{query});

        if (cursor.moveToFirst()) {
            do {
                int courseID = cursor.getInt(0);
                String courseName = cursor.getString(1);
                String courseStartDate = cursor.getString(2);
                String courseEndDate = cursor.getString(3);
                String courseState = cursor.getString(4);
                int termID = cursor.getInt(5);
                int courseInstructorID = cursor.getInt(6);

                Course course = new Course(courseID, courseName, Date.valueOf(courseStartDate), Date.valueOf(courseEndDate), courseState, termID, courseInstructorID);
                courses.add(course);

            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();

        return courses;

    }

    public List<Assessment> searchAssessments(String query) {
        query = "%" + query + "%";
        List<Assessment> assessments = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + DatabaseContract.Assessments.TABLE_NAME + " WHERE (" + DatabaseContract.Assessments.COLUMN_NAME_TITLE + " LIKE " + " ?)" + " OR (" + DatabaseContract.Assessments.COLUMN_NAME_TYPE + " LIKE " + " ?)";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{query});

        if (cursor.moveToFirst()) {
            do {
                assessments.add(new Assessment(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Date.valueOf(cursor.getString(3)),
                        Date.valueOf(cursor.getString(4)),
                        cursor.getInt(5)));

            } while (cursor.moveToNext());

        }

        cursor.close();
        sqLiteDatabase.close();

        return assessments;
    }

    public List<Note> searchNotes(String query) {

        query = "%" + query + "%";
        List<Note> notes = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + DatabaseContract.Notes.TABLE_NAME + " WHERE (" + DatabaseContract.Notes.COLUMN_NAME_TITLE + " LIKE " + " ?)" + " OR (" + DatabaseContract.Notes.COLUMN_NAME_CONTENT + " LIKE " + " ?)";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{query});

        if (cursor.moveToFirst()) {
            do {
                notes.add(new Note(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();

        return notes;

    }

    public List<Instructor> searchInstructors(String query) {

        query = "%" + query + "%";
        List<Instructor> instructors = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + DatabaseContract.Instructors.TABLE_NAME + " WHERE (" + DatabaseContract.Instructors.COLUMN_NAME_NAME + " LIKE " + " ?)" + " OR (" + DatabaseContract.Instructors.COLUMN_NAME_EMAIL + " LIKE " + " ?)";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{query});


        if (cursor.moveToNext()) {
            do {

                int instructorID = cursor.getInt(0);
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                String phoneNumber = cursor.getString(3);

                Instructor instructor = new Instructor(instructorID, name, email, phoneNumber);
                instructors.add(instructor);


            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();

        return instructors;
    }
}
