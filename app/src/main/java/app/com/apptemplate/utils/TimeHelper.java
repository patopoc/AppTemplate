package app.com.apptemplate.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by steve on 06/05/2015.
 */
public class TimeHelper {
    //timestamp in milliseconds
    public static String getDate(long timestamp, String format){
        try{
            DateFormat dateFormat= new SimpleDateFormat(format);
            Date date = new Date(timestamp);
            return dateFormat.format(date);
        }catch (Exception e){
            return "";
        }
    }
}
