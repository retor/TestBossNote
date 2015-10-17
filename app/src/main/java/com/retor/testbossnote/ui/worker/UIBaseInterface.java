package com.retor.testbossnote.ui.worker;

import android.support.v4.app.Fragment;

import com.retor.testbossnote.beans.Employee;
import com.retor.testbossnote.presenter.UpdateListener;

/**
 * Created by retor on 16.10.2015.
 */
public interface UIBaseInterface {

    void fillStartScreen();

    void removeFragment(Fragment fragment);

    void openEditFragment(Employee employee);

    void openNewFragment(Employee employee);

    void setListener(UpdateListener listener);

    boolean isTablet();

    void close();

    void delete();
}
