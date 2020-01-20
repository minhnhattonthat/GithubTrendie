package com.nathan.app.githubtrendie.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Repo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}