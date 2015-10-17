package com.retor.testbossnote.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.retor.testbossnote.R;
import com.retor.testbossnote.photos.PhotosSaver;
import com.retor.testbossnote.presenter.PresenterDetail;
import com.retor.testbossnote.ui.worker.UIBaseInterface;
import com.retor.testbossnote.view.ViewDetail;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by retor on 15.10.2015.
 */
public class DetailFragment extends Fragment implements ViewDetail {
    @Bind(R.id.detail_name)
    EditText name;
    @Bind(R.id.detail_surname)
    EditText surname;
    @Bind(R.id.detail_patronymic)
    EditText patronymic;
    @Bind(R.id.detail_photo)
    ImageView photo;
    @Bind(R.id.detail_age)
    EditText age;
    @Bind(R.id.detail_salary)
    EditText salary;
    @Bind(R.id.detail_delete)
    Button delete;
    @Bind(R.id.detail_edit)
    Button edit;

    private String pic;
    private UIBaseInterface uiWorker;
    private PresenterDetail presenterDetail;

    public DetailFragment() {
    }

    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        return fragment;
    }

    @OnClick(R.id.detail_edit)
    public void doEdit() {
        presenterDetail.onEditClick();
    }

    @OnClick(R.id.detail_delete)
    public void doDelete() {
        presenterDetail.onDeleteClick();
        if (pic != null)
            PhotosSaver.deletePicture(pic);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_employee, container, false);
        ButterKnife.bind(this, v);
        presenterDetail = new PresenterDetail(getActivity());
        presenterDetail.setView(this);
        presenterDetail.setArguments(getArguments());
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenterDetail.onSavedState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        presenterDetail.onRestoreState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterDetail.onDestroy();
    }

    @Override
    public void fillViews(final HashMap<String, String> map) {
        name.setText(map.get(PresenterDetail.name));
        surname.setText(map.get(PresenterDetail.surname));
        patronymic.setText(map.get(PresenterDetail.patronymic));
        salary.setText(map.get(PresenterDetail.salary));
        age.setText(map.get(PresenterDetail.age));
        if (map.get(PresenterDetail.photo) != null)
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    pic = map.get(PresenterDetail.photo);
                    Bitmap bitmap = BitmapFactory.decodeFile(pic, options);
                    photo.setImageBitmap(bitmap);
                    photo.setVisibility(View.VISIBLE);
                }
            });
    }
}
