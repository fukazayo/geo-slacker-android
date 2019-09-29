package com.fukazayo.geoslacker.datastore.slack

import com.fukazayo.geoslacker.BuildConfig
import com.fukazayo.geoslacker.common.Constants
import com.squareup.moshi.Moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class SlackClient {
    companion object {
        fun request(): SlackApi {
            val client = buildHttpClient()
            val moshi = Builder().add(KotlinJsonAdapterFactory()).build()
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.URL_SLACK_API_BASE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(client)
                .build()

            return retrofit.create(SlackApi::class.java)
        }

        private fun buildHttpClient(): OkHttpClient {
            val client = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                client.addInterceptor(logging)
            }

            return client.build()
        }
    }
}
