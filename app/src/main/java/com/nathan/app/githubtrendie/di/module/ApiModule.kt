package com.nathan.app.githubtrendie.di.module

import com.nathan.app.githubtrendie.api.GithubTrendingApi
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule(private val apiUrl: String) {

    @Provides
    internal fun provideGithubTrendingApi(retrofit: Retrofit): GithubTrendingApi {
        return retrofit.create(GithubTrendingApi::class.java)
    }

    @Provides
    internal fun provideRetrofitInterface(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }
}