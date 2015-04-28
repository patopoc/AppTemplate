package app.com.apptemplate.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by steve on 27/04/2015.
 */
public class SessionControl {
    private boolean requireSession;
    private SharedPreferences preferences;

    public SessionControl(Activity activity, boolean required){
        preferences= activity.getPreferences(Context.MODE_PRIVATE);
        requireSession= required;
    }

    public boolean isRequireSession(){
        return requireSession;
    }

    public boolean checkSessionData(){
        if(preferences.getString("USER","").equals("") ||
           preferences.getString("PASSWORD","").equals("")){
            return false;
        }
        return true;
    }

}
