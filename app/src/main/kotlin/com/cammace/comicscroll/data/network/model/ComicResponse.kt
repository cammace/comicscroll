package com.cammace.comicscroll.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A model that allows the XKCD JSON network response to deserialize into.
 */
@Serializable
data class ComicResponse(
    val month: String,
    val num: Int,
    val link: String,
    val year: String,
    val news: String,
    @SerialName("safe_title") val safeTitle: String,
    val transcript: String,
    val alt: String,
    val img: String,
    val title: String,
    val day: String
)
