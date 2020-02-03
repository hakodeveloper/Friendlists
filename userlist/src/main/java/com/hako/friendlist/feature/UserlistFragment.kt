package com.hako.friendlist.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hako.base.domain.network.RequestStatus
import com.hako.base.extensions.observeNonNull
import com.hako.base.extensions.toast
import com.hako.friendlist.model.UserViewable
import com.hako.friendlist.viewmodel.UserlistViewmodel
import com.hako.friendlist.widget.UserlistAdapter
import com.hako.friendlist_userlist.R
import kotlinx.android.synthetic.main.fragment_userlist.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class UserlistFragment : Fragment() {

    private val viewModel: UserlistViewmodel by viewModel()
    private val chatAdapter by lazy { UserlistAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_userlist, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycler()
        setObservers()
        viewModel.fetchUsers()
    }

    private fun setObservers() {
        viewModel.data.observeNonNull(this) {
            it.either(::handleFetchError, ::handleFetchSuccess)
        }

        viewModel.requestStatus.observeNonNull(this) {
            when (it) {
                RequestStatus.Ready -> { context?.toast("Ready") }
                RequestStatus.Loading -> { context?.toast("Loading") }
                RequestStatus.Errored -> { context?.toast("Errored") }
            }
        }
    }

    private fun handleFetchError(throwable: Throwable) {
        context?.toast("Could't get data")
        Timber.e(throwable)
    }

    private fun handleFetchSuccess(users: List<UserViewable>) {
        chatAdapter.addAll(users)
    }

    private fun setRecycler() {
        fragment_userlist_recycler_container.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter.apply {
                onItemClick = {
                    context.toast(it.realName)
                }

                onFavoriteClick = {
                    context.toast(it.userName)
                }
            }
        }
    }
}