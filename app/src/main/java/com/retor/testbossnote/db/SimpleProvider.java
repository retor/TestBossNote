package com.retor.testbossnote.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by retor on 14.10.2015.
 */
public class SimpleProvider extends ContentProvider {

    private static final String DB_NAME = "empdb";
    private static final int DB_VERSION = 1;
    private static final String AUTHORITY = "com.retor.providers.EmployersList";
    private static final String EMPLOYEE_PATH = "employes";

    public static final Uri EMPLOYEE_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + EMPLOYEE_PATH);

    public static final String EMPLOYEE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + EMPLOYEE_PATH;

    public static final String EMPLOYEE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + EMPLOYEE_PATH;

    public static final int URI_EMPLOYEE = 1;
    public static final int URI_EMPLOYEE_ID = 2;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, EMPLOYEE_PATH, URI_EMPLOYEE);
        uriMatcher.addURI(AUTHORITY, EMPLOYEE_PATH + "/#", URI_EMPLOYEE_ID);
    }

    EmployersDBHelper dbHelper;
    SQLiteDatabase db;

    private final String LOG_TAG = getClass().getName();

    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new EmployersDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_EMPLOYEE:
                Log.d(LOG_TAG, "URI_EMPLOYEE");
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = EmployeeTable.EMPLOYEE_NAME + " ASC";
                }
                break;
            case URI_EMPLOYEE_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_EMPLOYEE_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = EmployeeTable.EMPLOYEE_ID + " = " + id;
                } else {
                    selection = selection + " AND " + EmployeeTable.EMPLOYEE_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(EmployeeTable.TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                EMPLOYEE_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_EMPLOYEE:
                return EMPLOYEE_CONTENT_TYPE;
            case URI_EMPLOYEE_ID:
                return EMPLOYEE_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_EMPLOYEE)
            throw new IllegalArgumentException("Wrong URI: " + uri);
        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(EmployeeTable.TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(EMPLOYEE_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_EMPLOYEE:
                Log.d(LOG_TAG, "URI_EMPLOYEES");
                break;
            case URI_EMPLOYEE_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_EMPLOYEES_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = EmployeeTable.EMPLOYEE_ID + " = " + id;
                } else {
                    selection = selection + " AND " + EmployeeTable.EMPLOYEE_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(EmployeeTable.TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_EMPLOYEE:
                Log.d(LOG_TAG, "URI_EMPLOYEES");

                break;
            case URI_EMPLOYEE_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_EMPLOYEES_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = EmployeeTable.EMPLOYEE_ID + " = " + id;
                } else {
                    selection = selection + " AND " + EmployeeTable.EMPLOYEE_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(EmployeeTable.TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public class EmployersDBHelper extends SQLiteOpenHelper {

        public EmployersDBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(EmployeeTable.getCreationTableQuery());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion != newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + EmployeeTable.TABLE);
                onCreate(db);
            }
        }
    }
}
