package com.conscience.app.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.conscience.app.overlay.EntryInterventionActivity
import com.conscience.app.overlay.RealityCheckOverlayActivity
import com.conscience.app.overlay.OverlayEngine
import com.conscience.app.session.SessionManager
import com.conscience.app.utils.Constants

class ConscienceAccessibilityService : AccessibilityService() {

    private var currentPackage: String = ""
    private var instagramStartTime: Long = 0L
    private var isInstagramActive: Boolean = false
    private var ignoreNonInstagramUntilMs: Long = 0L
    private var lastNonInstagramPackage: String = ""
    private var lastNonInstagramAtMs: Long = 0L

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                    AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
            notificationTimeout = 100
        }
        serviceInfo = info

        SessionManager.getInstance(applicationContext).onOverlayTrigger = {
            if (isInstagramActive && currentPackage == Constants.INSTAGRAM_PACKAGE) {
                OverlayEngine.showRealityCheck(applicationContext)
            } else {
                Log.d(TAG, "Overlay trigger ignored; instagramActive=${isInstagramActive} current=${currentPackage}")
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return

        val packageName = event.packageName?.toString() ?: return
        val className = event.className?.toString().orEmpty()

        if (packageName == applicationContext.packageName &&
            (className == EntryInterventionActivity::class.java.name ||
                className == RealityCheckOverlayActivity::class.java.name)
        ) {
            currentPackage = packageName
            return
        }

        val now = System.currentTimeMillis()

        when {
            packageName == Constants.INSTAGRAM_PACKAGE && !isInstagramActive -> {
                // Instagram just came to foreground
                onInstagramOpened()
            }
            packageName != Constants.INSTAGRAM_PACKAGE && isInstagramActive -> {
                if (now >= ignoreNonInstagramUntilMs) {
                    if (shouldIgnoreNonInstagram(packageName, className)) {
                        currentPackage = packageName
                        return
                    }

                    if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (packageName == lastNonInstagramPackage &&
                            now - lastNonInstagramAtMs >= NON_INSTAGRAM_DEBOUNCE_MS
                        ) {
                            Log.d(
                                TAG,
                                "Instagram closed by ${packageName}/${className} eventType=${event.eventType}"
                            )
                            onInstagramClosed()
                        } else {
                            lastNonInstagramPackage = packageName
                            lastNonInstagramAtMs = now
                        }
                    }
                }
            }
        }

        currentPackage = packageName
    }

    private fun onInstagramOpened() {
        isInstagramActive = true
        instagramStartTime = System.currentTimeMillis()
        ignoreNonInstagramUntilMs = instagramStartTime +
            Constants.ENTRY_INTERVENTION_DURATION_MS + 1000L
        lastNonInstagramPackage = ""
        lastNonInstagramAtMs = 0L

        Log.d(TAG, "Instagram opened — firing intervention")

        // Notify SessionManager
        SessionManager.getInstance(applicationContext).onInstagramOpened()

        // Fire Entry Intervention
        val intent = Intent(this, EntryInterventionActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP or
                        Intent.FLAG_ACTIVITY_NO_HISTORY
            )
        }
        startActivity(intent)
    }

    private fun onInstagramClosed() {
        isInstagramActive = false
        val sessionDurationMs = System.currentTimeMillis() - instagramStartTime

        Log.d(TAG, "Instagram closed — session duration: ${sessionDurationMs}ms")

        // Notify SessionManager
        SessionManager.getInstance(applicationContext).onInstagramClosed(sessionDurationMs)

        // Broadcast for overlay cleanup
        sendBroadcast(Intent(Constants.ACTION_INSTAGRAM_CLOSED))
    }

    override fun onInterrupt() {
        isInstagramActive = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isInstagramActive) {
            onInstagramClosed()
        }
    }

    companion object {
        private const val TAG = "ConscienceAccessibilityService"
        private const val NON_INSTAGRAM_DEBOUNCE_MS = 800L
        private val IGNORED_NON_INSTAGRAM_PACKAGES = setOf(
            "com.google.android.inputmethod.latin",
            "com.android.launcher3"
        )
    }

    private fun shouldIgnoreNonInstagram(packageName: String, className: String): Boolean {
        if (packageName in IGNORED_NON_INSTAGRAM_PACKAGES) return true
        if (className.contains("SoftInputWindow")) return true
        return false
    }
}
