package com.example.waygofrontend.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.R
import com.example.waygofrontend.data.WaygoData
import com.example.waygofrontend.databinding.FragmentNotificationsBinding
import com.example.waygofrontend.databinding.ItemNoticeBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        val container = binding.noticeList
        WaygoData.notices.forEach { notice ->
            val item = ItemNoticeBinding.inflate(layoutInflater, container, false)
            item.tvTitle.text = notice.title
            item.tvNote.text = notice.note
            item.tvTime.text = getString(R.string.notice_hours_ago, notice.hoursAgo)
            item.icon.setImageResource(notice.iconRes)
            item.icon.setColorFilter(ContextCompat.getColor(requireContext(), notice.tintRes))
            ViewCompat.setBackgroundTintList(
                item.iconContainer,
                ContextCompat.getColorStateList(requireContext(), notice.backgroundRes)
            )
            container.addView(item.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
