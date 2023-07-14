package com.cammace.comicscroll.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.cammace.comicscroll.data.ComicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * [ViewModel] for the Comic Feed screen.
 */
@HiltViewModel
class ComicFeedViewModel @Inject constructor(comicRepository: ComicRepository) : ViewModel() {

    val comics = comicRepository.getComicsStream()
        .cachedIn(viewModelScope)
}
