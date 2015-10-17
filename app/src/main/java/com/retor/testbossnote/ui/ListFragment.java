package com.retor.testbossnote.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.retor.testbossnote.R;
import com.retor.testbossnote.adapter.TouchAdapterRecyclerView;
import com.retor.testbossnote.presenter.PresenterList;
import com.retor.testbossnote.ui.worker.UIBaseInterface;
import com.retor.testbossnote.ui.worker.UIWorker;
import com.retor.testbossnote.view.ViewList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by retor on 15.10.2015.
 */
public class ListFragment extends Fragment implements ViewList {
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    private PresenterList listPresenter;
    private UIBaseInterface uiWorker;

    public ListFragment() {
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiWorker = UIWorker.getInstance(getActivity());
        listPresenter = new PresenterList(getActivity());
        listPresenter.setViewList(this);
        listPresenter.onCreate(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);
        ButterKnife.bind(listPresenter, getActivity());
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemAnimator im = new DefaultItemAnimator();
        im.setAddDuration(1000);
        recyclerView.setItemAnimator(im);
        recyclerView.setAdapter(listPresenter.getBaseAdapter());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                listPresenter.onScroll(dy);
            }
        });
        recyclerView.addOnItemTouchListener(new TouchAdapterRecyclerView(recyclerView.getContext(), new TouchAdapterRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                listPresenter.onItemClick(position);
            }
        }));
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        listPresenter.onRestoreState(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        listPresenter.onResume(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listPresenter.onSaveState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listPresenter.onDestroy(getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        uiWorker.delete();
    }

    @Override
    public void showError(String msg) {
        new AlertDialog.Builder(this.getActivity()).setMessage(msg).create().show();
    }

    @Override
    public void showNullList() {

    }

    @Override
    public void setPosition(int lastPosition) {
        recyclerView.scrollToPosition(lastPosition);
    }
}
