package app.com.apptemplate.widgets;

import android.app.IntentService;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import app.com.apptemplate.R;
import app.com.apptemplate.utils.DataBaseHelper;

/**
 * Created by steve on 05/05/2015.
 */
public class WidgetUpdateService extends IntentService {
    final String TAG="WidgetUpdateService";

    DataBaseHelper dbh= new DataBaseHelper(this);
    int count=0;

    public WidgetUpdateService(){
        super("WidgetUpdateService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundles= intent.getExtras();
        String action= bundles.getString("action");
        Log.d(TAG,"accion: "+action);
        if(action.equals("update")){

            try {
                dbh.createDataBase();
                SQLiteDatabase database = dbh.getReadableDatabase();
                ContentValues val = new ContentValues();
                val.put("date", System.currentTimeMillis() / 1000);
                database.insert("smiles", null, val);
                Log.d("WidgetProvider", "row inserted");
                Cursor cursor = database.rawQuery("select count(date) from smiles", null);
                cursor.moveToFirst();
                count = cursor.getInt(0);
                cursor.close();
                WidgetProvider.updateViews(this,count);

            }catch(Exception e){
                Log.e(TAG,e.getMessage());
            }

        }
        else if(action.equals("delete")){
            try {
                Log.d(TAG,"entro aqui que m...");
                dbh.createDataBase();
                SQLiteDatabase database = dbh.getReadableDatabase();
                int result= database.delete("smiles","1",null);
                Log.d("WidgetProvider", "rows deleted: "+result);
                WidgetProvider.updateViews(this,count);

            }catch(Exception e){
                Log.e(TAG,e.getMessage());
            }
        }
    }

    /*public void updateViews(Context context){
        RemoteViews views= new RemoteViews(this.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.btn_widget_display, "" + count);
        //views.setTextColor(R.id.widget_text, Color.RED);

        ComponentName thisWidget= new ComponentName(this, WidgetProvider.class);
        AppWidgetManager appWidgetManager= AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(thisWidget,views);
        Log.d(TAG, "Widget updated");
    }*/
}
