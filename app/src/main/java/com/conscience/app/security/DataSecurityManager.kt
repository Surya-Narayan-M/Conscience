package com.conscience.app.security

import android.content.Context
import android.util.Log
import com.conscience.app.data.db.AppDatabase
import com.conscience.app.data.prefs.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DataSecurityManager {

    private const val TAG = "DataSecurityManager"

    fun initialize(context: Context) {
        enforceDataRetention(context)
        verifyNoNetworkPermission(context)
    }

    private fun enforceDataRetention(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cutoff = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)
                AppDatabase.getInstance(context).sessionDao().deleteOldSessions(cutoff)
            } catch (e: Exception) {
                Log.e(TAG, "Data retention enforcement failed", e)
            }
        }
    }

    private fun verifyNoNetworkPermission(context: Context) {
        val pm = context.packageManager
        val result = pm.checkPermission(
            android.Manifest.permission.INTERNET,
            context.packageName
        )
        if (result == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "SECURITY ALERT: Internet permission detected. Data may be at risk.")
        }
    }

    fun wipeAllData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                AppDatabase.getInstance(context).clearAllTables()
                AppPreferences(context).clearAll()
                Log.i(TAG, "All user data wiped successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Data wipe failed", e)
            }
        }
    }
}
