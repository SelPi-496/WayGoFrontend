package com.example.waygofrontend.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.R
import com.example.waygofrontend.data.model.Destination
import com.example.waygofrontend.databinding.FragmentDestinationsBinding
import com.example.waygofrontend.databinding.ItemDestinationLargeBinding
import com.example.waygofrontend.ui.viewmodel.DestinationsViewModel
import com.example.waygofrontend.di.AppContainer
import com.example.waygofrontend.di.ViewModelFactory
import kotlinx.coroutines.launch

class DestinationsFragment : Fragment() {

    private var _binding: FragmentDestinationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DestinationsViewModel by viewModels {
        ViewModelFactory { DestinationsViewModel(AppContainer.getRepository()) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDestinationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                if (state.isLoading) {
                    // Could show loading indicator
                }
                if (state.error != null) {
                    // Could show error
                }
                render(state.destinations)
            }
        }
        viewModel.loadDestinations()
    }

    private fun render(destinations: List<Destination>) {
        val container = binding.destinationList
        container.removeAllViews()
        val inflater = layoutInflater
        destinations.forEach { destination ->
            val item = ItemDestinationLargeBinding.inflate(inflater, container, false)
            val resId = com.example.waygofrontend.R.drawable.bg_photo_beach
            item.photo.setBackgroundResource(resId)
            item.tvName.text = destination.name
            item.tvPlace.text = destination.place
            item.tvTag.text = destination.topLabel
            item.tvRating.text = "\u2605 " + destination.rating
            item.root.setOnClickListener {
                val args = Bundle().apply { putString(ExploreFragment.ARG_DESTINATION, destination.id) }
                findNavController().navigate(R.id.action_destinations_to_detail, args)
            }
            container.addView(item.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
