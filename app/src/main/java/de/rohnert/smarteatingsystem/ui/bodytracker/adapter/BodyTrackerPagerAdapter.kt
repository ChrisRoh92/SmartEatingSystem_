package de.rohnert.smarteatingsystem.ui.bodytracker.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.rohnert.smarteatingsystem.ui.bodytracker.fragment.BodyEntryFragment
import de.rohnert.smarteatingsystem.ui.bodytracker.fragment.BodySettingFragment

class BodyTrackerPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle) {


    private val fragments:Array<Fragment> = arrayOf(
        BodyEntryFragment(),
        BodySettingFragment()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}