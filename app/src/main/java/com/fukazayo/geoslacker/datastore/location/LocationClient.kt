package com.fukazayo.geoslacker.datastore.location

import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import io.reactivex.Single

class LocationClient {
    companion object {
        fun getCurrentLocation(context: Context): Single<Location> {
            return Single.create { emitter ->
                val locationRequest = LocationRequest().apply {
                    interval = 10000
                    fastestInterval = 5000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        locationResult?.let {
                            emitter.onSuccess(it.lastLocation)
                        } ?: emitter.onError(Exception())
                    }
                }

                val fusedLocationClient = FusedLocationProviderClient(context)
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }
    }
}
