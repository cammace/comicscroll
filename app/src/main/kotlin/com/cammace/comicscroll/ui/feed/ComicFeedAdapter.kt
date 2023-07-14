package com.cammace.comicscroll.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cammace.comicscroll.R
import com.cammace.comicscroll.data.model.Comic
import com.cammace.comicscroll.databinding.ItemComicBinding
import com.cammace.comicscroll.databinding.ItemLoadStateBinding

/**
 * Adapter that receives paged data to populate the [Comic] list items inside a [ComicViewHolder].
 */
class ComicFeedAdapter : PagingDataAdapter<Comic, ComicFeedAdapter.ComicViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder = ComicViewHolder.create(parent)

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Comic>() {
            override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean =
                oldItem.num == newItem.num

            override fun areContentsTheSame(oldItem: Comic, newItem: Comic): Boolean =
                oldItem == newItem
        }
    }

    /**
     * Binds [Comic] data to the view.
     */
    class ComicViewHolder(private val binding: ItemComicBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comic: Comic) {
            binding.apply {
                comicTitle.text = comic.title
                comicImage.load(comic.img)
                comicImage.contentDescription = comic.alt
            }
        }

        companion object {
            /**
             * Factory method to create a [ComicViewHolder] instance.
             */
            fun create(parent: ViewGroup): ComicViewHolder {
                val binding = ItemComicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ComicViewHolder(binding)
            }
        }
    }
}

