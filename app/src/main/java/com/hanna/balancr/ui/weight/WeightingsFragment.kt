package com.hanna.balancr.ui.weight

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.hanna.balancr.R
import com.hanna.balancr.model.entities.Weighting
import kotlinx.android.synthetic.main.fragment_weightings.*
import java.text.SimpleDateFormat

class WeightingsFragment : Fragment(R.layout.fragment_weightings) {
    val adapter = WeightingsAdapter()
    val weightingsViewModel: WeightingsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        weightings_recycler_view.adapter = adapter
        weightings_recycler_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        add_weighting_button.setOnClickListener {
            navController.navigate(R.id.action_add_weighting)
        }

        weightingsViewModel.weightings.observe(viewLifecycleOwner, Observer {
            updateUi(it)
        })
    }

    private fun updateUi(weightings: List<Weighting>) {
        if (weightings.isEmpty()) {
            empty_view_image.visibility = View.VISIBLE
            empty_view_text.visibility = View.VISIBLE
            motion_layout.visibility = View.GONE
        } else {
            empty_view_image.visibility = View.GONE
            empty_view_text.visibility = View.GONE
            motion_layout.visibility = View.VISIBLE
            adapter.submitList(weightings)
            updateGraph(weightings)
        }
    }

    private fun updateGraph(weightings: List<Weighting>) {
        val yVals = weightings.reversed().mapIndexed { index, weighting ->
            Entry(index.toFloat(), weighting.weight)
        }
        val lineDataSet = LineDataSet(yVals, "Pesos")
        weightings_graph.data = LineData(lineDataSet)
    }
}