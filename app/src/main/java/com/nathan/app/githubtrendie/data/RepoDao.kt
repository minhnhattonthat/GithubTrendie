package com.nathan.app.githubtrendie.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RepoDao {
    @get:Query("SELECT * FROM repo")
    val all: List<Repo>

    @Insert
    fun insertAll(vararg repos: Repo)
}
