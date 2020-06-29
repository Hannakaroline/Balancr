package com.hanna.balancr.ui.pictures.add

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import androidx.activity.addCallback
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.hanna.balancr.R
import kotlinx.android.synthetic.main.fragment_add_body_picture.*
import kotlinx.android.synthetic.main.fragment_add_body_picture.date_edit_text
import kotlinx.android.synthetic.main.fragment_add_body_picture.date_text_input_layout
import java.io.File
import java.util.*

class AddBodyPictureFragment : Fragment(R.layout.fragment_add_body_picture),
    DatePickerDialog.OnDateSetListener {
    private val PICK_IMAGE_CODE: Int = 1122
    private val addBodyPictureViewModel: AddBodyPictureViewModel by activityViewModels()

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

        addBodyPictureViewModel.clearState()
        addBodyPictureViewModel.pickedImage.observe(viewLifecycleOwner, Observer {
            onImagePicked(it)
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navController.popBackStack(R.id.navigation_body_pictures, false)
        }

        addBodyPictureViewModel.addBodyPictureState.observe(viewLifecycleOwner, Observer {
            if (it == AddBodyPictureViewModel.AddBodyPictureState.Finished) {
                hideKeyboard()
                navController.popBackStack(R.id.navigation_body_pictures, false)
            }
        })

        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_MONTH]
        val month = calendar[Calendar.MONTH]
        val year = calendar[Calendar.YEAR]
        date_edit_text.setText(String.format("%02d/%02d/%04d", day, month + 1, year))
        date_text_input_layout.setEndIconOnClickListener {
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        select_from_gallery.setOnClickListener {
            pickImage()
        }

        save_image.setOnClickListener {
            addBodyPictureViewModel.saveBodyPicture(
                date_edit_text.text.toString()
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().popBackStack(R.id.navigation_body_pictures, false)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pickImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start(PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != PICK_IMAGE_CODE) return
        if (resultCode == Activity.RESULT_OK) {
            val filePath = ImagePicker.getFilePath(data)
            addBodyPictureViewModel.pickedImage.value = filePath
        }
    }

    private fun onImagePicked(imagePath: String?) {
        if (imagePath == null) {
            thumbnail.setImageDrawable(null)
            save_image.visibility = View.GONE
        } else {
            Glide.with(thumbnail)
                .load(imagePath)
                .placeholder(R.drawable.body)
                .into(thumbnail)
            save_image.visibility = View.VISIBLE
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        date_edit_text.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year))
    }

    private fun hideKeyboard() {
        val inputManager = activity?.getSystemService<InputMethodManager>()
        view?.run {
            inputManager?.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}