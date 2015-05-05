package app.com.apptemplate.widgets;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.widget.RemoteViews;

import app.com.apptemplate.R;

/**
 * Created by steve on 05/05/2015.
 */
public class WidgetProvider extends AppWidgetProvider {
    static int count=0;
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for(int i=0; i< appWidgetIds.length;i++){
            int appWidgetID= appWidgetIds[i];
            RemoteViews views= new RemoteViews(context.getPackageName(),R.layout.widget_layout);
            count++;
            views.setTextViewText(R.id.widget_text, "valor: " + count);
            views.setTextColor(R.id.widget_text, Color.RED);

            Intent intent= new Intent(context,WidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent= PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.button_widget, pendingIntent);
            AlarmManager am= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.cancel(pendingIntent);
            am.setRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime(),2000,pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetID,views);
        }
    }
}
