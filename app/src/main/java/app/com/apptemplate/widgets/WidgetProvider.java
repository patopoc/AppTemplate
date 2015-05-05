package app.com.apptemplate.widgets;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import app.com.apptemplate.R;
import app.com.apptemplate.utils.DataBaseHelper;

/**
 * Created by steve on 05/05/2015.
 */
public class WidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        DataBaseHelper dbh= new DataBaseHelper(context);
        int count=0;
        try {
            dbh.createDataBase();
            SQLiteDatabase database= dbh.getReadableDatabase();
            ContentValues val= new ContentValues();
            val.put("date",System.currentTimeMillis()/1000);
            val.put("date",0);
            database.insert("smiles",null,val);
            Log.d("WidgetProvider","row inserted");
            Cursor cursor=database.rawQuery("select count(date) from smiles",null);
            cursor.moveToFirst();
            count= cursor.getInt(0);
            cursor.close();

            Log.d("WidgetProviedr", "value: " + count);

            for (int i = 0; i < appWidgetIds.length; i++) {
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
            }
        }catch(Exception e){
            Log.e("WidgetProvider",e.getMessage());
        }
    }
}
