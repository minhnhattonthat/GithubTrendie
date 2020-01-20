package com.nathan.app.githubtrendie.di.component

import com.nathan.app.githubtrendie.di.module.ApiModule
import com.nathan.app.githubtrendie.ui.trending.TrendingViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ApiModule::class)])
interface ViewModelInjector {

    fun inject(trendingViewModel: TrendingViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun apiModule(networkModule: ApiModule): Builder
    }
}
