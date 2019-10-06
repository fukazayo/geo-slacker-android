package com.fukazayo.geoslacker.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.fukazayo.geoslacker.common.Constants
import com.fukazayo.geoslacker.datasource.location.GeocoderClient
import com.fukazayo.geoslacker.datasource.location.GeofenceClient
import com.fukazayo.geoslacker.datasource.location.LocationClient
import com.fukazayo.geoslacker.datasource.preference.PreferenceClient
import com.fukazayo.geoslacker.entity.AppSettings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class MainViewModel : ViewModel() {
    lateinit var context: Context

    private var location: Location? = null
    private var isStarted = false

    val startedEvent: BehaviorSubject<Unit> = BehaviorSubject.create()
    val stoppedEvent: BehaviorSubject<Unit> = BehaviorSubject.create()

    val slackToken = ObservableField<String>("")
    val slackChannel = ObservableField<String>("")
    val slackCommand = ObservableField<String>("")
    val centralPoint = ObservableField<String>("")
    val radius = ObservableField<String>("")

    @SuppressLint("CheckResult")
    fun getCurrentLocation() {
        LocationClient.getCurrentLocation(context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                location = it

                centralPoint.set(GeocoderClient.getAddress(context, it.latitude, it.longitude))

                showToast(GeocoderClient.getAddress(context, it.latitude, it.longitude))
            }, {
                showToast(it.message ?: "")
            })
    }

    fun load() {
        val appSettings = PreferenceClient.loadAppSettings(context)

        appSettings?.let {
            slackToken.set(it.slackToken)
            slackChannel.set(it.slackChannel)
            slackCommand.set(it.slackCommand)
            centralPoint.set(GeocoderClient.getAddress(context, it.geoLatitude, it.geoLongitude))
            radius.set(it.geoRadius.toString())

            val savedLocation = Location("")
            savedLocation.latitude = it.geoLatitude
            savedLocation.longitude = it.geoLongitude
            location = savedLocation

            isStarted = it.isStarted
            if (isStarted) startedEvent.onNext(Unit) else stoppedEvent.onNext(Unit)

            syncGeofence()
        }
    }

    fun save() {
        location?.let { location ->
            val appSettings = AppSettings(
                slackToken = slackToken.get() ?: "",
                slackChannel = slackChannel.get() ?: "",
                slackCommand = slackCommand.get() ?: "",
                geoLatitude = location.latitude,
                geoLongitude = location.longitude,
                geoRadius = radius.get()?.toFloat() ?: Constants.GEOFENCING_DEFAULT_RADIUS,
                isStarted = isStarted,
                lastPostChatCommandDate = null
            )

            PreferenceClient.saveAppSettings(context, appSettings)
        } ?: showToast("Failed to save. Please get the current location.")
    }

    fun start() {
        isStarted = true

        save()
        syncGeofence()

        startedEvent.onNext(Unit)
    }

    fun stop() {
        isStarted = false

        save()
        syncGeofence()

        stoppedEvent.onNext(Unit)
    }

    private fun syncGeofence() {
        location?.let { location ->
            GeofenceClient(context).syncGeofence(location.latitude, location.longitude, Constants.GEOFENCING_DEFAULT_RADIUS, isStarted)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showToast(if (isStarted) "Started." else "Stopped.")
                }, {
                    showToast(it.toString())
                })
        } ?: showToast("Failed to ${if (isStarted) "start" else "stop"}.")
    }

    private fun showToast(message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}
