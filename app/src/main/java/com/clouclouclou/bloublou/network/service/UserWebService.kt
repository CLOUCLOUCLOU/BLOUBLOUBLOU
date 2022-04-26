package com.clouclouclou.bloublou.network.service

import com.clouclouclou.bloublou.model.User
import retrofit2.Response
import retrofit2.http.GET

interface UserWebService {
    @GET("users/info")
    suspend fun getInfo(): Response<User>
}