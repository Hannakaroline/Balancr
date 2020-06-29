package com.hanna.balancr.model

import com.hanna.balancr.model.daos.BodyPictureDao
import com.hanna.balancr.model.daos.UserDao
import com.hanna.balancr.model.daos.WeightingDao
import com.hanna.balancr.model.entities.User
import java.security.MessageDigest

class BalancrRepository (
    private val userDao: UserDao,
    private val weightingDao: WeightingDao,
    private val bodyPictureDao: BodyPictureDao
){

}
