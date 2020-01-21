package com.nathan.app.githubtrendie.ui.trending

import android.text.format.DateUtils
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nathan.app.githubtrendie.repository.RepoRepository
import com.nathan.app.githubtrendie.vo.Repo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TrendingViewModel @Inject constructor(private val repository: RepoRepository) : ViewModel() {

    val repoAdapter: RepoAdapter = RepoAdapter()

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val hasError: MutableLiveData<Boolean> = MutableLiveData()
    val retryClickListener = View.OnClickListener {
        loadRepos()
    }
    val swipeRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        loadRepos(true)
    }

    private lateinit var subscription: Disposable
    private lateinit var intervalDisposable: Disposable

    init {
        loadRepos()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
        intervalDisposable.dispose()
    }

    private fun loadRepos(forceRefresh: Boolean = false) {
        subscription = repository.getRepos(forceRefresh)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onFetchReposStart() }
            .doOnTerminate {
                onFetchReposFinish()
            }
            .doOnComplete {
                intervalCheckCache()
            }
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
        repoAdapter.updateList(list)
    }

    private fun onRetrieveRepoListError(t: Throwable) {
        t.printStackTrace()
        hasError.value = true
    }

}
