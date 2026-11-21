package com.example.waygofrontend

import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.waygofrontend.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var hasTrips = false
    private var selectedDestinationId: String? = null
    private val lockBadges = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasTrips = savedInstanceState?.getBoolean(KEY_HAS_TRIPS) ?: false
        selectedDestinationId = savedInstanceState?.getString(KEY_SELECTED_DESTINATION)

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController

        binding.bottomNav.setOnItemSelectedListener { item ->
            val needsTrip = item.itemId == R.id.tripsFragment ||
                item.itemId == R.id.bookingListFragment

            if (!hasTrips && needsTrip) {
                navController.navigate(R.id.tripPlannerFragment)
                true
            } else {
                NavigationUI.onNavDestinationSelected(item, navController)
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopLevel = destination.id in topLevelDestinations
            binding.bottomNav.visibility = if (isTopLevel) View.VISIBLE else View.GONE
            if (isTopLevel) {
                binding.bottomNav.menu.findItem(destination.id)?.isChecked = true
            }
        }

        setupLockBadges()

        onBackPressedDispatcher.addCallback(this) {
            if (!navController.navigateUp()) finish()
        }
    }

    fun onTripCreated(destinationId: String) {
        hasTrips = true
        selectedDestinationId = destinationId
        updateLockBadges()
        val options = NavOptions.Builder()
            .setPopUpTo(navController.graph.findStartDestination().id, false, true)
            .setLaunchSingleTop(true)
            .build()
        navController.navigate(R.id.tripsFragment, null, options)
        binding.bottomNav.menu.findItem(R.id.tripsFragment)?.isChecked = true
    }

    fun selectedDestinationId(): String? = selectedDestinationId

    private fun setupLockBadges() {
        val size = resources.getDimensionPixelSize(R.dimen.lock_badge_size)
        val padding = resources.getDimensionPixelSize(R.dimen.lock_badge_padding)
        val offset = resources.getDimensionPixelSize(R.dimen.lock_badge_offset)
        val offsetY = resources.getDimensionPixelSize(R.dimen.lock_badge_offset_y)
        listOf(R.id.tripsFragment, R.id.bookingListFragment).forEach { id ->
            val itemView = binding.bottomNav.findViewById<ViewGroup>(id) ?: return@forEach
            val badge = ImageView(this).apply {
                setImageResource(R.drawable.ic_lock)
                setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.waygo_text_muted))
                setBackgroundResource(R.drawable.bg_lock_badge)
                setPadding(padding, padding, padding, padding)
                contentDescription = getString(R.string.cd_locked)
                translationX = offset.toFloat()
                translationY = offsetY.toFloat()
            }
            val params = FrameLayout.LayoutParams(size, size, Gravity.TOP or Gravity.CENTER_HORIZONTAL)
            itemView.addView(badge, params)
            lockBadges.add(badge)
        }
        updateLockBadges()
    }

    private fun updateLockBadges() {
        val visibility = if (hasTrips) View.GONE else View.VISIBLE
        lockBadges.forEach { it.visibility = visibility }
    }

    fun toggleTheme() {
        val nextMode = if (isNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO
        } else {
            AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(nextMode)
    }

    fun isNightMode(): Boolean {
        val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_HAS_TRIPS, hasTrips)
        outState.putString(KEY_SELECTED_DESTINATION, selectedDestinationId)
        super.onSaveInstanceState(outState)
    }

    private companion object {
        const val KEY_HAS_TRIPS = "has_trips"
        const val KEY_SELECTED_DESTINATION = "selected_destination"

        val topLevelDestinations = setOf(
            R.id.exploreFragment,
            R.id.tripsFragment,
            R.id.bookingListFragment,
            R.id.profileFragment
        )
    }
}
