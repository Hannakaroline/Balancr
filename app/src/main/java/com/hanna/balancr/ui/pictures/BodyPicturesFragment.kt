package com.hanna.balancr.ui.pictures

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hanna.balancr.R
import com.hanna.balancr.model.entities.BodyPicture
import kotlinx.android.synthetic.main.fragment_body_pictures.*

class BodyPicturesFragment : Fragment(R.layout.fragment_body_pictures) {

    private val bodyPicturesVieModel: BodyPicturesViewModel by activityViewModels()
    private val bodyPicturesAdapter = BodyPicturesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        body_pictures_view_pager.adapter = bodyPicturesAdapter

        val navController = findNavController()

        add_body_picture_button.setOnClickListener {
            navController.navigate(R.id.action_add_body_picture)
        }

        bodyPicturesVieModel.bodyPictures.observe(viewLifecycleOwner, Observer {
            updateUi(it)
        })
    }

    private fun updateUi(bodyPictures: List<BodyPicture>) {
        if (bodyPictures.isEmpty()) {
            placeholders.visibility = View.VISIBLE
            body_pictures_view_pager.visibility = View.GONE
        } else {
            placeholders.visibility = View.GONE
            body_pictures_view_pager.visibility = View.VISIBLE
            bodyPicturesAdapter.submitList(bodyPictures)
        }
    }
}