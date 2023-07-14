package com.cammace.comicscroll.data.model

import com.cammace.comicscroll.data.network.model.ComicResponse

/**
 * External data layer representation of a XKCD comic.
 */
data class Comic(
    val num: Int,
    val alt: String,
    val img: String,
    val title: String
)

fun ComicResponse.asExternalModel() = Comic(
    num = num,
    alt = alt,
    img = img,
    title = title
)
