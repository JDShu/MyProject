package com.unixlab.myproject;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.BaseAdapter;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private File storageDir;
    private String[] files;
    private static String TAG = "ImageAdapter";

    public ImageAdapter(Context c, File dir) {
        mContext = c;
        storageDir = dir;
        files = storageDir.list();
    }

    public int getCount() {
        return files.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                                                ViewGroup.LayoutParams.FILL_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 10;
        bmOptions.inPurgeable = true;

        String f = storageDir.getAbsolutePath() + "/" + files[position];
        Log.d(TAG, "file: " + f);
        Bitmap myBitmap = BitmapFactory.decodeFile(f, bmOptions);
        Log.d(TAG, "test: " + myBitmap);

        // Work around android cameras that do not rotate images automatically.
        try {
            Bitmap rotated = applyOrientation(myBitmap, resolveBitmapOrientation(f));
            imageView.setImageBitmap(rotated);
        } catch (IOException e) {
            imageView.setImageBitmap(myBitmap);
        }

        return imageView;
    }

    private int resolveBitmapOrientation(String path) throws IOException {
        ExifInterface exif = null;
        exif = new ExifInterface(path);

        return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
    }

    private Bitmap applyOrientation(Bitmap bitmap, int orientation) {
        int rotate = 0;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
            default:
                return bitmap;
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }
}
