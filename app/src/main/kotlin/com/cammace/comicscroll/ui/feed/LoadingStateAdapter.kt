package com.cammace.comicscroll.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cammace.comicscroll.databinding.ItemLoadStateBinding

/**
 * Allows for displaying loading state directly in the displayed list of paged data.
 */
class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder =
        LoadStateViewHolder.create(parent, retry)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) = holder.bind(loadState)

    /**
     * Binds [LoadState] data to the view.
     */
    class LoadStateViewHolder(private val binding: ItemLoadStateBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMessage.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMessage.isVisible = loadState is LoadState.Error
        }

        companion object {
            /**
             * Factory method to create a [LoadStateViewHolder] instance.
             */
            fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
                val binding = ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return LoadStateViewHolder(binding, retry)
            }
        }
    }
}

