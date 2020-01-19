package com.nathan.app.githubtrendie.ui.trending

import com.nathan.app.githubtrendie.data.Repo

interface OnRepoClickListener {
    fun onClick(repo: Repo?)
}