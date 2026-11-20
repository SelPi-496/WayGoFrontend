package com.example.waygofrontend.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.R
import com.example.waygofrontend.data.WaygoData
import com.example.waygofrontend.databinding.FragmentProfileDetailBinding
import com.example.waygofrontend.databinding.ItemSavedPlaceBinding

class ProfileDetailFragment : Fragment() {

    private var _binding: FragmentProfileDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString(ARG_TITLE) ?: getString(R.string.saved_places)
        binding.tvTitle.text = title
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        when (title) {
            getString(R.string.saved_places) -> showSaved()
            getString(R.string.account_settings) -> showSettings()
            else -> showState(title)
        }
    }

    private fun showSaved() {
        binding.savedContainer.visibility = View.VISIBLE
        val container = binding.savedContainer
        WaygoData.destinations.forEach { destination ->
            val item = ItemSavedPlaceBinding.inflate(layoutInflater, container, false)
            item.photo.setBackgroundResource(destination.imageRes)
            item.tvName.text = destination.name
            item.tvPlace.text = destination.place
            container.addView(item.root)
        }
    }

    private fun showSettings() {
        binding.settingsContainer.visibility = View.VISIBLE
        binding.btnSave.setOnClickListener {
            Toast.makeText(requireContext(), R.string.settings_saved, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showState(title: String) {
        binding.stateContainer.visibility = View.VISIBLE
        val isLogout = title == getString(R.string.logout)
        binding.imgState.setImageResource(
            if (isLogout) R.drawable.ic_logout else R.drawable.ic_help
        )
        if (isLogout) {
            binding.tvStateTitle.setText(R.string.logout_question)
            binding.tvStateNote.setText(R.string.logout_note)
            binding.btnStateAction.setText(R.string.confirm_logout)
        } else {
            binding.tvStateTitle.setText(R.string.help_title)
            binding.tvStateNote.setText(R.string.help_note)
            binding.btnStateAction.setText(R.string.start_chat)
        }
        binding.btnStateAction.setOnClickListener {
            Toast.makeText(requireContext(), R.string.coming_soon, Toast.LENGTH_SHORT).show()
            if (isLogout) findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_TITLE = "profile_page_title"
    }
}
