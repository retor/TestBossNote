package com.retor.testbossnote.presenter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.retor.testbossnote.beans.Employee;
import com.retor.testbossnote.db.EmployeeTable;
import com.retor.testbossnote.db.SimpleProvider;
import com.retor.testbossnote.photos.PhotosSaver;
import com.retor.testbossnote.ui.worker.UIBaseInterface;
import com.retor.testbossnote.ui.worker.UIWorker;
import com.retor.testbossnote.view.ViewSave;

import java.util.HashMap;

/**
 * Created by retor on 15.10.2015.
 */
public class PresenterSaver {
    public static final String name = "name";
    public static final String surname = "surname";
    public static final String patronymic = "patronymic";
    public static final String age = "age";
    public static final String photo = "photo";
    public static final String salary = "salary";

    private static final String SAVE_TAG = "employee";

    private ContentResolver resolver;
    private Employee employee;
    private ViewSave view;
    private UIBaseInterface uiWorker;

    public PresenterSaver(FragmentActivity activity) {
        this.uiWorker = UIWorker.getInstance(activity);
        this.resolver = activity.getContentResolver();
    }

    public void setView(ViewSave view) {
        this.view = view;
    }

    public void setArguments(Bundle arguments) {
        if (arguments != null)
            view.setTitle("Edit Employee: " + arguments.<Employee>getParcelable(EmployeeTable.TABLE).getName());
        else
            view.setTitle("Add new Employee");

        if (arguments != null && arguments.get(EmployeeTable.TABLE) != null)
            fillViews(arguments.<Employee>getParcelable(EmployeeTable.TABLE));
    }

    private void fillViews(Employee emp) {
        view.fillViews(getMapFromEmployee(emp));
        this.employee = emp;
    }

    @NonNull
    private HashMap<String, String> getMapFromEmployee(Employee emp) {
        HashMap<String, String> out = new HashMap<>();
        out.put(name, emp.getName());
        out.put(surname, emp.getSurname());
        out.put(patronymic, emp.getPatronymic());
        out.put(photo, emp.getPicture_url());
        out.put(age, String.valueOf(emp.getAge()));
        out.put(salary, String.valueOf(emp.getSalary()));
        return out;
    }

    public void doSave(HashMap<String, String> map) {
        if (employee == null)
            saveEmployee(createNewEmployee(map));
        else
            updateEmployee(createEmployee(map));
    }

    private Employee createEmployee(HashMap<String, String> map) {
        Employee emp = createNewEmployee(map);
        emp.setId(employee.getId());
        return emp;
    }

    private Employee createNewEmployee(HashMap<String, String> map) {
        Employee emp = new Employee();
        emp.setName(map.get(name));
        emp.setSurname(map.get(surname));
        emp.setPatronymic(map.get(patronymic));
        emp.setPicture_url(map.get(photo));
        if (map.get(age) != null)
            emp.setAge(Integer.parseInt(map.get(age)));
        if (map.get(salary) != null)
            emp.setSalary(Integer.parseInt(map.get(salary)));
        return emp;
    }

    private void saveEmployee(Employee item) {
        resolver.insert(SimpleProvider.EMPLOYEE_CONTENT_URI, getContentValues(item));
        uiWorker.close();
    }

    private void updateEmployee(Employee item) {
        Uri uri = ContentUris.withAppendedId(SimpleProvider.EMPLOYEE_CONTENT_URI, item.getId());
        resolver.update(uri, getContentValues(item), null, null);
        uiWorker.close();
    }

    public void onSavedState(Bundle outState) {
        if (employee != null)
            outState.putParcelable(SAVE_TAG, employee);
    }

    public void onRestoreState(Bundle savedState) {
        if (savedState != null && savedState.containsKey(SAVE_TAG))
            this.employee = savedState.getParcelable(SAVE_TAG);
    }

    public void onResult(int requestCode, int resultCode, Intent data, Activity activity) {
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    setImage(data, activity);
                }
                break;
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    setImage(data, activity);
                }
                break;
        }
    }

    public Intent selectPhoto() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.putExtra("crop", "true");
        pickPhoto.putExtra("aspectX", 0);
        pickPhoto.putExtra("aspectY", 0);
        pickPhoto.putExtra("outputX", 200);
        pickPhoto.putExtra("outputY", 150);
        pickPhoto.putExtra("return-data", true);
        return pickPhoto;
    }

    private void setImage(Intent data, Activity activity) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        String url = PhotosSaver.saveImage(data, activity);
        view.setImage(BitmapFactory.decodeFile(url, options), url);
    }

    public void onDestroy() {

    }

    @NonNull
    private ContentValues getContentValues(Employee item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EmployeeTable.EMPLOYEE_NAME, item.getName());
        contentValues.put(EmployeeTable.EMPLOYEE_SURNAME, item.getSurname());
        contentValues.put(EmployeeTable.EMPLOYEE_PATRONYMIC, item.getPatronymic());
        contentValues.put(EmployeeTable.EMPLOYEE_PICTURE, item.getPicture_url());
        contentValues.put(EmployeeTable.EMPLOYEE_AGE, item.getAge());
        contentValues.put(EmployeeTable.EMPLOYEE_SALARY, item.getSalary());
        return contentValues;
    }

    public void onCreate(FragmentActivity activity) {
        uiWorker = UIWorker.getInstance(activity);
    }
}
