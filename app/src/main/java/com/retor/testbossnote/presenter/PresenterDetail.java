package com.retor.testbossnote.presenter;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.retor.testbossnote.beans.Employee;
import com.retor.testbossnote.db.EmployeeTable;
import com.retor.testbossnote.db.SimpleProvider;
import com.retor.testbossnote.ui.worker.UIBaseInterface;
import com.retor.testbossnote.ui.worker.UIWorker;
import com.retor.testbossnote.view.ViewDetail;

import java.util.HashMap;

/**
 * Created by retor on 15.10.2015.
 */
public class PresenterDetail {
    public static final String name = "name";
    public static final String surname = "surname";
    public static final String patronymic = "patronymic";
    public static final String age = "age";
    public static final String photo = "photo";
    public static final String salary = "salary";

    private static final String SAVE_TAG = "employee";

    private Context context;
    private ViewDetail view;
    private Employee employee;
    private UIBaseInterface uiWorker;

    public PresenterDetail(FragmentActivity activity) {
        this.context = activity;
        this.uiWorker = UIWorker.getInstance(activity);
    }

    public void setView(ViewDetail view) {
        this.view = view;
    }

    public void setArguments(Bundle arguments) {
        if (arguments != null && arguments.containsKey(EmployeeTable.TABLE))
            fillViews(arguments.<Employee>getParcelable(EmployeeTable.TABLE));
    }

    private void fillViews(Employee employee) {
        view.fillViews(getMapFromEmployee(employee));
        this.employee = employee;
    }

    public void onEditClick() {
        if (!uiWorker.isTablet())
            uiWorker.close();
        uiWorker.openNewFragment(employee);
    }

    public void onDeleteClick() {
        Uri uri = ContentUris.withAppendedId(SimpleProvider.EMPLOYEE_CONTENT_URI, employee.getId());
        context.getContentResolver().delete(uri, null, null);
        uiWorker.close();
    }

    @NonNull
    private HashMap<String, String> getMapFromEmployee(Employee employee) {
        HashMap<String, String> out = new HashMap<>();
        out.put(name, employee.getName());
        out.put(surname, employee.getSurname());
        out.put(patronymic, employee.getPatronymic());
        out.put(photo, employee.getPicture_url());
        out.put(age, String.valueOf(employee.getAge()));
        out.put(salary, String.valueOf(employee.getSalary()));
        return out;
    }

    public void onSavedState(Bundle outState) {
        if (employee != null)
            outState.putParcelable(SAVE_TAG, employee);
    }

    public void onRestoreState(Bundle savedState) {
        if (savedState != null && savedState.containsKey(SAVE_TAG))
            this.employee = savedState.getParcelable(SAVE_TAG);
    }

    public void onDestroy() {

    }
}
