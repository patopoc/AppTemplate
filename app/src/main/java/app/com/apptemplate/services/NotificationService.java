package app.com.apptemplate.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import app.com.apptemplate.AppMain;
import app.com.apptemplate.R;
import app.com.apptemplate.utils.DataBaseHelper;
import app.com.apptemplate.utils.TimeHelper;

/**
 * Created by steve on 06/05/2015.
 */
public class NotificationService extends IntentService {
    final String TAG="NotificationService";

    public NotificationService(){
        super("NotificationService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"launching notification");
        sendNotification("hace pue click loco!");
        DataBaseHelper dbh= new DataBaseHelper(this);
        try {
            dbh.createDataBase();
            SQLiteDatabase database = dbh.getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from smiles limit 1", null);
            cursor.moveToFirst();
            long date= cursor.getLong(0);

            String format="dd/MM/yyyy hh:mm:ss";
            Log.d(TAG,"last date: "+ TimeHelper.getDate(date,format) + " currDate: " + TimeHelper.getDate(System.currentTimeMillis(),format));

            cursor.close();
        }catch(Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    private void sendNotification(String msg){
        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_action_call)
                .setContentTitle("Notifiqueichon")
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(msg);
        Intent resultIntent = new Intent(this,AppMain.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationID= 001;

        NotificationManager mNM= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNM.notify(mNotificationID,mBuilder.build());
    }
}
