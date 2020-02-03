package com.hako.userlist.domain.datasource

import com.hako.userlist.model.User
import io.reactivex.Single
import retrofit2.http.GET

interface UserlistRemoteApi {

    @GET("/users")
    fun getUsers(): Single<List<User>>
}