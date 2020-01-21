package com.nathan.app.githubtrendie.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nathan.app.githubtrendie.vo.Repo


@Database(entities = [Repo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}