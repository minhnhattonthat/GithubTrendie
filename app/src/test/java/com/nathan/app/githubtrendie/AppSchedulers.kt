package com.nathan.app.githubtrendie

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

object AppSchedulers {

    fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    fun compute(): Scheduler {
        return Schedulers.trampoline()
    }

    fun ui(): Scheduler {
        return Schedulers.trampoline()
    }
}