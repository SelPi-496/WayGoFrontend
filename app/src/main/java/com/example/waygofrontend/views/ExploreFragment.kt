package com.example.waygofrontend.views

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.MainActivity
import com.example.waygofrontend.R
import com.example.waygofrontend.data.WaygoData
import com.example.waygofrontend.databinding.FragmentExploreBinding
import com.example.waygofrontend.databinding.ItemCategoryBinding
import com.example.waygofrontend.databinding.ItemDestinationCardBinding

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        highlightTitle()
        renderCategories()
        renderDestinations(WaygoData.destinations)
        setupActions()
        setupSearch()
    }

    override fun onResume() {
        super.onResume()
        updateThemeIcon()
    }

    private fun highlightTitle() {
        val word = getString(R.string.explore_title_highlight)
        val text = getString(R.string.explore_title)
        val start = text.indexOf(word)
        if (start < 0) {
            binding.tvExploreTitle.text = text
            return
        }

        val span = SpannableString(text)
        span.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.waygo_blue)),
            start,
            start + word.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        span.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            start + word.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvExploreTitle.text = span
    }

    private fun renderCategories() {
        val container = binding.categoryContainer
        container.removeAllViews()
        val inflater = layoutInflater
        WaygoData.categories.forEach { category ->
            val item = ItemCategoryBinding.inflate(inflater, container, false)
            item.label.text = category.label
            item.icon.setImageResource(category.iconRes)
            item.icon.setColorFilter(ContextCompat.getColor(requireContext(), category.tintRes))
            ViewCompat.setBackgroundTintList(
                item.iconContainer,
                ContextCompat.getColorStateList(requireContext(), category.backgroundRes)
            )
            item.root.setOnClickListener {
                val args = Bundle().apply { putString(ARG_CATEGORY, category.label) }
                findNavController().navigate(R.id.action_explore_to_service, args)
            }
            container.addView(item.root)
        }
    }

    private fun renderDestinations(items: List<WaygoData.Destination>) {
        val container = binding.destinationContainer
        container.removeAllViews()
        val inflater = layoutInflater
        items.forEach { destination ->
            val item = ItemDestinationCardBinding.inflate(inflater, container, false)
            item.photo.setBackgroundResource(destination.imageRes)
            item.tvName.text = destination.name
            item.tvPlace.text = destination.place
            item.tvPriceLabel.text = getString(R.string.price_from) + " " + destination.price
            item.tvRating.text = "\u2605 " + destination.rating
            setupFavorite(item)
            item.root.setOnClickListener { openDetail(destination) }
            container.addView(item.root)
        }
    }

    private fun setupFavorite(item: ItemDestinationCardBinding) {
        var filled = false
        item.btnFavorite.setOnClickListener {
            filled = !filled
            item.btnFavorite.setImageResource(
                if (filled) R.drawable.ic_heart_filled else R.drawable.ic_heart
            )
            item.btnFavorite.imageTintList = ContextCompat.getColorStateList(
                requireContext(),
                if (filled) R.color.waygo_coral else R.color.waygo_text_primary
            )
        }
    }

    private fun openDetail(destination: WaygoData.Destination) {
        val args = Bundle().apply { putString(ARG_DESTINATION, destination.id) }
        findNavController().navigate(R.id.action_explore_to_detail, args)
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.trim().orEmpty()
                val results = WaygoData.destinations.filter {
                    it.name.lowercase().contains(query.lowercase())
                }
                renderDestinations(results)
                if (query.isEmpty()) {
                    binding.tvTrendingTitle.setText(R.string.trending_title)
                    binding.tvTrendingSubtitle.setText(R.string.trending_subtitle)
                } else {
                    binding.tvTrendingTitle.setText(R.string.search_results_title)
                    binding.tvTrendingSubtitle.text =
                        getString(R.string.destinations_found, results.size)
                }
            }
        })
    }

    private fun setupActions() {
        binding.bannerAi.setOnClickListener {
            findNavController().navigate(R.id.action_explore_to_planner)
        }

        binding.btnToggleTheme.setOnClickListener {
            (requireActivity() as MainActivity).toggleTheme()
        }

        binding.btnNotifications.setOnClickListener {
            findNavController().navigate(R.id.action_explore_to_notifications)
        }

        binding.btnMap.setOnClickListener {
            findNavController().navigate(R.id.action_explore_to_map)
        }

        binding.tvSeeAll.setOnClickListener {
            findNavController().navigate(R.id.action_explore_to_destinations)
        }
    }

    private fun updateThemeIcon() {
        val isNight = (requireActivity() as MainActivity).isNightMode()
        binding.btnToggleTheme.setImageResource(if (isNight) R.drawable.ic_sun else R.drawable.ic_moon)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_DESTINATION = "destination_id"
        const val ARG_CATEGORY = "category_label"
    }
}
