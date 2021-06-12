package com.peresapy.fundamentos_eh_ho.details


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.peresapy.fundamentos_eh_ho.model.Details
import com.peresapy.fundamentos_eh_ho.repository.Repository
import com.peresapy.fundamentos_eh_ho.details.DetailsViewModel.State.DetailsReceived
import com.peresapy.fundamentos_eh_ho.model.Topic

class DetailsViewModel(private val repository: Repository) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData<State>().apply {
        postValue(State.LoadingPost.Loading)
    }

    val state: LiveData<State> = _state

    fun loadPosts(topic: Topic) {
        _state.postValue(
            _state.value?.let {
                when (it) {
                    is State.DetailsReceived -> State.LoadingPost.LoadingWithPosts(it.posts)
                    is State.LoadingPost -> it
                    else -> State.LoadingPost.Loading
                }
            })

        repository.getDetail(topic) {
            it.fold(::onPostReceived, ::onPostFailure)
        }
    }


    private fun onPostReceived(posts: List<Details>) {
        _state.postValue(
            posts.takeUnless { it.isEmpty() }?.let(::DetailsReceived) ?: State.NoPost
        )
    }

    private fun onPostFailure(throwable: Throwable) {
        _state.postValue(State.NoPost)
    }


    sealed class State {
        sealed class LoadingPost : DetailsViewModel.State() {
            object Loading : LoadingPost()
            data class LoadingWithPosts(val posts: List<Details>) : LoadingPost()
        }

        data class DetailsReceived(val posts: List<Details>) : State()
        object NoPost : State()
    }

    class DetailsViewModelProviderFactory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
            DetailsViewModel::class.java -> DetailsViewModel(repository) as T
            else -> throw IllegalArgumentException("DetailsViewModelProviderFactory can only create instances of the PostsViewModel")
        }

    }
}