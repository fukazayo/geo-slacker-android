package com.fukazayo.geoslacker.datastore.location

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.fukazayo.geoslacker.datastore.preference.PreferenceClient
import com.fukazayo.geoslacker.datastore.slack.SlackClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GeofenceTransitionsIntentService : IntentService("GeofenceTransitionsIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        when (GeofencingEvent.fromIntent(intent).geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Log.d("GeofenceService", "enter")
                postSlack()
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Log.d("GeofenceService", "exit")
                postSlack()
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun postSlack() {
        PreferenceClient.loadAppSettings(this)?.let {
            SlackClient.request()
                .postChatCommand(
                    channel = it.slackChannel,
                    command = it.slackCommand,
                    token = it.slackToken
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {})
        }
    }
}
