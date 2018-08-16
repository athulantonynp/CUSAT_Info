package crap.scrap.rider

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.TaskStackBuilder
import androidx.work.Worker
import org.jsoup.Jsoup
import java.util.*


class BaseWorker: Worker() {

    override fun doWork(): Result {
        try {
            scrapAndGetResults()

        }catch (e:Exception){
            return Result.FAILURE
        }
        return Result.SUCCESS
    }

    fun scrapAndGetResults(){

        try {
            val homeDoc= Jsoup.connect("http://exam.cusat.ac.in/").get()
            val hallTicketDoc= Jsoup.connect("http://exam.cusat.ac.in/erp5/cusat/Cusat-Hall-Ticket/form1").get()

            val hasHomeChange:Boolean=false
            val hasLinkChange:Boolean=false

            if(homeDoc!=null){
                val homeTables:String=""
                for (element in homeDoc.getElementsByTag("table")){
                    homeTables.plus(element)
                }
                homeTables.trim()
                if(homeTables.isNotBlank()&&
                        homeTables.isNotEmpty()){
                   hasHomeChange== SessionManager.getInstance(applicationContext).hasChangeInHomeResponse(homeTables)
                }
            }

            if(hallTicketDoc!=null){
                val linkTables:String=""
                for (element in hallTicketDoc.getElementsByTag("table")){
                    linkTables.plus(element)
                }
                linkTables.trim()
                if(linkTables.isNotEmpty()&&linkTables.isNotBlank()){
                    hasLinkChange== SessionManager.getInstance(applicationContext).hasChangeInLinkResponse(linkTables)

                }
            }

            if(hasHomeChange==false&&hasLinkChange==false){
                Logger.e("NO CHANGE YET")
            }else{
                if(hasHomeChange){
                    sendNotificationForState(1)
                }else{
                    if (hasLinkChange){
                        sendNotificationForState(2)
                    }
                }
            }

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun sendNotificationForState(state: Int) {

        val intent=Intent(applicationContext,WebviewActivity::class.java)
        intent.putExtra("link","http://exam.cusat.ac.in/")

        val message:String="കുസാറ്റിൽ നിന്നും ഒരു കമ്പി ഉണ്ട്...ഹോം പേജിൽ തന്നെ കമ്പി ആയിട്ടുണ്ട്."
        if(state==2){
            message=="കുസാറ്റിൽ നിന്നും ഒരു ഹാൾ ടിക്കറ്റ് വന്നിട്ടുണ്ട്...ഇപ്പോ നോക്കിയാൽ പിന്നെ മൂഞ്ചണ്ട !"
            intent.putExtra("link","http://exam.cusat.ac.in/erp5/cusat/Cusat-Hall-Ticket/form1")
        }
        val summaryStackBuilder = TaskStackBuilder.create(applicationContext)
        summaryStackBuilder.addNextIntent(intent)
        val summaryIntent = summaryStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val a=Notification.Builder(applicationContext, "CHANNEL_ONE")
                    .setContentTitle("ശ്രദ്ധിക്കൂ .....")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.xc)
                    .setAutoCancel(true)
                    .build()
             try {
                 val notification = Uri.parse("android.resource://"
                         + applicationContext.packageName + "/" + R.raw.notifc)
                 val r = RingtoneManager.getRingtone(applicationContext, notification)
                 r.play()
             } catch (e: Exception) {
                 e.printStackTrace()
             }
            a.contentIntent=summaryIntent
             a.defaults = Notification.DEFAULT_LIGHTS
            getNotificationManager(applicationContext).notify(5,a)
        } else {
           val b:Notification= Notification.Builder(applicationContext)
                    .setContentTitle("ശ്രദ്ധിക്കൂ .....")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.xc)
                    .setAutoCancel(true)
                    .build()
             b.sound = Uri.parse("android.resource://" + applicationContext.getPackageName()
                     + "/" + R.raw.notifc)

             b.defaults = Notification.DEFAULT_LIGHTS
            b.contentIntent=summaryIntent
            getNotificationManager(applicationContext).notify(6,b)
        }
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}