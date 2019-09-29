package com.fukazayo.geoslacker.datastore.preference

import android.content.Context
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
        private const val LAST_POST_CHAT_COMMAND_DATE = "LAST_POST_CHAT_COMMAND_DATE"

        fun loadAppSettings(context: Context): AppSettings? {
            val sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

            val slackToken = sharedPreferences.getString(SLACK_TOKEN, "") ?: return null
            val slackCommand = sharedPreferences.getString(SLACK_COMMAND, "") ?: return null
            val slackChannel = sharedPreferences.getString(SLACK_CHANNEL, "") ?: return null
            val getLatitude = sharedPreferences.getString(GEO_LATITUDE, "0.0")?.toDouble() ?: return null
            val getLongitude = sharedPreferences.getString(GEO_LONGITUDE, "0.0")?.toDouble() ?: return null

            val lastPostChatCommandDateMillis = sharedPreferences.getLong(LAST_POST_CHAT_COMMAND_DATE, 0)
            val lastPostChatCommandDate = if (lastPostChatCommandDateMillis != 0L) Date(lastPostChatCommandDateMillis) else null

            return AppSettings(
                slackToken = slackToken,
                slackChannel = slackChannel,
                slackCommand = slackCommand,
                geoLatitude = getLatitude,
                geoLongitude = getLongitude,
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
            editor.putLong(LAST_POST_CHAT_COMMAND_DATE, appSettings.lastPostChatCommandDate?.time ?: 0)

            editor.apply()
        }
    }
}
