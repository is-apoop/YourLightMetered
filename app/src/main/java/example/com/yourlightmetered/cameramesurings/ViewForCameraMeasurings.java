package example.com.yourlightmetered.cameramesurings;

import android.app.Activity;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;


import java.io.*;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import example.com.yourlightmetered.R;

// TODO show the distance to focus to ensure that you're measuring
// the light of that object
// use - public void getFocusDistances (float[] output)


public class ViewForCameraMeasurings extends Activity {
    public static int number_of_cameras = 0;

    public static int index_camera_open = 0;

    Camera mCamera;
    private CameraPreview mPreview;


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.e("Error", "Error creating media file, check storage permissions: " );
                //+e.getMessage());
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.e("Error", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.e("Error", "Error accessing file: " + e.getMessage());
            }
        }
    };

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_for_camera_mesurings);


        // get number of cameras available
        number_of_cameras = Camera.getNumberOfCameras();




        Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textView = (EditText)findViewById(R.id.editText);
                Method m = null;

                try {
                    m=  Camera.class.getDeclaredMethod("native_getParameters");

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

                m.setAccessible(true);//Abracadabra

                try {
                    textView.setText(m.invoke(mCamera).toString() + " ------- " + mCamera.getParameters().getExposureCompensation());//now its ok
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    protected void onResume() {
        super.onResume();

        //here we will execute it as a parallel execution (not serial)
        // we use executeOnExecutor instead of execute

        new AsyncTask<Void,Void, Camera>(){
            @Override
            protected Camera doInBackground(Void ... params) {

                Log.e("open","open");

                //get mCamera instance (first)
                return getCameraInstance(index_camera_open);
            }

            @Override
            protected void onPostExecute(Camera camera) {

                mCamera = camera;

                //add camera to camera preview class
                mPreview = new CameraPreview(ViewForCameraMeasurings.this, camera);

                // put camera to framelayout
                FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
                preview.addView(mPreview);

                Button buttonTakePicture = (Button)findViewById(R.id.button_start_taking_pictures);

                buttonTakePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);

                    }
                });


            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);


    }

    @Override
    protected void onPause() {
        super.onPause();

        releaseCamera();

    }


    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(int cameraIndex){
        Camera c = null;
        try {
            c = Camera.open(cameraIndex); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if mCamera is unavailable
    }

    private void releaseCamera(){
        if (mCamera != null){
            Log.e("release","release");
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_for_camera_mesurings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
