package com.nathan.app.githubtrendie.ui.trending

import android.content.SharedPreferences
import android.text.format.DateUtils
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
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TrendingViewModel(
    private val repoDao: RepoDao,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .apiModule(ApiModule(API_URL))
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
        loadRepos(true)
    }

    private lateinit var subscription: Disposable
    private lateinit var intervalDisposable: Disposable

    init {
        injector.inject(this)
        loadRepos()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
        intervalDisposable.dispose()
    }

    private fun loadRepos(forceRefresh: Boolean = false) {
        subscription = Observable.fromCallable { repoDao.all }
            .concatMap { dbList ->
                if (dbList.isEmpty() || forceRefresh || isCacheExpired())
                    trendingApi.getRepositories().concatMap { apiList ->
                        repoDao.deleteAll()
                        repoDao.insertAll(*apiList.toTypedArray())
                        saveCacheTimestamp()
                        Observable.just(apiList)
                    }
                else
                    Observable.just(dbList)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onFetchReposStart() }
            .doOnTerminate {
                onFetchReposFinish()
                intervalCheckCache()
            }
            .subscribe(
                { result -> onFetchReposSuccess(result) },
                { onRetrieveRepoListError(it) }
            )
    }

    private fun intervalCheckCache() {
        if (::intervalDisposable.isInitialized) {
            intervalDisposable.dispose()
        }
        val delay = if (cacheExpiresInMillis() <= 0) 0 else cacheExpiresInMillis()
        intervalDisposable =
            Observable.interval(delay, 2 * DateUtils.HOUR_IN_MILLIS, TimeUnit.MILLISECONDS)
                .concatMap {
                    trendingApi.getRepositories().concatMap { apiList ->
                        repoDao.deleteAll()
                        repoDao.insertAll(*apiList.toTypedArray())
                        saveCacheTimestamp()
                        Observable.just(apiList)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result -> onFetchReposSuccess(result) }
    }

    private fun isCacheExpired(): Boolean {
        return cacheExpiresInMillis() <= 0
    }

    private fun cacheExpiresInMillis(): Long {
        val lastCachedTime = sharedPreferences.getLong(LAST_CACHED_KEY, 0)
        val now = Date().time
        return CACHE_EXPIRATION_MILLIS - (now - lastCachedTime)
    }

    private fun saveCacheTimestamp() {
        val editor = sharedPreferences.edit()
        editor.putLong(LAST_CACHED_KEY, Date().time)
        editor.apply()
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

    companion object {
        private const val LAST_CACHED_KEY = "lastCached"
        private const val CACHE_EXPIRATION_MILLIS = 2 * DateUtils.HOUR_IN_MILLIS
        private const val API_URL = "https://github-trending-api.now.sh/"
    }

}
