package com.nathan.app.githubtrendie.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nathan.app.githubtrendie.vo.Repo

@Dao
interface RepoDao {
    @get:Query("SELECT * FROM repo")
    val all: List<Repo>

    @Insert
    fun insertAll(vararg repos: Repo)

    @Query("DELETE FROM repo")
    fun deleteAll()
}
