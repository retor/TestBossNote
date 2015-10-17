package com.retor.testbossnote.view;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by retor on 15.10.2015.
 */
public interface ViewSave {
    void fillViews(HashMap<String, String> map);

    void setTitle(String title);

    void setImage(Bitmap bitmap, String url);
}
