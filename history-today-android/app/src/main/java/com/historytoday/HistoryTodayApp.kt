package com.historytoday

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltAndroidApp
class HistoryTodayApp : Application() {

    override fun onCreate() {
        super.onCreate()
        installCrashHandler()
    }

    private fun installCrashHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("HistoryToday_CRASH", "Uncaught exception on thread: $thread", throwable)
            saveCrashToFile(throwable)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    private fun saveCrashToFile(throwable: Throwable) {
        try {
            val dir = File(getExternalFilesDir(null), "crash_logs")
            if (!dir.exists()) dir.mkdirs()
            val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
            val file = File(dir, "crash_$timestamp.txt")
            PrintWriter(FileWriter(file)).use { writer ->
                writer.println("Time: $timestamp")
                writer.println("App Version: ${packageManager.getPackageInfo(packageName, 0).versionName}")
                writer.println("Android SDK: ${android.os.Build.VERSION.SDK_INT}")
                writer.println("Device: ${android.os.Build.MANUFACTERER} ${android.os.Build.MODEL}")
                writer.println("---")
                throwable.printStackTrace(writer)
            }
            Log.i("HistoryToday_CRASH", "Crash log saved to: ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e("HistoryToday_CRASH", "Failed to save crash log", e)
        }
    }
}
