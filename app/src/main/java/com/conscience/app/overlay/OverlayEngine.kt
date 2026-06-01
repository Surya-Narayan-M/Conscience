package com.conscience.app.overlay

import android.content.Context
import android.content.Intent

object OverlayEngine {

    fun showEntryIntervention(context: Context) {
        val intent = Intent(context, EntryInterventionActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_NO_HISTORY
            )
        }
        context.startActivity(intent)
    }

    fun showRealityCheck(context: Context) {
        val intent = Intent(context, RealityCheckOverlayActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
        }
        context.startActivity(intent)
    }
}
