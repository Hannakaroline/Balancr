package com.hanna.balancr.ui.weight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hanna.balancr.R

class WeightingsFragment : Fragment() {

    private lateinit var homeViewModel: WeightingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(WeightingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_weightings, container, false)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        return root
    }
}