package com.nathan.app.githubtrendie.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Repo(
    val author: String,
    val avatar: String,
    val currentPeriodStars: Int,
    val description: String,
    val forks: Int,
    val language: String?,
    val languageColor: String?,
    val name: String,
    val stars: Int,
    @PrimaryKey
    val url: String
)
