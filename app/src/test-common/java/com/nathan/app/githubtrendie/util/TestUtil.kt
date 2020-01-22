package com.nathan.app.githubtrendie.util

import com.nathan.app.githubtrendie.vo.Repo


object TestUtil {

    fun createRepos(count: Int, name: String, author: String, description: String): List<Repo> {
        return (0 until count).map {
            createRepo(
                name = name + it,
                author = author + it,
                description = description + it
            )
        }
    }

    fun createRepo(name: String, author: String, description: String) = Repo(
        author = author,
        avatar = "https://github.com/$author.png",
        currentPeriodStars = 3,
        description = description,
        forks = 3,
        language = "C++",
        languageColor = "#FFFFFF",
        name = name,
        stars = 3,
        url = "https://github.com/$author/$name"
    )
}
