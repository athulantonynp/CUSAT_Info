package crap.scrap.rider;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NotificationChannelUtils {

    Context context;

    public NotificationChannelUtils(Context context) {
        this.context = context;
    }

    private NotificationManager getNotificationManager(Context context){

        if(context==null){
            return null;
        }

         return (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
    }


    public NotificationChannel getNotificationChannel(String channelID, String name, @Nullable String description,
                                                      int importance){

        NotificationChannel channel=new NotificationChannel(channelID,name,importance);
        if (description!=null){
            channel.setDescription(description);
        }
        return channel;
    }


    public void createNotificationChannels(ArrayList<NotificationChannel> channels){

        try {
            getNotificationManager(context).createNotificationChannels(channels);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
