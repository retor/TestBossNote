package com.retor.testbossnote.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.retor.testbossnote.R;
import com.retor.testbossnote.presenter.PresenterDetail;
import com.retor.testbossnote.presenter.PresenterSaver;
import com.retor.testbossnote.view.ViewSave;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by retor on 15.10.2015.
 */
public class NewDialogFragment extends DialogFragment implements ViewSave {

    @Bind(R.id.new_name)
    EditText name;
    @Bind(R.id.new_surname)
    EditText surname;
    @Bind(R.id.new_patronymic)
    EditText patronymic;
    @Bind(R.id.new_photo)
    ImageView photo;
    @Bind(R.id.new_age)
    EditText age;
    @Bind(R.id.new_salary)
    EditText salary;
    @Bind(R.id.new_select_photo)
    Button select_photo;
    @Bind(R.id.new_save)
    Button save;

    private String photo_url;
    private PresenterSaver presenterSaver;

    @OnClick(R.id.new_save)
    public void doSave() {
        if (!name.getText().toString().equals("") && !surname.getText().toString().equals("")) {
            presenterSaver.doSave(createMap());
            dismiss();
        } else {
            new AlertDialog.Builder(getActivity()).setTitle("Error").setMessage("Name and Surname can't be empty").setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    setFocuseOnName();
                }
            }).setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            }).create().show();
        }
    }

    private HashMap<String, String> createMap() {
        HashMap<String, String> out = new HashMap<>();
        out.put(PresenterSaver.name, name.getText().toString());
        out.put(PresenterSaver.surname, surname.getText().toString());
        out.put(PresenterSaver.patronymic, patronymic.getText().toString());
        out.put(PresenterSaver.photo, photo_url);
        out.put(PresenterSaver.age, age.getText().toString());
        out.put(PresenterSaver.salary, salary.getText().toString());
        return out;
    }

    @OnClick(R.id.new_select_photo)
    public void selectPhoto() {
        NewDialogFragment.this.startActivityForResult(presenterSaver.selectPhoto(), 1);
    }

    public NewDialogFragment() {
    }

    public static NewDialogFragment newInstance() {
        NewDialogFragment fragment = new NewDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        this.presenterSaver = new PresenterSaver(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_employee, container, false);
        Log.d("Layout", "onCreateView()" + v.getTag());
        ButterKnife.bind(this, v);
        presenterSaver.setView(this);
        presenterSaver.setArguments(getArguments());
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
    }

    private void setFocuseOnName() {
        name.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenterSaver.onSavedState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        presenterSaver.onRestoreState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterSaver.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenterSaver.onResult(requestCode, resultCode, data, getActivity());
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void fillViews(final HashMap<String, String> map) {
        name.setText(map.get(PresenterDetail.name));
        surname.setText(map.get(PresenterDetail.surname));
        patronymic.setText(map.get(PresenterDetail.patronymic));
        salary.setText(map.get(PresenterDetail.salary));
        age.setText(map.get(PresenterDetail.age));
        if (map.get(PresenterDetail.photo) != null)
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    photo.setImageURI(Uri.parse(map.get(PresenterDetail.photo)));
                    photo_url = map.get(PresenterDetail.photo);
                    photo.setVisibility(View.VISIBLE);
                }
            });
    }

    @Override
    public void setTitle(String title) {
        getDialog().setTitle(title);
    }

    @Override
    public void setImage(Bitmap bitmap, String url) {
        photo_url = url;
        photo.setImageBitmap(bitmap);
        photo.setVisibility(View.VISIBLE);
    }
}
