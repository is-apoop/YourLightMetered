package example.com.yourlightmetered;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import example.com.yourlightmetered.cameramesurings.ViewForCameraMeasurings;
import example.com.yourlightmetered.lightsensormeasurings.ViewForFrontLightSensor;


public class MainActivity extends Activity {
    public static Boolean camera_works = false;



    Button button_start_camera_measurings;
    Button button_start_light_sensor_measuring;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // here we will check our device on camera and sensors availability



        /*
        camera_works = checkCameraHardware(MainActivity.this);




        //TODO also we will TEST these sensors on workability







        // here we add listeners
        button_start_camera_measurings = (Button)findViewById(R.id.button_start_camera_measurings);
        button_start_camera_measurings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera_works) {
                    Intent intent = new Intent(MainActivity.this, ViewForCameraMeasurings.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, R.string.toast_camera_disabled, Toast.LENGTH_LONG).show();
                }

            }
        });


        button_start_light_sensor_measuring = (Button)findViewById(R.id.button_start_light_meter);
        button_start_light_sensor_measuring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO check for availability sensor
                Intent intent = new Intent(MainActivity.this, ViewForFrontLightSensor.class);
                startActivity(intent);
                
            }
        });
*/

        RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.main_layout);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO check for availability sensor
                Intent intent = new Intent(MainActivity.this, ViewForFrontLightSensor.class);
                startActivity(intent);
            }
        });

    }



    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        // this device has a camera or
        // no camera on this device
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }













    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
