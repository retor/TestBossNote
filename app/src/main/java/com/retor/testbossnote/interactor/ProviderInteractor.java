package com.retor.testbossnote.interactor;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.retor.testbossnote.beans.Employee;
import com.retor.testbossnote.db.EmployeeTable;
import com.retor.testbossnote.db.SimpleProvider;

import java.util.ArrayList;
import java.util.List;

import static com.retor.testbossnote.db.EmployeeTable.EMPLOYEE_AGE;
import static com.retor.testbossnote.db.EmployeeTable.EMPLOYEE_ID;
import static com.retor.testbossnote.db.EmployeeTable.EMPLOYEE_NAME;
import static com.retor.testbossnote.db.EmployeeTable.EMPLOYEE_PATRONYMIC;
import static com.retor.testbossnote.db.EmployeeTable.EMPLOYEE_PICTURE;
import static com.retor.testbossnote.db.EmployeeTable.EMPLOYEE_SALARY;
import static com.retor.testbossnote.db.EmployeeTable.EMPLOYEE_SURNAME;

/**
 * Created by retor on 15.10.2015.
 */
public class ProviderInteractor implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;
    private LoadingCallback callback;

    public static String[] projection = new String[]{EmployeeTable.EMPLOYEE_ID,
            EmployeeTable.EMPLOYEE_NAME,
            EmployeeTable.EMPLOYEE_SURNAME,
            EmployeeTable.EMPLOYEE_PATRONYMIC,
            EmployeeTable.EMPLOYEE_PICTURE,
            EmployeeTable.EMPLOYEE_SALARY,
            EmployeeTable.EMPLOYEE_AGE};

    public ProviderInteractor(Context context) {
        this.context = context;
    }

    public LoadingCallback getCallback() {
        return callback;
    }

    public void setCallback(LoadingCallback callback) {
        this.callback = callback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(context, SimpleProvider.EMPLOYEE_CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Employee> out = new ArrayList<>();
        try {
            if (checkCursor(data)) {
                while (data.moveToNext()) {
                    Employee emp = new Employee();
                    emp.setId(data.getInt(data.getColumnIndex(EMPLOYEE_ID)));
                    emp.setName(data.getString(data.getColumnIndex(EMPLOYEE_NAME)));
                    emp.setSurname(data.getString(data.getColumnIndex(EMPLOYEE_SURNAME)));
                    emp.setPatronymic(data.getString(data.getColumnIndex(EMPLOYEE_PATRONYMIC)));
                    emp.setPicture_url(data.getString(data.getColumnIndex(EMPLOYEE_PICTURE)));
                    emp.setAge(data.getInt(data.getColumnIndex(EMPLOYEE_AGE)));
                    emp.setSalary(data.getInt(data.getColumnIndex(EMPLOYEE_SALARY)));
                    out.add(emp);
                }
            }
        } finally {
            if (!data.isClosed()) {
                data.close();
            }
        }
        callback.loaded(out);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private boolean checkCursor(Cursor data) {
        if (data == null) {
            callback.onError("Null cursor");
            return false;
        }
        if (data.getCount() == 0) {
            callback.empty();
            return false;
        }
        return true;
    }
}
