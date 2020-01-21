package com.nathan.app.githubtrendie.di.component

import android.app.Application
import com.nathan.app.githubtrendie.GithubTrendieApp
import com.nathan.app.githubtrendie.di.module.AppModule
import com.nathan.app.githubtrendie.di.module.FragmentModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, FragmentModule::class, AppModule::class])
interface AppComponent {

    fun inject(app: GithubTrendieApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}