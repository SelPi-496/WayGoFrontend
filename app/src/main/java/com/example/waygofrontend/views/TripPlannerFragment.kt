package com.example.waygofrontend.views

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.MainActivity
import com.example.waygofrontend.R
import com.example.waygofrontend.data.WaygoData
import com.example.waygofrontend.databinding.FragmentTripPlannerBinding
import com.example.waygofrontend.databinding.ItemAdvisorDestinationBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class TripPlannerFragment : Fragment() {

    private var _binding: FragmentTripPlannerBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale("vi", "VN")).apply {
        timeZone = TimeZone.getTimeZone("UTC")
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
            val destination = WaygoData.destinations[selectedIndex]
            (requireActivity() as MainActivity).onTripCreated(destination.id)
        }

        renderResults()
    }

    private fun updateBudget(value: Float) {
        val label = when {
            value <= 3f -> "1–3 triệu / người"
            value <= 6f -> "3–6 triệu / người"
            value <= 8f -> "6–9 triệu / người"
            else -> "Trên 9 triệu / người"
        }
        binding.tvBudget.text = label
    }

    private fun pickDates() {
        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText(R.string.advisor_when)
            .build()
        picker.addOnPositiveButtonClickListener { selection ->
            val start = selection.first ?: return@addOnPositiveButtonClickListener
            val end = selection.second ?: return@addOnPositiveButtonClickListener
            binding.tvStartDate.text = dateFormatter.format(Date(start))
            binding.tvEndDate.text = dateFormatter.format(Date(end))
        }
        picker.show(childFragmentManager, "date_range")
    }

    private fun startLoading() {
        binding.formContainer.visibility = View.GONE
        binding.resultsContainer.visibility = View.GONE
        binding.loadingContainer.visibility = View.VISIBLE
        handler.postDelayed({
            if (_binding != null) showResults()
        }, 1500)
    }

    private fun showForm() {
        binding.formContainer.visibility = View.VISIBLE
        binding.resultsContainer.visibility = View.GONE
        binding.loadingContainer.visibility = View.GONE
    }

    private fun showResults() {
        binding.formContainer.visibility = View.GONE
        binding.loadingContainer.visibility = View.GONE
        binding.resultsContainer.visibility = View.VISIBLE
        renderResults()
        binding.root.scrollTo(0, 0)
    }

    private fun renderResults() {
        val container = binding.resultList
        container.removeAllViews()
        WaygoData.destinations.forEachIndexed { index, destination ->
            val item = ItemAdvisorDestinationBinding.inflate(layoutInflater, container, false)
            item.photo.setBackgroundResource(destination.imageRes)
            item.tvName.text = destination.name
            item.tvMatch.text = getString(R.string.match_percent, destination.match)
            item.tvReason.text = destination.reason
            item.tvPrice.text = getString(R.string.price_from_label, destination.price)
            item.root.setOnClickListener { select(index) }
            container.addView(item.root)
        }
        select(selectedIndex)
    }

    private fun select(index: Int) {
        selectedIndex = index
        val selectedColor = ContextCompat.getColor(requireContext(), R.color.waygo_blue)
        val outlineColor = ContextCompat.getColor(requireContext(), R.color.waygo_outline)
        val transparent = ContextCompat.getColor(requireContext(), R.color.transparent)
        val stroke = resources.getDimensionPixelSize(R.dimen.card_stroke) * 2

        for (i in 0 until binding.resultList.childCount) {
            val child = binding.resultList.getChildAt(i)
            val card = child as? com.google.android.material.card.MaterialCardView ?: continue
            val chosen = i == index
            card.strokeColor = if (chosen) selectedColor else outlineColor
            card.strokeWidth = if (chosen) stroke else resources.getDimensionPixelSize(R.dimen.card_stroke)
            val check = child.findViewById<View>(R.id.check)
            check.visibility = if (chosen) View.VISIBLE else View.GONE
            val checkContainer = child.findViewById<View>(R.id.check)
            checkContainer.backgroundTintList =
                ColorStateList.valueOf(if (chosen) selectedColor else transparent)
            val checkIcon = child.findViewById<android.widget.ImageView>(R.id.checkIcon)
            checkIcon.imageTintList = ColorStateList.valueOf(
                if (chosen) ContextCompat.getColor(requireContext(), R.color.white) else transparent
            )
        }

        val destination = WaygoData.destinations[index]
        binding.tvReason.text = getString(R.string.ai_reason_title, destination.name) + " " +
            getString(R.string.ai_reason_note)
        binding.btnChoose.text = getString(R.string.choose_and_plan, destination.name)
    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroyView()
        _binding = null
    }
}
