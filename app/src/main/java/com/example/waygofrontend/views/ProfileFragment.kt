package com.example.waygofrontend.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.MainActivity
import com.example.waygofrontend.R
import com.example.waygofrontend.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnToggleTheme.setOnClickListener {
            (requireActivity() as MainActivity).toggleTheme()
        }

        binding.rowSaved.setOnClickListener { openPage(getString(R.string.saved_places)) }
        binding.rowSettings.setOnClickListener { openPage(getString(R.string.account_settings)) }
        binding.rowHelp.setOnClickListener { openPage(getString(R.string.help_support)) }
        binding.rowLogout.setOnClickListener { openPage(getString(R.string.logout)) }
    }

    override fun onResume() {
        super.onResume()
        val isNight = (requireActivity() as MainActivity).isNightMode()
        binding.btnToggleTheme.setImageResource(if (isNight) R.drawable.ic_sun else R.drawable.ic_moon)
    }

    private fun openPage(title: String) {
        val args = Bundle().apply { putString(ProfileDetailFragment.ARG_TITLE, title) }
        findNavController().navigate(R.id.action_profile_to_detail, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
