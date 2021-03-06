package com.fukazayo.geoslacker.entity

import java.util.*

data class AppSettings(
    val slackToken: String,
    val slackChannel: String,
    val slackCommand: String,
    val geoLatitude: Double,
    val geoLongitude: Double,
    val geoRadius: Float,
    val isStarted: Boolean,
    val lastPostChatCommandDate: Date?
)
