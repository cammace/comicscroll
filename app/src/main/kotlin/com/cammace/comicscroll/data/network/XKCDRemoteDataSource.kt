package com.cammace.comicscroll.data.network

import com.cammace.comicscroll.data.network.model.ComicResponse

/**
 * Interface representing network calls to the XKCD backend.
 */
interface XKCDRemoteDataSource {

    suspend fun getLatest(): ComicResponse

    suspend fun getComic(comicId: Int): ComicResponse
}
