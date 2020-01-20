package com.nathan.app.githubtrendie.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.nathan.app.githubtrendie.data.AppDatabase
import com.nathan.app.githubtrendie.ui.trending.TrendingViewModel

class ViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrendingViewModel::class.java)) {
            val db =
                Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "repos")
                    .build()
            @Suppress("UNCHECKED_CAST")
            return TrendingViewModel(db.repoDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}