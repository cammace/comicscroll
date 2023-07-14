package com.cammace.comicscroll.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.cammace.comicscroll.data.network.XKCDRemoteDataSource
import com.cammace.comicscroll.data.page.XKCDPagingSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The app's data is managed using the Paging library. [XKCDPagingSource] handles in-memory caching
 * and fetching additional data as needed.
 */
@Singleton
class ComicRepository @Inject constructor(
    private val network: XKCDRemoteDataSource
) {

    fun getComicsStream() = Pager(
        config = PagingConfig(pageSize = COMICS_PAGE_SIZE),
        pagingSourceFactory = { XKCDPagingSource(network) }
    ).flow

    companion object {
        private const val COMICS_PAGE_SIZE = 10
    }
}
