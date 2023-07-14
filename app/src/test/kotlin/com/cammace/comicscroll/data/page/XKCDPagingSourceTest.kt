package com.cammace.comicscroll.data.page

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams.Append
import androidx.paging.PagingSource.LoadParams.Refresh
import com.cammace.comicscroll.data.model.asExternalModel
import com.cammace.comicscroll.data.network.XKCDRemoteDataSource
import com.cammace.comicscroll.data.network.model.ComicResponse
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit test for [XKCDPagingSource].
 */
class XKCDPagingSourceTest {

    private lateinit var fakeRemoteDataSource: FakeXKCDRemoteDataSource
    private lateinit var pagingSource: XKCDPagingSource

    private val latestComicResponse = ComicResponse(
        month = "7",
        num = 2801,
        link = "",
        year = "2023",
        news = "",
        safeTitle = "Contact Merge",
        transcript = "",
        alt = "",
        img = "https://imgs.xkcd.com/comics/contact_merge.png",
        title = "Contact Merge",
        day = "12"
    )

    @Before
    fun setup() {
        fakeRemoteDataSource = FakeXKCDRemoteDataSource(latestComicResponse)
        pagingSource = XKCDPagingSource(fakeRemoteDataSource)
    }

    @Test
    fun `load returns page when network request successful`() = runTest {
        val loadSize = 10
        val comicsResponse = List(loadSize) {
            if (it == 0) return@List latestComicResponse
            val id = latestComicResponse.num - it
            latestComicResponse.copy(num = id, title = "title$id", img = "url$id")
        }
        fakeRemoteDataSource.comics = comicsResponse

        val loadResult = pagingSource.load(Refresh(key = null, loadSize = loadSize, placeholdersEnabled = false))

        check(loadResult is PagingSource.LoadResult.Page)
        loadResult.data.size shouldBe loadSize
        loadResult.data shouldBe comicsResponse.map(ComicResponse::asExternalModel)
    }

    @Test
    fun `load returns error when network request throws exception`() = runTest {
        fakeRemoteDataSource.exception = Exception()

        val loadResult = pagingSource.load(Refresh(key = null, loadSize = 10, placeholdersEnabled = false))

        check(loadResult is PagingSource.LoadResult.Error)
        loadResult.throwable shouldBe fakeRemoteDataSource.exception
    }

    @Test
    fun `load returns correct prevKey and nextKey when there are more comics available`() = runTest {
        fakeRemoteDataSource.comics = List(15) {
            if (it == 0) return@List latestComicResponse
            val id = latestComicResponse.num - it
            latestComicResponse.copy(num = id, title = "title$id", img = "url$id")
        }

        val initialResult = pagingSource.load(Refresh(key = null, loadSize = 2, placeholdersEnabled = false))
        check(initialResult is PagingSource.LoadResult.Page)
        val appendResult =
            pagingSource.load(Append(key = initialResult.nextKey!!, loadSize = 2, placeholdersEnabled = false))
        check(appendResult is PagingSource.LoadResult.Page)
        initialResult.prevKey.shouldBeNull()
        initialResult.nextKey shouldBe 2799
        appendResult.prevKey shouldBe 2800
        appendResult.nextKey shouldBe 2797
    }
}

/**
 * A Fake implementation of the [XKCDRemoteDataSource] that exposes additional data setters
 * and just returns dummy data.
 */
private class FakeXKCDRemoteDataSource(private val latestComicResponse: ComicResponse) : XKCDRemoteDataSource {

    var comics: List<ComicResponse> = emptyList()
    var exception: Exception? = null

    override suspend fun getLatest(): ComicResponse {
        exception?.let { throw it }
        return latestComicResponse
    }

    override suspend fun getComic(comicId: Int): ComicResponse {
        exception?.let { throw it }
        return comics.find { it.num == comicId } ?: throw Exception("No comic found for ID: $comicId")
    }
}
