package com.example.waygofrontend.views

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.R
import com.example.waygofrontend.data.WaygoData
import com.example.waygofrontend.databinding.FragmentBookingListBinding
import com.example.waygofrontend.databinding.ItemHotelBinding
import com.google.android.material.chip.ChipGroup

class BookingListFragment : Fragment() {

    private var _binding: FragmentBookingListBinding? = null
    private val binding get() = _binding!!

    private var destination: WaygoData.Destination = WaygoData.destinations[0]
    private var hotels: List<WaygoData.Hotel> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        destination = WaygoData.destination(arguments?.getString(ExploreFragment.ARG_DESTINATION))
        hotels = WaygoData.hotels(destination.id)

        binding.tvEyebrow.text = getString(R.string.booking_eyebrow, destination.name)
        binding.etHotelSearch.hint = getString(R.string.search_hotel_hint, destination.name)
        binding.tvMatchCount.text = getString(R.string.best_match_count, hotels.size)

        binding.tvMap.setOnClickListener {
            findNavController().navigate(R.id.action_booking_to_map)
        }
        binding.chipGroupFilters.setOnCheckedStateChangeListener { group, _ ->
            val chip = group.findViewById<com.google.android.material.chip.Chip>(group.checkedChipId)
            val filter = chip?.text?.toString()
            render(if (filter == getString(R.string.filter_best_price)) hotels.sortedBy { it.price } else hotels)
        }
        render(hotels)
    }

    private fun render(items: List<WaygoData.Hotel>) {
        binding.tvMatchCount.text = getString(R.string.best_match_count, items.size)
        val container = binding.hotelList
        container.removeAllViews()
        items.forEach { hotel ->
            val item = ItemHotelBinding.inflate(layoutInflater, container, false)
            item.photo.setBackgroundResource(hotel.imageRes)
            item.tvBadge.text = hotel.badge
            item.tvName.text = hotel.name
            item.tvArea.text = hotel.area
            item.tvRating.text = "\u2605 " + hotel.rating
            item.tvAmenity.text = hotel.amenity
            item.tvReviews.text = getString(R.string.reviews_count, hotel.reviews)
            item.tvPrice.text = WaygoData.formatPrice(hotel.price)
            item.tvOldPrice.text = WaygoData.formatPrice(hotel.oldPrice)
            item.tvOldPrice.paintFlags = item.tvOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            var favorite = false
            item.btnFavorite.setOnClickListener {
                favorite = !favorite
                item.btnFavorite.setImageResource(
                    if (favorite) R.drawable.ic_heart_filled else R.drawable.ic_heart
                )
                item.btnFavorite.imageTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    if (favorite) R.color.waygo_coral else R.color.waygo_text_secondary
                )
            }
            item.root.setOnClickListener {
                val args = Bundle().apply {
                    putInt(BookingDetailFragment.ARG_HOTEL_ID, hotel.id)
                    putString(ExploreFragment.ARG_DESTINATION, destination.id)
                }
                findNavController().navigate(R.id.action_booking_to_detail, args)
            }
            container.addView(item.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
