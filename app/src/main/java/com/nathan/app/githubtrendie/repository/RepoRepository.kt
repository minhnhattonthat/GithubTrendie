package com.nathan.app.githubtrendie.repository

import android.content.SharedPreferences
import android.text.format.DateUtils
import com.nathan.app.githubtrendie.api.GithubTrendingApi
import com.nathan.app.githubtrendie.db.RepoDao
import com.nathan.app.githubtrendie.vo.Repo
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoRepository @Inject constructor(
    private val trendingApi: GithubTrendingApi,
    private val repoDao: RepoDao,
    private val sharedPreferences: SharedPreferences
) {

    fun getRepos(forceRefresh: Boolean = false): Observable<List<Repo>> {
        if (forceRefresh || isCacheExpired()) {
            return refreshedRepo()
        }
        return Observable.fromCallable { repoDao.all }
            .concatMap { dbList ->
                if (dbList.isEmpty())
                    refreshedRepo()
                else
                    Observable.just(dbList).subscribeOn(Schedulers.io())
            }
    }

    fun refreshedRepo(): Observable<List<Repo>> {
        return trendingApi.getRepositories().concatMap { apiList ->
            repoDao.deleteAll()
            repoDao.insertAll(*apiList.toTypedArray())
            saveCacheTimestamp()
            Observable.fromCallable { repoDao.all }
        }
            .subscribeOn(Schedulers.io())
    }

    private fun isCacheExpired(): Boolean {
        return cacheExpiresInMillis() <= 0
    }

    fun cacheExpiresInMillis(): Long {
        val lastCachedTime = sharedPreferences.getLong(LAST_CACHED_KEY, 0)
        val now = Date().time
        return CACHE_EXPIRATION_MILLIS - (now - lastCachedTime)
    }

    private fun saveCacheTimestamp() {
        val editor = sharedPreferences.edit()
        editor.putLong(LAST_CACHED_KEY, Date().time)
        editor.apply()
    }

    companion object {
        private const val LAST_CACHED_KEY = "lastCached"
        private const val CACHE_EXPIRATION_MILLIS = 2 * DateUtils.HOUR_IN_MILLIS
    }
}