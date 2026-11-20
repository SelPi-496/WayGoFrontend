package com.example.waygofrontend

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasTrips = savedInstanceState?.getBoolean(KEY_HAS_TRIPS) ?: false

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

        onBackPressedDispatcher.addCallback(this) {
            if (!navController.navigateUp()) finish()
        }
    }

    fun onTripCreated() {
        hasTrips = true
        val options = NavOptions.Builder()
            .setPopUpTo(navController.graph.findStartDestination().id, false, true)
            .setLaunchSingleTop(true)
            .build()
        navController.navigate(R.id.tripsFragment, null, options)
        binding.bottomNav.menu.findItem(R.id.tripsFragment)?.isChecked = true
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
        super.onSaveInstanceState(outState)
    }

    private companion object {
        const val KEY_HAS_TRIPS = "has_trips"

        val topLevelDestinations = setOf(
            R.id.exploreFragment,
            R.id.tripsFragment,
            R.id.bookingListFragment,
            R.id.profileFragment
        )
    }
}
