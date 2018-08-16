package crap.scrap.rider

import android.util.Log

class Logger{

    companion object {
        fun e(message:String){
            Log.e("APP",message)
        }
        fun e(message:Int){
            Log.e("APP",message.toString())
        }
    }
}