package com.fukazayo.geoslacker.datasource.preference

import android.content.Context
import com.fukazayo.geoslacker.common.Constants
import com.fukazayo.geoslacker.entity.AppSettings
import java.util.*

class PreferenceClient {
    companion object {
        private const val PREFERENCES = "preferences"

        private const val SLACK_TOKEN = "SLACK_TOKEN"
        private const val SLACK_CHANNEL = "SLACK_CHANNEL"
        private const val SLACK_COMMAND = "SLACK_COMMAND"
        private const val GEO_LATITUDE = "GEO_LATITUDE"
        private const val GEO_LONGITUDE = "GEO_LONGITUDE"
        private const val GEO_RADIUS = "GEO_RADIUS"
        private const val IS_STARTED = "IS_STARTED"
        private const val LAST_POST_CHAT_COMMAND_DATE = "LAST_POST_CHAT_COMMAND_DATE"

        fun loadAppSettings(context: Context): AppSettings? {
            val sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

            val slackToken = sharedPreferences.getString(SLACK_TOKEN, "") ?: return null
            val slackCommand = sharedPreferences.getString(SLACK_COMMAND, "") ?: return null
            val slackChannel = sharedPreferences.getString(SLACK_CHANNEL, "") ?: return null
            val geoLatitude = sharedPreferences.getString(GEO_LATITUDE, "0.0")?.toDouble() ?: return null
            val geoLongitude = sharedPreferences.getString(GEO_LONGITUDE, "0.0")?.toDouble() ?: return null
            val geoRadius = sharedPreferences.getString(GEO_RADIUS, Constants.GEOFENCING_DEFAULT_RADIUS.toString())?.toFloat() ?: return null
            val isStarted = sharedPreferences.getBoolean(IS_STARTED, false)

            val lastPostChatCommandDateMillis = sharedPreferences.getLong(LAST_POST_CHAT_COMMAND_DATE, 0)
            val lastPostChatCommandDate = if (lastPostChatCommandDateMillis != 0L) Date(lastPostChatCommandDateMillis) else null

            return AppSettings(
                slackToken = slackToken,
                slackChannel = slackChannel,
                slackCommand = slackCommand,
                geoLatitude = geoLatitude,
                geoLongitude = geoLongitude,
                geoRadius = geoRadius,
                isStarted = isStarted,
                lastPostChatCommandDate = lastPostChatCommandDate
            )
        }

        fun saveAppSettings(context: Context, appSettings: AppSettings) {
            val sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putString(SLACK_TOKEN, appSettings.slackToken)
            editor.putString(SLACK_CHANNEL, appSettings.slackChannel)
            editor.putString(SLACK_COMMAND, appSettings.slackCommand)
            editor.putString(GEO_LATITUDE, appSettings.geoLatitude.toString())
            editor.putString(GEO_LONGITUDE, appSettings.geoLongitude.toString())
            editor.putString(GEO_RADIUS, appSettings.geoRadius.toString())
            editor.putBoolean(IS_STARTED, appSettings.isStarted)
            editor.putLong(LAST_POST_CHAT_COMMAND_DATE, appSettings.lastPostChatCommandDate?.time ?: 0)

            editor.apply()
        }
    }
}
