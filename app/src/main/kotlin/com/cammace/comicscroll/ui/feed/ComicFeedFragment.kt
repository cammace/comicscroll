package com.cammace.comicscroll.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import com.cammace.comicscroll.data.model.Comic
import com.cammace.comicscroll.databinding.FragmentComicFeedBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Displays the last 100 comics in a scrollable view.
 */
@AndroidEntryPoint
class ComicFeedFragment : Fragment() {

    private var _binding: FragmentComicFeedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ComicFeedViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentComicFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val comicFeedAdapter = ComicFeedAdapter()
        binding.recyclerView.apply {
            addItemDecoration(MaterialDividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            // Sets the recycler view adapter to use the ConcatAdapter provided from withLoadStateFooter.
            adapter = comicFeedAdapter.withLoadStateFooter(LoadingStateAdapter(comicFeedAdapter::retry))
        }
        binding.bindList(comicFeedAdapter, viewModel.comics)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Sets up the view for displaying comics.
     */
    private fun FragmentComicFeedBinding.bindList(
        comicFeedAdapter: ComicFeedAdapter,
        comicFeedData: Flow<PagingData<Comic>>
    ) {
        retryButton.setOnClickListener { comicFeedAdapter.retry() }

        // observe the [PagingData] stream
        viewLifecycleOwner.lifecycleScope.launch {
            comicFeedData.collectLatest(comicFeedAdapter::submitData)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            comicFeedAdapter.loadStateFlow.collect { loadState ->
                // Show loading spinner during initial load or refresh.
                loadingIndicator.isVisible = loadState.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                if (loadState.refresh is LoadState.Error) {
                    errorMessage.text = (loadState.refresh as LoadState.Error).error.localizedMessage
                }
                retryButton.isVisible = loadState.refresh is LoadState.Error && comicFeedAdapter.itemCount == 0
                errorMessage.isVisible = loadState.refresh is LoadState.Error && comicFeedAdapter.itemCount == 0
            }
        }
    }
}
