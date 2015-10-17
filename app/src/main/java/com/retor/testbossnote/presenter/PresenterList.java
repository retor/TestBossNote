package com.retor.testbossnote.presenter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.retor.testbossnote.R;
import com.retor.testbossnote.adapter.BaseAdapter;
import com.retor.testbossnote.beans.Employee;
import com.retor.testbossnote.interactor.LoadingCallback;
import com.retor.testbossnote.interactor.ProviderInteractor;
import com.retor.testbossnote.ui.worker.UIBaseInterface;
import com.retor.testbossnote.ui.worker.UIWorker;
import com.retor.testbossnote.view.ViewList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by retor on 15.10.2015.
 */
public class PresenterList implements LoadingCallback, UpdateListener {
    private ViewList viewList;
    private BaseAdapter baseAdapter;
    private UIBaseInterface uiWorker;
    private int lastPosition = 0;
    private ProviderInteractor interactor;
    private FragmentActivity activity;

    private static String LIST_TAG = "array";
    private static String LASTPOSITION_TAG = "position";
    private static final int LOADER_ID = 89898;

    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    public PresenterList(FragmentActivity activity) {
        this.uiWorker = UIWorker.getInstance(activity);
        uiWorker.setListener(this);
        this.baseAdapter = new BaseAdapter(new ArrayList<Employee>());
        this.interactor = new ProviderInteractor(activity);
        interactor.setCallback(this);
    }

    public void loadData() {
        activity.getSupportLoaderManager().initLoader(LOADER_ID, null, interactor);
    }

    @Override
    public void loaded(List<Employee> listEmployees) {
        baseAdapter.setItems(listEmployees);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                checkIsTablet();
            }
        });
        if (floatingActionButton != null && !floatingActionButton.isShown())
            floatingActionButton.show();
    }

    public void onCreate(FragmentActivity activity) {
        this.activity = activity;
    }

    @OnClick(R.id.fab)
    public void openNewEmpDialog() {
        uiWorker.openNewFragment(null);
    }

    public void onItemClick(int position) {
        if (position > (baseAdapter.getItemCount() - 1))
            lastPosition = position - 1;
        else
            lastPosition = position;

        if (!uiWorker.isTablet() && !baseAdapter.getList().isEmpty()) {
            floatingActionButton.hide();
            uiWorker.openEditFragment(baseAdapter.getList().get(lastPosition));
        } else if (uiWorker.isTablet() && !baseAdapter.getList().isEmpty()) {
            uiWorker.openEditFragment(baseAdapter.getList().get(lastPosition));
        }
    }

    public void onScroll(int dy) {
        if (dy > 0 && floatingActionButton.isShown()) {
            floatingActionButton.hide();
        } else {
            floatingActionButton.show();
        }
    }

    public BaseAdapter getBaseAdapter() {
        if (baseAdapter == null)
            this.baseAdapter = new BaseAdapter(new ArrayList<Employee>());
        return this.baseAdapter;
    }

    public void setViewList(ViewList viewList) {
        this.viewList = viewList;
    }

    private void checkIsTablet() {
        if (uiWorker.isTablet()) {
            if (!baseAdapter.getList().isEmpty())
                onItemClick(lastPosition);
        }
    }

    @Override
    public void empty() {
        new AlertDialog.Builder(activity).setTitle("Empty List").setMessage("Add Employee").setNeutralButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openNewEmpDialog();
                dialog.dismiss();
            }
        }).create().show();
    }

    @Override
    public void onError(String errorMessage) {
        viewList.showError(errorMessage);
    }

    public void onSaveState(Bundle outBundle) {
        saveAllStates(outBundle);
    }

    private void saveAllStates(Bundle outBundle) {
        saveList(outBundle);
        savePosition(outBundle);
    }

    private void savePosition(Bundle outBundle) {
        outBundle.putInt(LASTPOSITION_TAG, lastPosition);
    }

    private void saveList(Bundle outBundle) {
        if (baseAdapter != null)
            outBundle.putParcelableArrayList(LIST_TAG, (ArrayList<? extends Parcelable>) baseAdapter.getList());
    }

    public void onRestoreState(Bundle savedState) {
        restoreFromState(savedState);
    }

    private void restoreFromState(Bundle savedState) {
        if (savedState != null) {
            if (savedState.containsKey(LIST_TAG))
                restoreList(savedState);
            if ((savedState.containsKey(LASTPOSITION_TAG)))
                restorePosition(savedState);
        }
        if (!baseAdapter.getList().isEmpty())
            viewList.setPosition(lastPosition);
    }

    private void restoreList(Bundle savedState) {
        this.baseAdapter.setItems(savedState.<Employee>getParcelableArrayList(LIST_TAG));
    }

    private void restorePosition(Bundle savedState) {
        lastPosition = savedState.getInt(LASTPOSITION_TAG, 0);
    }

    public void onDestroy(FragmentActivity activity) {
        activity.getSupportLoaderManager().destroyLoader(LOADER_ID);
        ButterKnife.unbind(this);
    }

    public void onResume(FragmentActivity activity) {
        this.activity = activity;
        activity.getSupportLoaderManager().restartLoader(LOADER_ID, null, interactor);
    }

    @Override
    public void update() {
        if (floatingActionButton != null && !floatingActionButton.isShown())
            floatingActionButton.show();
        activity.getSupportLoaderManager().restartLoader(LOADER_ID, null, interactor);
        loadData();
    }
}
