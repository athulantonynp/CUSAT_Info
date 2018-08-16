package crap.scrap.rider

import android.app.NotificationChannel
import android.app.NotificationManager
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.*
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannels()
        checkForWorker()
        initWebView()
    }

    private fun initWebView() {
        val webView = findViewById<WebView>(R.id.wb_home)
        try {

            webView.settings.javaScriptEnabled = true
            webView.settings.javaScriptCanOpenWindowsAutomatically = true
            webView.settings.domStorageEnabled = true
            webView.loadUrl("http://exam.cusat.ac.in/")
            webView.webChromeClient = WebChromeClient()
            webView.requestFocus()
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)


                }

                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    super.onReceivedError(view, request, error)

                }
            }

        } catch (e: Exception) {
        }

    }

    private fun createNotificationChannels() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val utils = NotificationChannelUtils(this)
            val channels = ArrayList<NotificationChannel>()
            channels.add(utils.getNotificationChannel("CHANNEL_ONE", "Default", null,
                    NotificationManager.IMPORTANCE_HIGH))
            utils.createNotificationChannels(channels)
        }

    }


    private fun checkForWorker() {
        try {
           WorkManager.getInstance()!!.getStatusesByTag(Constants.WORKER_TAG_CALL_UPLOAD)
                   .observe(this, Observer {

                       if(it==null||it.size==0||it.get(0).state.isFinished){
                           val constraints = Constraints.Builder()
                                   .setRequiredNetworkType(NetworkType.CONNECTED)
                                   .build()
                           val builder = PeriodicWorkRequest.Builder(BaseWorker::class.java,
                                   15, TimeUnit.MINUTES)
                                   .addTag(Constants.WORKER_TAG_CALL_UPLOAD)
                                   .setConstraints(constraints)
                           WorkManager.getInstance()!!.enqueue(builder.build())
                       }
                   })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
