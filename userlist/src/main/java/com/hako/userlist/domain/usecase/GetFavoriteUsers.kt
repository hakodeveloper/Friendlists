package com.hako.userlist.domain.usecase

import com.hako.userlist.domain.datasource.UserDao
import com.hako.userlist.model.User
import com.hako.userlist.model.toUserViewable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class GetFavoriteUsers(private val dao: UserDao) {

    fun execute(
        onSuccess: (List<User>) -> Unit,
        onEmpty: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        Single.fromCallable { dao.getFavoriteUsers() }
            .subscribeOn(Schedulers.io())
            .doOnSuccess { dbUsers ->
                if (dbUsers.isEmpty()) {
                    onEmpty()
                } else {
                    onSuccess(dbUsers.map { it.toUserViewable() })
                }
            }
            .doOnError { onError(it) }
            .onErrorReturn { emptyList() }
            .subscribe()
    }
}
