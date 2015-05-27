package example.com.yourlightmetered.lightsensormeasurings;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import example.com.yourlightmetered.Converter;
import example.com.yourlightmetered.R;


public class ViewForFrontLightSensor extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mLightSensor;

    private TextView mTextView, mTextView2;

    private ArrayList<Integer> ISO =
             new ArrayList<>(Arrays.asList(50,64,80,100,125,160,200,250
                     ,320,400,500,640,800,1000,1250,1600));
    private ArrayList<Double> Fstop = new ArrayList<Double>(Arrays.asList(
            1., 1.4, 1.6, 1.7, 1.8, 2., 2.2, 2.4, 2.5, 2.8, 3.2, 3.3, 3.5, 4.,
            4.5, 4.8, 5.0, 5.5, 5.6, 6.3, 6.7, 7.1, 8., 9.,
            9.5, 10., 11., 13., 14., 16., 18., 19., 20.));

    // 1 / k for shutter speed

    private ArrayList<Double> shutterSpeed = new ArrayList<>(Arrays.asList(
        60.,30.,15.,13.,12.,10.,8.,6.,5.,4.,3.,2.5,2.,1.6,1.5,1.3,1.
    ));


    /* here should be or
        0 - f stop
        1 - sec
        2  - iso
    */
    int whatWeCalculateFor = 0;

    Button mButtonFstop, mButtonSec, mButtonISO, mButtonStopPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_for_light_sensor_measuring);
        mTextView = (TextView)findViewById(R.id.textView);
        mTextView2 = (TextView)findViewById(R.id.textView2);


        mButtonFstop = (Button)findViewById(R.id.button_f);
        mButtonISO = (Button)findViewById(R.id.button_iso);
        mButtonSec = (Button)findViewById(R.id.button_sec);
        mButtonStopPreview = (Button)findViewById(R.id.button_stop_measurings);


                mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        mTextView.setText(mSensorManager.getSensorList(Sensor.TYPE_LIGHT).toString());
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    void calculateForFstop(double EV){

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.e("asdasd", "light accuracy");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.e("asdasd", "light changed");



        double ev = Converter.convertLUXtoEV(event.values[0]);

        mTextView2.setText("Lux: " + String.valueOf(event.values[0] + "\nEV: " + ev));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_view, menu);
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
