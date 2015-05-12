package example.com.yourlightmetered.cameramesurings;

import android.app.Activity;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import example.com.yourlightmetered.R;


public class ViewForCameraMeasurings extends Activity {
    public static int number_of_cameras = 0;

    public static int index_camera_open = 0;

    Camera mCamera;
    private CameraPreview mPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_for_camera_mesurings);


        // get number of cameras available
        number_of_cameras = Camera.getNumberOfCameras();




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

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);


    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mCamera !=null) {
            Log.e("release","release");
            mCamera.release();

        }

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
