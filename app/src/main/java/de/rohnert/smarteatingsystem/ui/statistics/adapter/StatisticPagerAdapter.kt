package de.rohnert.smarteatingsystem.ui.statistics.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.rohnert.smarteatingsystem.ui.statistics.fragment.StatisticBodyFragment
import de.rohnert.smarteatingsystem.ui.statistics.fragment.StatisticFoodFragment

class StatisticPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle)
{

    var fragments = arrayListOf(StatisticFoodFragment(),StatisticBodyFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}