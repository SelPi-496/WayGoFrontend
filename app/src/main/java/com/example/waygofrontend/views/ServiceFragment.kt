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
import com.example.waygofrontend.databinding.FragmentServiceBinding
import com.example.waygofrontend.databinding.ItemServiceBinding

class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val label = arguments?.getString(ExploreFragment.ARG_CATEGORY) ?: WaygoData.categories[0].label
        val category = WaygoData.categories.firstOrNull { it.label == label } ?: WaygoData.categories[0]
        val content = WaygoData.service(label)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.tvTitle.text = category.label
        binding.etSearch.hint = getString(R.string.service_search_hint, category.label.lowercase())
        binding.tvSubtitle.text = content.subtitle
        binding.icon.setImageResource(category.iconRes)
        binding.icon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))

        render(content)
    }

    private fun render(content: WaygoData.ServiceContent) {
        val container = binding.serviceList
        container.removeAllViews()
        content.items.forEachIndexed { index, service ->
            val item = ItemServiceBinding.inflate(layoutInflater, container, false)
            item.photo.setBackgroundResource(service.imageRes)
            item.tvTitle.text = service.title
            item.tvNote.text = service.note
            item.tvPrice.text = service.price
            item.tvRating.text = "\u2605 4.${9 - index}"
            var chosen = false
            item.btnChoose.setOnClickListener {
                chosen = !chosen
                item.btnChoose.setText(if (chosen) R.string.service_chosen else R.string.service_view)
                item.btnChoose.backgroundTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    if (chosen) R.color.waygo_success else R.color.waygo_coral
                )
            }
            container.addView(item.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
