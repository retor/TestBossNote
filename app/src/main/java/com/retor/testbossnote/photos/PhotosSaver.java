package com.retor.testbossnote.photos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by retor on 16.10.2015.
 */
public class PhotosSaver {

    public static String saveImage(Intent data, Activity activity) {
        String image_name = generateName(activity.getPackageName());
        File file = new File(image_name);
        if (file.exists()) {
            image_name = generateName(activity.getPackageName());
            file = new File(image_name);
        }

        Bitmap bitmap = data.getExtras().getParcelable("data");

        OutputStream fOutputStream = null;
        try {
            file.createNewFile();
            fOutputStream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);
            fOutputStream.flush();
            fOutputStream.close();
            MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), file.getName());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("Photo uri", "Save Failed");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Photo uri", "Save Failed");
        }
        Log.d("Photo uri", file.getAbsoluteFile().getAbsolutePath());
        return file.getAbsolutePath();
    }

    private static String generateName(String packageName) {
        Random random = new Random();
        File f = new File(Environment.getExternalStorageDirectory() + "/" + packageName + "/photos");
        if (!f.exists())
            f.mkdirs();
        return f.getPath() + "/" + "photo" + random.nextInt(2456346) + ".jpg";
    }

    public static void deletePicture(String path){
        File file = new File(path);
        if (file.exists())
            file.delete();
    }
}
