package com.unixlab.myproject;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.net.Uri;
import android.os.Environment;
import android.graphics.BitmapFactory;
import android.content.Intent;

import android.util.Log;

public class PhotoActivity extends Activity
{
    private static final String TAG = "MyActivity";

    private ImageView mImageView;
    private Bitmap mImageBitmap;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mImageView = (ImageView) findViewById(R.id.imageView1);
        Log.d(TAG, "created in onCreate");
        displayTmp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void takePicture(View view) {
        dispatchTakePictureIntent(1);
    }

    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File("/sdcard/tmp.jpg");
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                   Uri.fromFile(f));
        Log.d(TAG, "About to start camera intent");
        startActivityForResult(takePictureIntent, actionCode);
    }

    private void displayTmp() {
        Log.d(TAG, "Trying to display tmp");
        File f = new File("/sdcard/tmp.jpg");
        Log.d(TAG, "read file");
        if(f.exists()){
            Log.d(TAG, "about to decode file: " + f.getAbsolutePath());
            // Get the dimensions of the View
            int targetW = mImageView.getWidth();
            int targetH = mImageView.getHeight();

            Log.d(TAG, "got dimensions");
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            Log.d(TAG, "about to decode for first time");
            BitmapFactory.decodeFile(f.getAbsolutePath(), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            Log.d(TAG, "decoded " + photoW + "/" + targetW);
            // Determine how much to scale down the image
            int scaleFactor = 10;//Math.min(photoW/targetW, photoH/targetH);

            Log.d(TAG, "about to get bounds");
            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Log.d(TAG, "about to decode for second time");
            Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bmOptions);
            Log.d(TAG, "test: " + myBitmap);
            mImageView.setImageBitmap(myBitmap);
        }
    }

    private void handleSmallCameraPhoto(Intent intent) {
        Log.d(TAG, "Trying to handle small camera photo");
        displayTmp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "in onActivityResult");
        handleSmallCameraPhoto(data);
    }
}
