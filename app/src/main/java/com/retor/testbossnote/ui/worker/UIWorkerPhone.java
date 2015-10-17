package com.retor.testbossnote.ui.worker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.retor.testbossnote.R;
import com.retor.testbossnote.beans.Employee;
import com.retor.testbossnote.db.EmployeeTable;
import com.retor.testbossnote.presenter.UpdateListener;
import com.retor.testbossnote.ui.DetailFragment;
import com.retor.testbossnote.ui.ListFragment;
import com.retor.testbossnote.ui.NewFragment;

import static com.retor.testbossnote.db.EmployeeTable.TABLE;

/**
 * Created by retor on 16.10.2015.
 */
class UIWorkerPhone implements UIBaseInterface {

    private static final String LIST_TAG = "listFragment";
    private static final String DETAIL_TAG = "detailFragment";
    private static final String NEW_TAG = "newFragment";

    private FragmentManager manager;
    private UpdateListener listener;

    protected UIWorkerPhone(FragmentActivity activity) {
        this.manager = activity.getSupportFragmentManager();
    }

    @Override
    public void fillStartScreen() {
        if (manager.findFragmentByTag(LIST_TAG) == null)
            manager.beginTransaction()
                    .add(R.id.frame, ListFragment.newInstance(), LIST_TAG)
                    .commit();
    }

    @Override
    public void removeFragment(Fragment fragment) {
        manager.beginTransaction().remove(fragment).commit();
    }

    @Override
    public void openEditFragment(Employee employee) {
        Fragment fragment = DetailFragment.newInstance();
        Bundle arg = new Bundle();
        arg.putParcelable(TABLE, employee);
        fragment.setArguments(arg);
        manager.beginTransaction()
                .replace(R.id.frame, fragment, DETAIL_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openNewFragment(Employee employee) {
        Fragment fragment = NewFragment.newInstance();
        if (employee != null) {
            Bundle args = new Bundle();
            args.putParcelable(EmployeeTable.TABLE, employee);
            fragment.setArguments(args);
        }
        manager.beginTransaction()
                .replace(R.id.frame, fragment, NEW_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setListener(UpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isTablet() {
        return false;
    }

    @Override
    public void close() {
        manager.popBackStack();
        if (listener != null)
            listener.update();
    }

    @Override
    public void delete() {
        listener = null;
        manager = null;
    }
}
