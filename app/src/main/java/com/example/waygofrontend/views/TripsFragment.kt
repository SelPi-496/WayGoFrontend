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
import com.example.waygofrontend.MainActivity
import com.example.waygofrontend.R
import com.example.waygofrontend.data.model.Trip
import com.example.waygofrontend.databinding.FragmentTripsBinding
import com.example.waygofrontend.databinding.ItemTripBinding
import com.example.waygofrontend.ui.viewmodel.TripsViewModel
import com.example.waygofrontend.di.AppContainer
import com.example.waygofrontend.di.ViewModelFactory
import kotlinx.coroutines.launch

class TripsFragment : Fragment() {

    private var _binding: FragmentTripsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TripsViewModel by viewModels {
        ViewModelFactory { TripsViewModel(AppContainer.getRepository()) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedId = (requireActivity() as MainActivity).selectedDestinationId()
        observeViewModel()
        viewModel.loadTrips(selectedId)

        binding.btnAddTrip.setOnClickListener {
            findNavController().navigate(R.id.action_trips_to_planner)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                if (state.isLoading) {
                    // Could show loading
                }
                if (state.error != null) {
                    // Could show error
                }
                renderTrips(state.trips)
            }
        }
    }

    private fun renderTrips(trips: List<Trip>) {
        val container = binding.tripList
        container.removeAllViews()
        val inflater = layoutInflater
        trips.forEach { trip ->
            val item = ItemTripBinding.inflate(inflater, container, false)
            val resId = com.example.waygofrontend.R.drawable.bg_photo_beach
            item.photo.setBackgroundResource(resId)
            item.tvName.text = trip.destinationId // Will be replaced with destination name when API is ready
            item.tvStatus.text = trip.status
            item.tvDates.text = "${trip.range} · ${trip.days} ngày"
            item.root.setOnClickListener {
                val args = Bundle().apply {
                    putString(ItineraryDetailFragment.ARG_DESTINATION, trip.destinationId)
                }
                findNavController().navigate(R.id.action_trips_to_itinerary, args)
            }
            container.addView(item.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
