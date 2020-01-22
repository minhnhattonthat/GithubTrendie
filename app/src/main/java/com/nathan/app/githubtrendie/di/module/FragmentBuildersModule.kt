package com.nathan.app.githubtrendie.di.module

import com.nathan.app.githubtrendie.ui.trending.TrendingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeTrendingFragment(): TrendingFragment
}