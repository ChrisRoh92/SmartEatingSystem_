package de.rohnert.smarteatingsystem.frontend.statistics.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import de.rohnert.smarteatingsystem.frontend.statistics.fragment.StatisticBodyFragment
import de.rohnert.smarteatingsystem.frontend.statistics.fragment.StatisticFoodFragment

class StatisticPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private var fragments: ArrayList<Fragment> = arrayListOf(
        StatisticFoodFragment(), StatisticBodyFragment()
    )

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}