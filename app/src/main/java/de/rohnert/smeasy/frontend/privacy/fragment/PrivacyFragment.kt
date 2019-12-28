package de.rohnert.smeasy.frontend.privacy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import de.rohnert.smeasy.R
import de.rohnert.smeasy.frontend.share.fragment.ShareViewModel

class PrivacyFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_privacy, container, false)

        return root
    }
}