package de.rohnert.smarteatingsystem.frontend.bodytracker.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import de.rohnert.smarteatingsystem.frontend.bodytracker.fragment.BodyEntryFragment
import de.rohnert.smarteatingsystem.frontend.bodytracker.fragment.BodySettingFragment

class BodyTrackerPagerAdapter(fm:FragmentManager): FragmentStatePagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragments:ArrayList<Fragment> = arrayListOf(BodyEntryFragment(),BodySettingFragment())

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}