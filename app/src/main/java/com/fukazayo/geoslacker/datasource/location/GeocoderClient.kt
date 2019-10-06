package com.fukazayo.geoslacker.datasource.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*

class GeocoderClient {
    companion object {
        fun getAddress(context: Context, latitude: Double, longitude: Double): String {
            val addresses: MutableList<Address>

            try {
                addresses = Geocoder(context, Locale.getDefault()).getFromLocation(latitude, longitude, 1)
            } catch (e: Exception) {
                return e.toString()
            }

            val addressString = StringBuilder()

            addresses.forEach {
                for (cnt in 0..it.maxAddressLineIndex) {
                    addressString.append(it.getAddressLine(cnt))
                }
            }

            return addressString.toString()
        }
    }
}
