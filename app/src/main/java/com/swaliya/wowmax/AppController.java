package com.swaliya.wowmax;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
       /* AutoErrorReporter.get(this)
                .setEmailAddresses("agnaraut@gmail.com")
                .setEmailSubject("Crash Report Vidmax App")
                .start();*/
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                handleUncaughtException(thread, ex);

            }
        });
    }

    public void handleUncaughtException (Thread thread, Throwable e)
    {
        String stackTrace = Log.getStackTraceString(e);
        String message = e.getMessage();


        Intent intent = new Intent (Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra (Intent.EXTRA_EMAIL, new String[] {"agnaraut@gmail.com"});
        intent.putExtra (Intent.EXTRA_SUBJECT, "MyApp Crash log file");
        intent.putExtra (Intent.EXTRA_TEXT, stackTrace);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);

    }

}
