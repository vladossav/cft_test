package com.example.cft_test

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface BinNumberApi {
    @GET("{bin_number}")
    suspend fun getCardNumberInfo(
        @Path("bin_number") number: String
    ): Response<CardItem>

    @GET("45717360")
    suspend fun getCardNumberInfo(): Response<CardItem>


    companion object {
        const val BASE_URL = "https://lookup.binlist.net/"

        fun create() : BinNumberApi {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory()).build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(BinNumberApi::class.java)
        }

    }
}