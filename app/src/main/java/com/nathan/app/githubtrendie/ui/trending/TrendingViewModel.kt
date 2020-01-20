package com.nathan.app.githubtrendie.ui.trending

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nathan.app.githubtrendie.api.GithubTrendingApi
import com.nathan.app.githubtrendie.data.Repo
import com.nathan.app.githubtrendie.data.RepoDao
import com.nathan.app.githubtrendie.di.component.DaggerViewModelInjector
import com.nathan.app.githubtrendie.di.component.ViewModelInjector
import com.nathan.app.githubtrendie.di.module.ApiModule
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TrendingViewModel(private val repoDao: RepoDao) : ViewModel() {

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .apiModule(ApiModule("https://github-trending-api.now.sh/"))
        .build()

    @Inject
    lateinit var trendingApi: GithubTrendingApi

    val repoAdapter: RepoAdapter = RepoAdapter()

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val hasError: MutableLiveData<Boolean> = MutableLiveData()
    val retryClickListener = View.OnClickListener {
        loadRepos()
    }
    val swipeRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        loadRepos()
    }

    private lateinit var subscription: Disposable

    init {
        injector.inject(this)
        loadRepos()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    private fun loadRepos() {
        subscription = Observable.fromCallable { repoDao.all }
            .concatMap { dbList ->
                if (dbList.isEmpty())
                    trendingApi.getRepositories().concatMap { apiList ->
                        repoDao.insertAll(*apiList.toTypedArray())
                        Observable.just(apiList)
                    }
                else
                    Observable.just(dbList)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onFetchReposStart() }
            .doOnTerminate { onFetchReposFinish() }
            .subscribe(
                { result -> onFetchReposSuccess(result) },
                { onRetrieveRepoListError(it) }
            )
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
        hasError.value = true
    }

}
