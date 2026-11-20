package com.example.waygofrontend.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.R
import com.example.waygofrontend.data.WaygoData
import com.example.waygofrontend.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnPin1.setOnClickListener { select(0) }
        binding.btnPin2.setOnClickListener { select(1) }
        binding.btnPin3.setOnClickListener { select(2) }
        select(0)
    }

    private fun select(position: Int) {
        val pins = listOf(binding.btnPin1, binding.btnPin2, binding.btnPin3)
        pins.forEachIndexed { index, pin ->
            pin.setBackgroundResource(
                if (index == position) R.drawable.bg_circle_coral_solid else R.drawable.bg_circle_blue
            )
        }
        val destination = WaygoData.destinations[position]
        binding.imgMapPhoto.setBackgroundResource(destination.imageRes)
        binding.tvMapName.text = destination.name
        binding.tvMapPlace.text = destination.place
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
