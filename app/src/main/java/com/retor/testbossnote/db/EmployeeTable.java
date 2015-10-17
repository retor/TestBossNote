package com.retor.testbossnote.db;

/**
 * Created by retor on 14.10.2015.
 */
public class EmployeeTable {
    public static final String TABLE = "employers";
    public static final String EMPLOYEE_ID = "_id";
    public static final String EMPLOYEE_NAME = "name";
    public static final String EMPLOYEE_SURNAME = "surname";
    public static final String EMPLOYEE_PATRONYMIC = "patronymic";
    public static final String EMPLOYEE_PICTURE = "picture";
    public static final String EMPLOYEE_SALARY = "salary";
    public static final String EMPLOYEE_AGE = "age";

    public static String getCreationTableQuery(){
        return "CREATE TABLE " + TABLE + "("
                + EMPLOYEE_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + EMPLOYEE_NAME + " TEXT, "
                + EMPLOYEE_SURNAME + " TEXT, "
                + EMPLOYEE_PATRONYMIC + " TEXT, "
                + EMPLOYEE_PICTURE + " TEXT, "
                + EMPLOYEE_SALARY + " INTEGER, "
                + EMPLOYEE_AGE  + " INTEGER"
                + ");";
    }
}
