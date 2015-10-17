package com.retor.testbossnote.interactor;

import com.retor.testbossnote.beans.Employee;

import java.util.List;

/**
 * Created by retor on 15.10.2015.
 */
public interface LoadingCallback {
    void loaded(List<Employee> listEmployees);
    void empty();
    void onError(String errorMessage);
}
