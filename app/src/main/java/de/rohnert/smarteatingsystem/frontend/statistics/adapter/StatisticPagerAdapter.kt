package de.rohnert.smarteatingsystem.frontend.statistics.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.rohnert.smarteatingsystem.frontend.statistics.fragment.StatisticBodyFragment
import de.rohnert.smarteatingsystem.frontend.statistics.fragment.StatisticFoodFragment

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