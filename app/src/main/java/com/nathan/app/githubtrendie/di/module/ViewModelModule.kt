package com.nathan.app.githubtrendie.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nathan.app.githubtrendie.di.ViewModelFactory
import com.nathan.app.githubtrendie.ui.trending.TrendingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TrendingViewModel::class)
    abstract fun bindRepoViewModel(trendingViewModel: TrendingViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}