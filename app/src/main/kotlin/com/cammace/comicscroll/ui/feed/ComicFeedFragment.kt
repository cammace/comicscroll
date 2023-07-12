package com.cammace.comicscroll.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cammace.comicscroll.databinding.FragmentComicFeedBinding
import dagger.hilt.android.AndroidEntryPoint

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

        binding.loadingIndicator.isVisible = true

        // TODO handle loading data into view here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
