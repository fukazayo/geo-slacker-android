package com.fukazayo.geoslacker.datastore.location

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import io.reactivex.Completable

class GeofenceClient(context: Context) {
    private val geofencingClient = LocationServices.getGeofencingClient(context)
    private val pendingIntent: PendingIntent

    init {
        val intent = Intent(context, GeofenceTransitionsIntentService::class.java)
        pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun addGeofence(latitude: Double, longitude: Double, radius: Float): Completable {
        return Completable.create { emitter ->
            val builder = Geofence.Builder()
            builder.apply {
                setRequestId("geofence")
                setCircularRegion(latitude, longitude, radius)
                setExpirationDuration(Geofence.NEVER_EXPIRE)
                setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            }

            val geofences = ArrayList<Geofence>()
            geofences.add(builder.build())

            val request = GeofencingRequest.Builder()
                .addGeofences(geofences)
                .build()

            geofencingClient.addGeofences(request, pendingIntent)?.run {
                addOnSuccessListener {
                    emitter.onComplete()
                }
                addOnFailureListener {
                    emitter.onError(it)
                }
            }
        }
    }

    fun removeGeofencing(): Completable {
        return Completable.create { emitter ->
            geofencingClient.removeGeofences(pendingIntent)
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }
}
