package example.com.yourlightmetered;

/**
 * Created by Anton Yeshchenko on 25/05/2015.
 */
public class Converter {

    public static double convertLUXtoEV( float lux){
        return Math.log10(0.4 * lux) / Math.log10(2.);
    }
};
