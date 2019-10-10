package com.fukazayo.geoslacker.datasource.location

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fukazayo.geoslacker.common.Logger
import com.fukazayo.geoslacker.datasource.notification.NotificationClient
import com.fukazayo.geoslacker.datasource.preference.PreferenceClient
import com.fukazayo.geoslacker.datasource.slack.SlackClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            return
        }

        context?.let {
            when (geofencingEvent.geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    Logger.d("GeofenceBroadcastReceiver: ENTER")
                    postSlack(it, true)
                }
                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    Logger.d("GeofenceBroadcastReceiver: EXIT")
                    postSlack(it, false)
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun postSlack(context: Context, isEntered: Boolean) {
        PreferenceClient.loadAppSettings(context)?.let {
            SlackClient.request()
                .postChatCommand(
                    channel = it.slackChannel,
                    command = it.slackCommand,
                    token = it.slackToken
                )
                .retry(10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _ ->
                    val message = "Posted \"${it.slackCommand}\" to \"${it.slackChannel}\" channel."
                    Logger.i(message)

                    NotificationClient.sendNotification(
                        context,
                        "${if (isEntered) "Entered" else "Exited"} the region",
                        message
                    )
                }, { error ->
                    val message = "Failed to post \"${it.slackCommand}\" to \"${it.slackChannel}\" channel."
                    Logger.e(message, error)

                    NotificationClient.sendNotification(
                        context,
                        "${if (isEntered) "Entered" else "Exited"} the region",
                        message
                    )
                })
        }
    }
}
