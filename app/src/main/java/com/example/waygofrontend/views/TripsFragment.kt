package com.example.waygofrontend.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.MainActivity
import com.example.waygofrontend.R
import com.example.waygofrontend.data.WaygoData
import com.example.waygofrontend.databinding.FragmentTripsBinding
import com.example.waygofrontend.databinding.ItemTripBinding

class TripsFragment : Fragment() {

    private var _binding: FragmentTripsBinding? = null
    private val binding get() = _binding!!

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

        val container = binding.tripList
        val selectedId = (requireActivity() as MainActivity).selectedDestinationId()
        WaygoData.plannedTrips(selectedId).forEach { trip ->
            val item = ItemTripBinding.inflate(layoutInflater, container, false)
            item.photo.setBackgroundResource(trip.destination.imageRes)
            item.tvName.text = trip.destination.name
            item.tvStatus.text = trip.status
            item.tvDates.text = "${trip.range} · ${trip.days} ngày"
            item.root.setOnClickListener {
                val args = Bundle().apply {
                    putString(ItineraryDetailFragment.ARG_DESTINATION, trip.destination.id)
                }
                findNavController().navigate(R.id.action_trips_to_itinerary, args)
            }
            container.addView(item.root)
        }

        binding.btnAddTrip.setOnClickListener {
            findNavController().navigate(R.id.action_trips_to_planner)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
