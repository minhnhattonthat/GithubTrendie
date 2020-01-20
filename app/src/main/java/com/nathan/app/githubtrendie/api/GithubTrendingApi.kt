package com.nathan.app.githubtrendie.api

import com.nathan.app.githubtrendie.data.Repo
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface GithubTrendingApi {

    @GET("./repositories")
    fun getRepositories(): Observable<List<Repo>>

    companion object {
        private const val BASE_URL = "https://github-trending-api.now.sh/"

        fun getInstance(): GithubTrendingApi {
            // Retrofit client pointed at the Firebase Auth API
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(GithubTrendingApi::class.java)
        }
    }

}