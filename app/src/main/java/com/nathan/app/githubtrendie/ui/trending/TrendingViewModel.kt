package com.nathan.app.githubtrendie.ui.trending

import android.text.format.DateUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nathan.app.githubtrendie.AppSchedulers
import com.nathan.app.githubtrendie.repository.RepoRepository
import com.nathan.app.githubtrendie.testing.OpenForTesting
import com.nathan.app.githubtrendie.vo.Repo
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OpenForTesting
class TrendingViewModel @Inject constructor(private val repository: RepoRepository) : ViewModel() {

    val repos = MutableLiveData<List<Repo>>()

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val hasError: MutableLiveData<Boolean> = MutableLiveData()

    lateinit var subscription: Disposable
    lateinit var intervalDisposable: Disposable

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
        intervalDisposable.dispose()
    }

    fun loadRepos(forceRefresh: Boolean = false) {
        subscription = repository.getRepos(forceRefresh)
            .observeOn(AppSchedulers.ui())
            .doOnSubscribe { onFetchReposStart() }
            .doOnTerminate { onFetchReposFinish() }
            .doOnComplete { intervalCheckCache() }
            .doOnError { it.printStackTrace() }
            .subscribe(
                { result -> onFetchReposSuccess(result) },
                { onRetrieveRepoListError(it) }
            )
    }

    private fun intervalCheckCache() {
        if (::intervalDisposable.isInitialized) {
            intervalDisposable.dispose()
        }
        val cacheExpiresInMillis = repository.cacheExpiresInMillis()
        val delay = if (cacheExpiresInMillis <= 0) 0 else cacheExpiresInMillis
        intervalDisposable =
            Observable.interval(delay, 2 * DateUtils.HOUR_IN_MILLIS, TimeUnit.MILLISECONDS)
                .concatMap {
                    repository.refreshedRepo()
                }
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnError { it.printStackTrace() }
                .subscribe { result -> onFetchReposSuccess(result) }
    }

    private fun onFetchReposStart() {
        loading.value = true
        hasError.value = false
    }

    private fun onFetchReposFinish() {
        loading.value = false
    }

    private fun onFetchReposSuccess(list: List<Repo>) {
        repos.value = list
    }

    private fun onRetrieveRepoListError(t: Throwable) {
        t.printStackTrace()
        hasError.value = true
    }

}
