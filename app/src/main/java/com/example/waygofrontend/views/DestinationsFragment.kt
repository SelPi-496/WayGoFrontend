package com.example.waygofrontend.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.R
import com.example.waygofrontend.data.WaygoData
import com.example.waygofrontend.databinding.FragmentDestinationsBinding
import com.example.waygofrontend.databinding.ItemDestinationLargeBinding

class DestinationsFragment : Fragment() {

    private var _binding: FragmentDestinationsBinding? = null
    private val binding get() = _binding!!

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
        render()
    }

    private fun render() {
        val container = binding.destinationList
        container.removeAllViews()
        WaygoData.destinations.forEach { destination ->
            val item = ItemDestinationLargeBinding.inflate(layoutInflater, container, false)
            item.photo.setBackgroundResource(destination.imageRes)
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
