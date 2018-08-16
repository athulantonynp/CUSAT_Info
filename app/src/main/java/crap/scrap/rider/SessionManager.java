package crap.scrap.rider;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by napster on 07/07/15.
 */
public class SessionManager {

    SharedPreferences preferences;
    Context context;

    static SessionManager instance;

    public static SessionManager getInstance(Context applicationContext) {
        if (instance == null) {
            instance = new SessionManager(applicationContext);
            return instance;
        }
        return instance;
    }

    private SessionManager(Context applicationContext) {
        this.context = applicationContext;
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    public boolean hasChangeInHomeResponse(String newResponse) {
        String oldOne=preferences.getString("home_table",null);
        if(oldOne!=null){
            if(!oldOne.equals(newResponse)){
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("home_table", newResponse);
                editor.commit();
                editor.apply();
                return true;
            }else {
                return false;
            }
        }else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("home_table", newResponse);
            editor.commit();
            editor.apply();
            return false;
        }
    }

    public boolean hasChangeInLinkResponse(String newResponse) {
        String oldOne=preferences.getString("link_table",null);
        if(oldOne!=null){
            if(!oldOne.equals(newResponse)){
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("link_table", newResponse);
                editor.commit();
                editor.apply();
                return true;
            }else {
                return false;
            }
        }else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("link_table", newResponse);
            editor.commit();
            editor.apply();
            return false;
        }
    }



}
