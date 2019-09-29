package com.fukazayo.geoslacker.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.fukazayo.geoslacker.common.Constants
import com.fukazayo.geoslacker.datastore.location.GeocoderClient
import com.fukazayo.geoslacker.datastore.location.GeofenceClient
import com.fukazayo.geoslacker.datastore.location.LocationClient
import com.fukazayo.geoslacker.datastore.preference.PreferenceClient
import com.fukazayo.geoslacker.entity.AppSettings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {
    lateinit var context: Context

    private var location: Location? = null

    val slackToken = ObservableField<String>("")
    val slackChannel = ObservableField<String>("")
    val slackCommand = ObservableField<String>("")
    val centralPoint = ObservableField<String>("")

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

            val savedLocation = Location("")
            savedLocation.latitude = it.geoLatitude
            savedLocation.longitude = it.geoLongitude
            location = savedLocation
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
                lastPostChatCommandDate = null
            )

            PreferenceClient.saveAppSettings(context, appSettings)

            showToast("Saved.")
        } ?: showToast("Failed to save.")
    }

    fun initialize() {
        val appSettings = AppSettings(
            slackToken = "",
            slackChannel = "",
            slackCommand = "",
            geoLatitude = 0.0,
            geoLongitude = 0.0,
            lastPostChatCommandDate = null
        )

        PreferenceClient.saveAppSettings(context, appSettings)

        slackToken.set("")
        slackChannel.set("")
        slackCommand.set("")
        centralPoint.set(GeocoderClient.getAddress(context, 0.0, 0.0))

        showToast("Initialized.")
    }

    @SuppressLint("CheckResult")
    fun start() {
        location?.let { location ->
            GeofenceClient(context).addGeofence(location.latitude, location.longitude, Constants.GEOFENCING_RADIUS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showToast("Started.")
                }, {
                    showToast(it.toString())
                })
        } ?: showToast("Failed to start.")
    }

    @SuppressLint("CheckResult")
    fun stop() {
        GeofenceClient(context).removeGeofencing()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showToast("Stopped.")
            }, {
                showToast(it.toString())
            })
    }

    private fun showToast(message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}
