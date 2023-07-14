package com.cammace.comicscroll.data.page

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cammace.comicscroll.data.model.Comic
import com.cammace.comicscroll.data.model.asExternalModel
import com.cammace.comicscroll.data.network.XKCDRemoteDataSource
import kotlin.math.max

/**
 * A [PagingSource] that fetches pages of XKCD comics from a network data source.
 *
 * @property network The network data source from which the comics are fetched.
 */
class XKCDPagingSource(private val network: XKCDRemoteDataSource) : PagingSource<Int, Comic>() {

    companion object {
        // Only display the latest 100 comics
        private const val MAXIMUM_FEED_COMICS = 100

        // Default value for comic ID when it's undefined
        private const val COMIC_ID_UNDEFINED = Int.MIN_VALUE
    }

    // Holds the ID of the latest comic that becomes our page key upperbound.
    private var latestComicId: Int = COMIC_ID_UNDEFINED

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comic> {
        val comicDataList = mutableListOf<Comic>()
        var firstComicId = params.key

        if (firstComicId == null) {
            val latestComicResponse = try {
                network.getLatest()
            } catch (exception: Exception) {
                return LoadResult.Error(exception)
            }
            //
            latestComicId = latestComicResponse.num
            comicDataList += latestComicResponse.asExternalModel()
            firstComicId = latestComicId
        }

        // Prevent fetching comics beyond our MAXIMUM_FEED_COMICS value.
        val lastComicId = max(firstComicId - params.loadSize, latestComicId - MAXIMUM_FEED_COMICS)

        // Each comic requires individual network call. iterate from our firstComicId
        for (comicId in firstComicId downTo lastComicId) {
            // Skip fetching latest comic, it's already added to comicDataList on page refresh.
            if (comicId == latestComicId) continue

            try {
                comicDataList += network.getComic(comicId).asExternalModel()
            } catch (exception: Exception) {
                return LoadResult.Error(exception)
            }
        }

        // Load as many items as hinted by params.loadSize in descending sequential order.
        firstComicId.downTo(lastComicId)
            .take(params.loadSize)
            .filterNot { it == latestComicId } // The latest comic data already fetched via network.getLatest call,
            .mapTo(comicDataList) { comicId ->
                try {
                    network.getComic(comicId).asExternalModel()
                } catch (exception: Exception) {
                    return LoadResult.Error(exception)
                }
            }

        val prevKey = if (firstComicId == latestComicId) null else firstComicId + 1
        val nextKey =
            if (lastComicId < latestComicId - MAXIMUM_FEED_COMICS || firstComicId == lastComicId) null else lastComicId

        return LoadResult.Page(
            data = comicDataList,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Comic>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.num
        }
    }
}
