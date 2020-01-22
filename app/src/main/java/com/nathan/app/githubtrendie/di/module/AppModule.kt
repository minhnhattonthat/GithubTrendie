package com.nathan.app.githubtrendie.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.nathan.app.githubtrendie.AppSchedulers
import com.nathan.app.githubtrendie.api.GithubTrendingApi
import com.nathan.app.githubtrendie.db.AppDatabase
import com.nathan.app.githubtrendie.db.RepoDao
import com.nathan.app.githubtrendie.repository.RepoRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    internal fun provideGithubTrendingApi(retrofit: Retrofit): GithubTrendingApi {
        return retrofit.create(GithubTrendingApi::class.java)
    }

    @Singleton
    @Provides
    internal fun provideRetrofitInterface(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://github-trending-api.now.sh/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(AppSchedulers.io()))
            .build()
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDatabase {
        return Room
            .databaseBuilder(app.applicationContext, AppDatabase::class.java, "repos.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRepoDao(db: AppDatabase): RepoDao {
        return db.repoDao()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("global", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideRepository(
        githubTrendingApi: GithubTrendingApi,
        repoDao: RepoDao,
        sharedPreferences: SharedPreferences
    ): RepoRepository {
        return RepoRepository(githubTrendingApi, repoDao, sharedPreferences)
    }
}