package com.example.waygofrontend.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.R
import com.example.waygofrontend.databinding.FragmentAddActivityBinding

class AddActivityFragment : Fragment() {

    private var _binding: FragmentAddActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.etName.setText(getString(R.string.activity_name_hint))
        binding.etTime.setText(getString(R.string.activity_time_hint))
        binding.etCost.setText(getString(R.string.activity_cost_hint))
        binding.etPlace.setText(getString(R.string.activity_location_hint))

        binding.btnAdd.setOnClickListener {
            val title = binding.etName.text?.toString()?.trim().orEmpty()
            if (title.isEmpty()) {
                binding.etName.error = getString(R.string.activity_name_required)
                return@setOnClickListener
            }
            parentFragmentManager.setFragmentResult(
                REQUEST_KEY,
                bundleOf(
                    KEY_DAY to (arguments?.getInt(ARG_DAY) ?: 1),
                    KEY_TITLE to title,
                    KEY_TIME to binding.etTime.text?.toString()?.trim().orEmpty(),
                    KEY_COST to binding.etCost.text?.toString()?.trim().orEmpty(),
                    KEY_PLACE to binding.etPlace.text?.toString()?.trim().orEmpty()
                )
            )
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val REQUEST_KEY = "add_activity_request"
        const val KEY_TITLE = "activity_title"
        const val KEY_TIME = "activity_time"
        const val KEY_COST = "activity_cost"
        const val KEY_PLACE = "activity_place"
        const val KEY_DAY = "activity_day"
        const val ARG_DAY = "day"
    }
}
