package com.example.waygofrontend.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.R
import com.example.waygofrontend.data.model.Destination
import com.example.waygofrontend.databinding.FragmentDestinationDetailBinding
import com.example.waygofrontend.ui.viewmodel.DestinationDetailViewModel
import com.example.waygofrontend.di.AppContainer
import com.example.waygofrontend.di.ViewModelFactory
import kotlinx.coroutines.launch

class DestinationDetailFragment : Fragment() {

    private var _binding: FragmentDestinationDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DestinationDetailViewModel by viewModels {
        ViewModelFactory { DestinationDetailViewModel(AppContainer.getRepository()) }
    }

    private var saved = false
    private var added = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDestinationDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val destinationId = arguments?.getString(ExploreFragment.ARG_DESTINATION)
        if (destinationId != null) {
            viewModel.loadDestination(destinationId)
            observeViewModel(destinationId)
        }

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnShare.setOnClickListener {
            Toast.makeText(requireContext(), R.string.coming_soon, Toast.LENGTH_SHORT).show()
        }
        binding.tvOpenMap.setOnClickListener { findNavController().navigate(R.id.action_detail_to_map) }

        binding.btnFavorite.setOnClickListener { toggleSaved() }
        binding.btnSaveBottom.setOnClickListener { toggleSaved() }
        binding.btnAddItinerary.setOnClickListener { toggleAdded() }
    }

    private fun observeViewModel(destinationId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                if (state.isLoading) {
                    // Could show loading
                }
                if (state.error != null) {
                    // Could show error
                }
                state.destination?.let { destination ->
                    bindDestination(destination)
                }
            }
        }
    }

    private fun bindDestination(destination: Destination) {
        binding.photo.setBackgroundResource(com.example.waygofrontend.R.drawable.bg_photo_beach)
        binding.tvName.text = destination.name
        binding.tvLocation.text = getString(R.string.destination_location, destination.place)
    }

    private fun toggleSaved() {
        saved = !saved
        val icon = if (saved) R.drawable.ic_heart_filled else R.drawable.ic_heart
        val tint = if (saved) R.color.waygo_coral else R.color.waygo_text_primary
        binding.btnFavorite.setImageResource(icon)
        binding.btnFavorite.imageTintList = ContextCompat.getColorStateList(requireContext(), tint)
        binding.btnSaveBottom.setImageResource(icon)
        binding.btnSaveBottom.imageTintList = ContextCompat.getColorStateList(
            requireContext(),
            if (saved) R.color.waygo_coral else R.color.waygo_text_secondary
        )
        binding.btnSaveBottom.backgroundTintList = ContextCompat.getColorStateList(
            requireContext(),
            if (saved) R.color.waygo_coral_soft else R.color.waygo_blue_soft
        )
    }

    private fun toggleAdded() {
        added = !added
        if (added) {
            binding.btnAddItinerary.setText(R.string.added_to_itinerary)
            binding.btnAddItinerary.backgroundTintList = ContextCompat.getColorStateList(
                requireContext(), R.color.waygo_success
            )
            binding.btnAddItinerary.setIconResource(R.drawable.ic_check)
        } else {
            binding.btnAddItinerary.setText(R.string.add_to_itinerary)
            binding.btnAddItinerary.backgroundTintList = ContextCompat.getColorStateList(
                requireContext(), R.color.waygo_blue
            )
            binding.btnAddItinerary.setIconResource(R.drawable.ic_plus)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
