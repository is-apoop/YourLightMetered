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
import android.widget.TextView;

import example.com.yourlightmetered.Converter;
import example.com.yourlightmetered.R;


public class ViewForFrontLightSensor extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mLightSensor;

    private TextView mTextView, mTextView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        mTextView = (TextView)findViewById(R.id.textView);
        mTextView2 = (TextView)findViewById(R.id.textView2);


        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.e("asdasd", "light accuracy");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.e("asdasd", "light changed");
        mTextView2.setText("Lux: " + String.valueOf(event.values[0]));


        double ev = Converter.convertLUXtoEV(event.values[0]);
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
