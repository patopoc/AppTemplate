package app.com.apptemplate.widgets;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.com.apptemplate.AppMain;
import app.com.apptemplate.R;
import app.com.apptemplate.services.NotificationService;
import app.com.apptemplate.utils.DataBaseHelper;

/**
 * Created by steve on 05/05/2015.
 */
public class WidgetProvider extends AppWidgetProvider {
    final String TAG="WidgetProvider";

    public static String ACTION_UPDATE="ActionUpdate";
    public static String ACTION_DELETE="ActionDelete";

    @Override
    public void onEnabled(Context context){
        Log.d(TAG,"setting alarm");
        setAlarm(context);
        super.onEnabled(context);
    }

    public void setAlarm(Context context) {

        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.DAY_OF_YEAR,1);
        calendar.set(Calendar.HOUR_OF_DAY, 13); // if u need run 2PM use 14
        calendar.set(Calendar.MINUTE, 35);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationService.class);
        //intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);

        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);
    }


    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){

        try {
            /*dbh.createDataBase();
            SQLiteDatabase database= dbh.getReadableDatabase();
            ContentValues val= new ContentValues();
            val.put("date",System.currentTimeMillis()/1000);
            database.insert("smiles",null,val);
            Log.d("WidgetProvider","row inserted");
            Cursor cursor=database.rawQuery("select count(date) from smiles",null);
            cursor.moveToFirst();
            count= cursor.getInt(0);
            cursor.close();

            Log.d("WidgetProviedr", "value: " + getDate(System.currentTimeMillis()));

            /*for (int i = 0; i < appWidgetIds.length; i++) {
                int appWidgetID = appWidgetIds[i];
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

                views.setTextViewText(R.id.widget_text, "valor: " + count);
                views.setTextColor(R.id.widget_text, Color.RED);

                Intent intent = new Intent(context, WidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.button_widget, pendingIntent);

                appWidgetManager.updateAppWidget(appWidgetID, views);
            }*/

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            Intent intentUpdate = new Intent(context, WidgetProvider.class);
            intentUpdate.setAction(ACTION_UPDATE);
            PendingIntent pendingUpdate= PendingIntent.getBroadcast(context,100,intentUpdate,PendingIntent.FLAG_CANCEL_CURRENT);

            Intent intentDelete = new Intent(context, AppMain.class);
            //intentDelete.setAction(ACTION_DELETE);
            intentDelete.putExtra(AppMain.SELECT_FRAGMENT,2);
            PendingIntent pendingDelete= PendingIntent.getActivity(context,200,intentDelete,PendingIntent.FLAG_CANCEL_CURRENT);

            views.setOnClickPendingIntent(R.id.btn_widget_update, pendingUpdate);
            views.setOnClickPendingIntent(R.id.btn_widget_delete, pendingDelete);

            appWidgetManager.updateAppWidget(appWidgetIds[0], views);



        }catch(Exception e){
            Log.e("WidgetProvider",e.getMessage());
        }
    }

    @Override
    public void onReceive(Context context, Intent intent){

        DataBaseHelper dbh= new DataBaseHelper(context);
        int count=0;
        if(intent.getAction().equals(ACTION_UPDATE)){
            try {
            dbh.createDataBase();
            SQLiteDatabase database= dbh.getReadableDatabase();
            ContentValues val= new ContentValues();
            val.put("date",System.currentTimeMillis());

            database.insert("smiles", null, val);
            Log.d("WidgetProvider","row inserted");
            Cursor cursor=database.rawQuery("select count(date) from smiles",null);
            cursor.moveToFirst();
            count= cursor.getInt(0);
            cursor.close();

            updateViews(context, count);

            }catch(Exception e){
                Log.e("WidgetProvider",e.getMessage());
            }
        }

        else if(intent.getAction().equals(ACTION_DELETE)){
            try {
                Log.d(TAG,"entro aqui que m...");
                dbh.createDataBase();
                SQLiteDatabase database = dbh.getReadableDatabase();
                int result= database.delete("smiles","1",null);
                Log.d("WidgetProvider", "rows deleted: "+result);
                updateViews(context,0);

            }catch(Exception e){
                Log.e(TAG,e.getMessage());
            }
        }
        dbh.close();

        super.onReceive(context, intent);

    }

    public void updateViews(Context context, int count){
        RemoteViews views= new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.widget_text, "valor: " + count);
        views.setTextColor(R.id.widget_text, Color.RED);

        ComponentName thisWidget= new ComponentName(context, WidgetProvider.class);
        AppWidgetManager appWidgetManager= AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(thisWidget,views);
        Log.d(TAG, "Widget updated");
    }

}
