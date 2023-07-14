package com.cammace.comicscroll.data.network.retrofit

import com.cammace.comicscroll.data.network.XKCDRemoteDataSource
import com.cammace.comicscroll.data.network.model.ComicResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit API declaration for XKCD JSON API
 */
private interface RetrofitXKCDApi {

    @GET("/info.0.json")
    suspend fun getLatest(): ComicResponse

    @GET("/{comicId}/info.0.json")
    suspend fun getComic(@Path("comicId") comicId: Int): ComicResponse
}

private const val BASE_URL = "https://xkcd.com/"

/**
 * [Retrofit] backed [XKCDRemoteDataSource]
 */
@Singleton
class RetrofitXKCDRemoteDataSource @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) : XKCDRemoteDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RetrofitXKCDApi::class.java)

    override suspend fun getLatest(): ComicResponse = networkApi.getLatest()

    override suspend fun getComic(comicId: Int): ComicResponse = networkApi.getComic(comicId)
}
