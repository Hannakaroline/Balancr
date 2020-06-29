package com.hanna.balancr.ui.pictures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.hanna.balancr.R
import kotlinx.android.synthetic.main.fragment_body_pictures.*

class BodyPicturesFragment : Fragment(R.layout.fragment_body_pictures) {

    private val bodyPicturesViewModel: BodyPicturesViewModel by activityViewModels()
    private val bodyPicturesAdapter = BodyPicturesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (super.onViewCreated(view, savedInstanceState))

        //body_pictures_view_pager.adapter = bodyPicturesAdapter

        val navController = findNavController()

        add_body_picture_button.setOnClickListener {
            navController.navigate(R.id.action_add_body_picture)
        }

        bodyPicturesViewModel.bodyPictures.ob

    }
}