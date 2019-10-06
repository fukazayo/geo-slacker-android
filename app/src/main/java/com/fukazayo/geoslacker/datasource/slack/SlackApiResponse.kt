package com.fukazayo.geoslacker.datasource.slack

import com.squareup.moshi.Json

data class SlackApiResponse(
    @Json(name = "ok") var ok: Boolean,
    @Json(name = "keep_input") var keepInput: Boolean
)
