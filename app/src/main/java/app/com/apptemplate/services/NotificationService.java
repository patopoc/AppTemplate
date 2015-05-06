package app.com.apptemplate.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
}
