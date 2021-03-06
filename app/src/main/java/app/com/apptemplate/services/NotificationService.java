package app.com.apptemplate.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import app.com.apptemplate.AppConf;
import app.com.apptemplate.AppMain;
import app.com.apptemplate.R;
import app.com.apptemplate.utils.DataBaseHelper;
import app.com.apptemplate.utils.TimeHelper;

/**
 * Created by steve on 06/05/2015.
 */
public class NotificationService extends IntentService {
    final String TAG="NotificationService";
    public static boolean enableDailyNotification=true;
    public static boolean enableWeeklyNotification=true;

    public NotificationService(){
        super("NotificationService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "launching notification");

        DataBaseHelper dbh= new DataBaseHelper(this);
        try {
            dbh.createDataBase();
            SQLiteDatabase database = dbh.getReadableDatabase();
            Cursor cursor = database.rawQuery("select date from smiles order by id desc limit 1", null);
            cursor.moveToFirst();
            long lastSmileDate= cursor.getLong(0);
            if(enableDailyNotification){
                if(lastSmileDate + (AppConf.notificationDays * 3600000) < System.currentTimeMillis()){
                //if(lastSmileDate + 120000 < System.currentTimeMillis()){
                    sendNotification(getResources().getString(R.string.notification_message),this, AppMain.class,3);
                }
                /*else{
                    sendNotification("hiciste ra'e click hoy loco!",this, AppMain.class,3);
                }*/
            }
            if(enableWeeklyNotification){

            }

            String format="dd/MM/yyyy hh:mm:ss";
            Log.d(TAG,"last date: "+ TimeHelper.getDate(lastSmileDate,format) + " currDate: " + TimeHelper.getDate(System.currentTimeMillis(),format));

            cursor.close();
            dbh.close();
        }catch(Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    public static void sendNotification(String msg, Context context, Class<?> launchClass,Integer fragmentPos){
        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_action_call)
                .setContentTitle("Notifiqueichon")
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(msg);
        Intent resultIntent = new Intent(context,launchClass);
        if(fragmentPos != null)
            resultIntent.putExtra(AppMain.SELECT_FRAGMENT,fragmentPos);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationID= 001;

        NotificationManager mNM= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNM.notify(mNotificationID,mBuilder.build());
    }
}
