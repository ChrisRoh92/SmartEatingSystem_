package de.rohnert.smarteatingsystem.ui.foodtracker.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.rohnert.smarteatingsystem.ui.foodtracker.fragment.foodchooser.FoodListFragment

class FoodChooserViewPager2Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle) {

    // TODO("MealListFragment wieder aufnehmen, wenn ready!")
    private val fragments:Array<Fragment> = arrayOf(
        FoodListFragment()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}