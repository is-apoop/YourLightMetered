package example.com.yourlightmetered.lightsensormeasurings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;

import example.com.yourlightmetered.Converter;
import example.com.yourlightmetered.R;
import example.com.yourlightmetered.cameramesurings.ViewForCameraMeasurings;


public class ViewForFrontLightSensor extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mLightSensor;

    private TextView mTextView, mTextView2;

    boolean previewIsFreezed = false;

    int ISOposition = 3,
            FstopPosition = 1,
            shutterSpeedPosition = 0;


    private ArrayList<Integer> ISO =
             new ArrayList<>(Arrays.asList(100,125,160,200,250
                     ,320,400,800,1600));
    CharSequence ISOsStrings[] = new CharSequence[]{
            "100", "125","160","200","250"
            ,"320","400","800","1600"
    };

    private ArrayList<Double> Fstop = new ArrayList<Double>(Arrays.asList(
              1.6, 2., 2.8, 4.,5.6, 8.,11., 16.));

    CharSequence FStopStrings[] = new CharSequence[]{
            "1.6", "2", "2.8", "4","5.6", "8", "11", "16"
    };


    // 1 / k for shutter speed

    private ArrayList<Double> shutterSpeed = new ArrayList<>(Arrays.asList(
            (1/500.), (1/250.),(1/125.), (1/60.), (1/30.), 1., 10.
    ));

    CharSequence ShutterSpeedStrings[] = new CharSequence[]{
            "1/500", "1/250","1/125","1/60","1/30", "1", "10"
    };

    /* here should be or
        0 - f stop
        1 - sec
        2  - iso
    */
    int whatWeCalculateFor = 0;

    Button mButtonFstop, mButtonSec, mButtonISO;
    ToggleButton mButtonStopPreview;
    TextView mTextSec, mTextFstop, mTextISO, mTextEV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_for_light_sensor_measuring);
        mTextView = (TextView)findViewById(R.id.textView);
        mTextView2 = (TextView)findViewById(R.id.textView2);


        mButtonFstop = (Button)findViewById(R.id.button_f);
        mButtonISO = (Button)findViewById(R.id.button_iso);
        mButtonSec = (Button)findViewById(R.id.button_sec);
        mButtonStopPreview = (ToggleButton)findViewById(R.id.button_stop_measurings);

        mButtonISO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(ViewForFrontLightSensor.this);

                builder.setTitle(R.string.picker_iso);
                builder.setItems(ISOsStrings, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        ISOposition = which;
                    }
                });
                builder.show();
            }
        });


        mButtonSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(ViewForFrontLightSensor.this);
                builder.setTitle(R.string.picker_sec);
                builder.setItems(ShutterSpeedStrings, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        shutterSpeedPosition = which;
                    }
                });
                builder.show();
            }
        });

        mButtonFstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(ViewForFrontLightSensor.this);
                builder.setTitle(R.string.picker_f_stop);
                builder.setItems(FStopStrings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        FstopPosition = which;
                    }
                });
                builder.show();
            }
        });


        mButtonStopPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewIsFreezed = !previewIsFreezed;
            }
        });

        mTextEV = (TextView)findViewById(R.id.text_ev);
        mTextFstop = (TextView)findViewById(R.id.text_f);
        mTextISO = (TextView)findViewById(R.id.text_iso);
        mTextSec = (TextView)findViewById(R.id.text_sec);


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

    void calculateEVwithOptions(double EV){
        switch (whatWeCalculateFor){
            case 0:{
                calculateForSEC(EV);
            }break;
            case 1:{

            }break;
            case 2:{

            }break;
            default:break;
        }
    }
    void calculateForSEC(double EV){
        //TODO uncomment mTextEV.setText(Double.toString(EV));

        mTextISO.setText(ISOsStrings[ISOposition]);
        mTextFstop.setText(FStopStrings[FstopPosition]);

        double sec =  Math.pow(Fstop.get(FstopPosition), 2) / Math.pow(2, EV) ;

        //here we will simplify to be one of our values

        double temp = Integer.MAX_VALUE;
        for (int i = 0 ; i < shutterSpeed.size() ; i++){
            if (shutterSpeed.get(i) - sec < temp) {
                temp = Math.sqrt((shutterSpeed.get(i) - sec) * (shutterSpeed.get(i) - sec)) ;
                shutterSpeedPosition = i;
            }
        }

        mTextSec.setText(ShutterSpeedStrings[shutterSpeedPosition]);

    }
    void calculateForFstop(double EV){
        //TODO uncomment mTextEV.setText(Double.toString(EV));

        mTextISO.setText(ISOsStrings[ISOposition]);
        mTextSec.setText(ShutterSpeedStrings[shutterSpeedPosition]);




        double Fst =  Math.sqrt((double)shutterSpeed.get(shutterSpeedPosition) * Math.pow(2,EV));


        double temp = Integer.MAX_VALUE;
        for (int i = 0 ; i < Fstop.size() ; i++){
            if (Fstop.get(i) - Fst < temp) {
                temp = Math.sqrt((Fstop.get(i) - Fst) * (Fstop.get(i) - Fst));
                FstopPosition = i;
            }
        }


        mTextFstop.setText(FStopStrings[FstopPosition]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.e("asdasd", "light accuracy");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.e("asdasd", "light changed");

        if (!previewIsFreezed){
            // here we convert LUX to EV including ISO given
            double ev = Converter.convertLUXtoEV(event.values[0]) + Math.log(ISO.get(ISOposition)/ 100);

            calculateEVwithOptions(ev);

            mTextEV.setText("Lux: " + String.valueOf(
                    String.format("%.0f",event.values[0]) + "\nEV: " +
                            String.format("%.2f",ev)));


        }

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
