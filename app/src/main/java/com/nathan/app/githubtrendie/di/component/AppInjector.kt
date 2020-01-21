package com.nathan.app.githubtrendie.di.component

import com.nathan.app.githubtrendie.GithubTrendieApp

object AppInjector {
    fun init(app: GithubTrendieApp) {
        DaggerAppComponent.builder().application(app)
            .build().inject(app)
    }
}