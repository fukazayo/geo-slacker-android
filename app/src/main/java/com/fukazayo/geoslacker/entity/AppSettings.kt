package com.fukazayo.geoslacker.entity

import java.util.*

data class AppSettings(
    val slackToken: String,
    val slackChannel: String,
    val slackCommand: String,
    val geoLatitude: Double,
    val geoLongitude: Double,
    val lastPostChatCommandDate: Date?
)
