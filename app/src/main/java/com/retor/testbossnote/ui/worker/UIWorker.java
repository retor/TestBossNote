package com.retor.testbossnote.ui.worker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.retor.testbossnote.R;
import com.retor.testbossnote.beans.Employee;
import com.retor.testbossnote.presenter.UpdateListener;

/**
 * Created by retor on 16.10.2015.
 */
public class UIWorker implements UIBaseInterface {
    private static volatile UIWorker instance;

    public static UIWorker getInstance(FragmentActivity activity) {
        UIWorker localInstance = instance;
        if (localInstance == null) {
            synchronized (UIWorker.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UIWorker(activity);
                }
            }
        }
        return instance;
    }

    private UIBaseInterface uiworker;

    protected UIWorker(FragmentActivity activity) {
        if (activity.getResources().getBoolean(R.bool.isTablet)) {
            uiworker = new UIWorkerTab(activity);
        } else {
            uiworker = new UIWorkerPhone(activity);
        }
    }

    public boolean isTablet() {
        return uiworker.isTablet();
    }

    @Override
    public void fillStartScreen() {
        uiworker.fillStartScreen();
    }

    @Override
    public void removeFragment(Fragment fragment) {
        uiworker.removeFragment(fragment);
    }

    @Override
    public void openEditFragment(Employee employee) {
        uiworker.openEditFragment(employee);
    }

    @Override
    public void openNewFragment(Employee employee) {
        uiworker.openNewFragment(employee);
    }

    @Override
    public void setListener(UpdateListener listener) {
        uiworker.setListener(listener);
    }

    @Override
    public void close() {
        uiworker.close();
    }

    public void delete() {
        if (uiworker != null)
            uiworker.delete();
        instance = null;
        uiworker = null;
    }
}
