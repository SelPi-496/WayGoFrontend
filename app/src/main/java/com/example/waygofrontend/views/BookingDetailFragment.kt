package com.example.waygofrontend.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.waygofrontend.R
import com.example.waygofrontend.data.WaygoData
import com.example.waygofrontend.databinding.FragmentBookingDetailBinding

class BookingDetailFragment : Fragment() {

    private var _binding: FragmentBookingDetailBinding? = null
    private val binding get() = _binding!!

    private var favorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val destination = WaygoData.destination(arguments?.getString(ExploreFragment.ARG_DESTINATION))
        val hotelId = arguments?.getInt(ARG_HOTEL_ID) ?: 0
        val hotel = WaygoData.hotels(destination.id).firstOrNull { it.id == hotelId }
            ?: WaygoData.hotels(destination.id).first()
        val total = hotel.price * 3

        binding.photo.setBackgroundResource(hotel.imageRes)
        binding.tvDestination.text = destination.name
        binding.tvHotelName.text = hotel.name
        binding.tvRating.text = "\u2605 ${hotel.rating}  ·  ${getString(R.string.reviews_count, hotel.reviews)}"
        binding.tvTotal.text = WaygoData.formatPrice(total)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnFavorite.setOnClickListener {
            favorite = !favorite
            binding.btnFavorite.setImageResource(
                if (favorite) R.drawable.ic_heart_filled else R.drawable.ic_heart
            )
            binding.btnFavorite.imageTintList = ContextCompat.getColorStateList(
                requireContext(),
                if (favorite) R.color.waygo_coral else R.color.waygo_text_primary
            )
        }

        binding.btnBookNow.setOnClickListener {
            binding.normalContainer.visibility = View.GONE
            binding.confirmedContainer.visibility = View.VISIBLE
            binding.tvSuccessMessage.text =
                getString(R.string.booking_success_message, hotel.name, destination.name)
            binding.tvBookingCode.text = "WG0825${hotel.id + 24}"
            binding.tvConfirmedTotal.text = WaygoData.formatPrice(total)
            binding.root.scrollTo(0, 0)
        }
        binding.btnDone.setOnClickListener { findNavController().navigateUp() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_HOTEL_ID = "hotel_id"
    }
}
