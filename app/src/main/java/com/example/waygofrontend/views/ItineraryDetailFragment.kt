package com.example.waygofrontend.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.MainActivity
import com.example.waygofrontend.R
import com.example.waygofrontend.data.WaygoData
import com.example.waygofrontend.databinding.FragmentItineraryDetailBinding
import com.example.waygofrontend.databinding.ItemActivityBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ItineraryDetailFragment : Fragment() {

    private var _binding: FragmentItineraryDetailBinding? = null
    private val binding get() = _binding!!

    private var trip: WaygoData.Trip = WaygoData.trips[0]
    private var selectedDay = 1
    private val activitiesByDay = mutableMapOf<Int, MutableList<WaygoData.Activity>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItineraryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedId = (requireActivity() as MainActivity).selectedDestinationId()
        trip = WaygoData.tripFor(arguments?.getString(ARG_DESTINATION), selectedId)
        for (day in 1..trip.days) {
            activitiesByDay[day] = WaygoData.activities(day).toMutableList()
        }

        binding.tvRange.text = trip.range.uppercase()
        binding.tvTitle.text = getString(R.string.itinerary_title, trip.destination.name)
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        buildDayChips()
        selectDay(1)

        binding.fabAddActivity.setOnClickListener { openAddActivity() }
        binding.btnEmptyAdd.setOnClickListener { openAddActivity() }

        parentFragmentManager.setFragmentResultListener(
            AddActivityFragment.REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val activity = WaygoData.Activity(
                id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                time = bundle.getString(AddActivityFragment.KEY_TIME) ?: "21:00",
                title = bundle.getString(AddActivityFragment.KEY_TITLE) ?: "",
                place = bundle.getString(AddActivityFragment.KEY_PLACE) ?: "",
                cost = bundle.getString(AddActivityFragment.KEY_COST) ?: "Miễn phí",
                dotColor = R.color.waygo_success
            )
            val targetDay = bundle.getInt(AddActivityFragment.KEY_DAY, selectedDay)
            activitiesByDay.getOrPut(targetDay) { mutableListOf() }.add(activity)
            if (targetDay != selectedDay) selectDay(targetDay) else renderDay()
            Toast.makeText(requireContext(), R.string.activity_added_demo, Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildDayChips() {
        val group: ChipGroup = binding.dayChips
        group.removeAllViews()
        for (day in 1..trip.days) {
            val chip = Chip(requireContext()).apply {
                text = getString(R.string.day_n, day)
                isCheckable = true
                isChecked = day == selectedDay
                chipMinHeight = resources.getDimension(R.dimen.touch_target) * 0.8f
                setTextColor(
                    ContextCompat.getColorStateList(requireContext(), R.color.chip_text_color)
                )
                chipBackgroundColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.chip_background_color)
                setChipStrokeColorResource(R.color.waygo_outline)
                chipStrokeWidth = resources.getDimension(R.dimen.card_stroke)
                chipCornerRadius = resources.getDimension(R.dimen.radius_sm)
                setOnClickListener { selectDay(day) }
            }
            group.addView(chip)
        }
    }

    private fun selectDay(day: Int) {
        selectedDay = day
        for (index in 0 until binding.dayChips.childCount) {
            val chip = binding.dayChips.getChildAt(index) as? Chip ?: continue
            chip.isChecked = index + 1 == day
        }
        renderDay()
    }

    private fun renderDay() {
        val activities = activitiesByDay[selectedDay].orEmpty()
        binding.tvDayTitle.text = getString(R.string.day_n, selectedDay)
        binding.tvDayCount.text = getString(R.string.activities_count, activities.size)
        binding.emptyState.visibility = if (activities.isEmpty()) View.VISIBLE else View.GONE

        val container = binding.timelineContainer
        container.removeAllViews()
        activities.forEach { activity ->
            val item = ItemActivityBinding.inflate(layoutInflater, container, false)
            item.tvTime.text = activity.time
            item.tvTitle.text = activity.title
            item.tvPlace.text = activity.place
            item.tvCost.text = activity.cost
            ViewCompat.setBackgroundTintList(
                item.dot,
                ContextCompat.getColorStateList(requireContext(), activity.dotColor)
            )
            item.btnMore.setOnClickListener {
                Toast.makeText(requireContext(), R.string.coming_soon, Toast.LENGTH_SHORT).show()
            }
            container.addView(item.root)
        }
    }

    private fun openAddActivity() {
        val args = Bundle().apply { putInt(AddActivityFragment.ARG_DAY, selectedDay) }
        findNavController().navigate(R.id.action_itinerary_to_addActivity, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_DESTINATION = "destination_id"
    }
}
