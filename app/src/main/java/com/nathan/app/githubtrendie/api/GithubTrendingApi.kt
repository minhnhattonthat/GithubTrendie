package com.nathan.app.githubtrendie.api

import com.nathan.app.githubtrendie.vo.Repo
import io.reactivex.Observable
import retrofit2.http.GET

interface GithubTrendingApi {

    @GET("./repositories")
    fun getRepositories(): Observable<List<Repo>>

}