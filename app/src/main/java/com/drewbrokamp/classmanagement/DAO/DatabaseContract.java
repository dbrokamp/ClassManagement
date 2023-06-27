package com.drewbrokamp.classmanagement.DAO;

import android.provider.BaseColumns;

import com.drewbrokamp.classmanagement.Util.Globals;

public final class DatabaseContract {

    private DatabaseContract() {}

    public static class Terms implements BaseColumns {
        public static final String TABLE_NAME = "terms";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START_DATE = "startDate";
        public static final String COLUMN_NAME_END_DATE = "endDate";
    }

    public static class Courses implements BaseColumns {
        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START_DATE = "startDate";
        public static final String COLUMN_NAME_END_DATE = "endDate";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_TERM_ID = "termID";
        public static final String COLUMN_NAME_INSTRUCTOR_ID = "instructorID";
    }

    public static class Instructors implements BaseColumns {
        public static final String TABLE_NAME = "instructors";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phoneNumber";
        public static final String COLUMN_NAME_EMAIL = "email";
    }

    public static class Assessments implements BaseColumns {
        public static final String TABLE_NAME = "assessments";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START_DATE = "startDate";
        public static final String COLUMN_NAME_END_DATE = "endDate";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_COURSE_ID = "courseID";
    }

    public static class Notes implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_COURSE_ID = "courseID";
    }
}
