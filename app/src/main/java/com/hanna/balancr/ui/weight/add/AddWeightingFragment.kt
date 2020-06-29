package com.hanna.balancr.ui.weight.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.hanna.balancr.R
import kotlinx.android.synthetic.main.fragment_register_weight.*
import java.util.Calendar

class AddWeightingFragment : Fragment(R.layout.fragment_register_weight),
    DatePickerDialog.OnDateSetListener {
    private val addWeightingViewModel: AddWeightingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        addWeightingViewModel.clearState()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navController.popBackStack(R.id.navigation_weighting, false)
        }
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_MONTH]
        val month = calendar[Calendar.MONTH]
        val year = calendar[Calendar.YEAR]
        date_edit_text.setText(String.format("%02d/%02d/%04d", day, month + 1, year))
        date_text_input_layout.setEndIconOnClickListener {
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        date_edit_text.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                commitWeighting()
                true
            } else false
        }

        save_weight_button.setOnClickListener {
            commitWeighting()
        }

        addWeightingViewModel.addWeightingState.observe(viewLifecycleOwner, Observer {
            when (it) {
                AddWeightingViewModel.AddWeightingState.WithError -> notify(R.string.invalid_input)
                AddWeightingViewModel.AddWeightingState.Finished -> {
                    hideKeyboard()
                    navController.popBackStack(
                        R.id.navigation_weighting, false
                    )
                }
                else -> {
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().popBackStack(R.id.navigation_weighting, false)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun commitWeighting() {
        addWeightingViewModel.saveWeight(
            weight_edit_text.text?.toString()?.toFloatOrNull(),
            date_edit_text.text?.toString()
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        date_edit_text.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year))
    }

    private fun notify(errorStringId: Int) {
        Toast.makeText(context, errorStringId, Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        val inputManager = activity?.getSystemService<InputMethodManager>()
        view?.run {
            inputManager?.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}