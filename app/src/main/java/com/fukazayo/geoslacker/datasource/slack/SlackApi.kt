package com.fukazayo.geoslacker.datasource.slack

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface SlackApi {
    @POST("chat.command")
    fun postChatCommand(@Query("token") token: String,
                        @Query("channel") channel: String,
                        @Query("command") command: String): Single<Response<SlackApiResponse>>
}
