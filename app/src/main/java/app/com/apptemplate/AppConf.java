package app.com.apptemplate;

/**
 * Created by steve on 27/04/2015.
 */
public class AppConf {
    public enum NavigationType{
        NONE,DRAWER,TAB,VIEWPAGER,GRID
    }


    public static NavigationType navigation=NavigationType.NONE; //drawer, tab, viewpager, grid
    public static boolean acceptTwoPane=false;

    /*Network Comunication Details*/
    public static String host="192.168.1.100";
    //public static String host="192.168.1.106";
    public static String protocol="http";

    /*Notification Alarm */
    public static long notificationHours=12;
    public static long notificationDays=7;


}
