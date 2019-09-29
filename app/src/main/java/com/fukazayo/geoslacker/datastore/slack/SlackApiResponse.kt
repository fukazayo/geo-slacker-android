package com.fukazayo.geoslacker.datastore.slack

import com.squareup.moshi.Json

data class SlackApiResponse(
    @Json(name = "ok") var ok: String,
    @Json(name = "response") var response: String
)
