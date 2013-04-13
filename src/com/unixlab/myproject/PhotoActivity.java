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
    private static final String PICTURES_DIR = "Box/";
    private static final String UNORGANIZED_DIR = "Unorganized/";

    private ImageView mImageView;
    private Bitmap mImageBitmap;
    private File storageDir;

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
        storageDir = new File (
                               Environment.getExternalStorageDirectory(),
                               PICTURES_DIR
                               + UNORGANIZED_DIR
                               );
        storageDir.mkdirs();
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
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = createImageFile();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                       Uri.fromFile(f));
            Log.d(TAG, "About to start camera intent");
            startActivityForResult(takePictureIntent, actionCode);
        } catch (IOException e) {
            Log.d(TAG, "Error while creating image file: ", e);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        // TODO: let user name file
        String timeStamp =
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        Log.d(TAG, "storageDir: " + storageDir.getAbsolutePath() + ", exists: " + storageDir.exists());
        File image = File.createTempFile(
                                         imageFileName,
                                         JPEG_FILE_SUFFIX,
                                         storageDir
                                         );
        return image;
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
        //displayTmp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "in onActivityResult");
        handleSmallCameraPhoto(data);
    }
}
