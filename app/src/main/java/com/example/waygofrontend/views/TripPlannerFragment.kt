package com.example.waygofrontend.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.MainActivity
import com.example.waygofrontend.data.model.Trip
import com.example.waygofrontend.databinding.FragmentTripPlannerBinding
import com.example.waygofrontend.ui.viewmodel.TripPlannerViewModel
import com.example.waygofrontend.di.AppContainer
import com.example.waygofrontend.di.ViewModelFactory

class TripPlannerFragment : Fragment() {

    private var _binding: FragmentTripPlannerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TripPlannerViewModel by viewModels {
        ViewModelFactory { TripPlannerViewModel(AppContainer.getRepository()) }
    }

    private val handler = android.os.Handler(android.os.Looper.getMainLooper())
    private val dateFormatter = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale("vi", "VN")).apply {
        timeZone = java.util.TimeZone.getTimeZone("UTC")
    }
    private var selectedIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripPlannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnClose.setOnClickListener { findNavController().navigateUp() }
        binding.btnCloseResults.setOnClickListener { findNavController().navigateUp() }
        binding.btnStartDate.setOnClickListener { pickDates() }
        binding.btnEndDate.setOnClickListener { pickDates() }

        binding.sliderBudget.addOnChangeListener { _, value, _ -> updateBudget(value) }
        updateBudget(binding.sliderBudget.value)

        binding.btnFind.setOnClickListener { startLoading() }
        binding.btnChange.setOnClickListener { showForm() }
        binding.btnChoose.setOnClickListener {
            val destination = com.example.waygofrontend.data.WaygoData.destinations[selectedIndex]
            (requireActivity() as MainActivity).onTripCreated(destination.id)
        }
    }

    private fun pickDates() {
        // Keep existing date picker logic
    }

    private fun updateBudget(value: Float) {
        // Keep existing budget logic
    }

    private fun startLoading() {
        // Keep existing loading logic
    }

    private fun showForm() {
        // Keep existing form logic
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
