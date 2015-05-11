package example.com.yourlightmetered;

import android.app.Activity;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class ViewForCameraMeasurings extends Activity {
    public static int number_of_cameras = 0;

    public static int index_camera_open = 0;

    Camera camera;

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

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void ... params) {
                //get camera instance (first)
                camera = getCameraInstance(index_camera_open);
                Log.e("open","open");
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(camera!=null) {
            Log.e("release","release");
            camera.release();

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
        return c; // returns null if camera is unavailable
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
