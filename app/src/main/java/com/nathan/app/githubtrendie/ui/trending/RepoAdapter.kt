package com.nathan.app.githubtrendie.ui.trending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.nathan.app.githubtrendie.data.Repo
import com.nathan.app.githubtrendie.databinding.RowRepoBinding
import com.nathan.app.githubtrendie.ui.trending.RepoAdapter.RepoHolder

class RepoAdapter internal constructor(private val clickListener: OnRepoClickListener) :
    RecyclerView.Adapter<RepoHolder>() {

    var repos = mutableListOf<Repo>()
        set(list) {
            val result = DiffUtil.calculateDiff(RepoDiffCallback(this.repos, list))
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoHolder {
        val binding =
            RowRepoBinding.inflate(LayoutInflater.from(parent.context))
        return RepoHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoHolder, position: Int) {
        val repo = repos[position]
        holder.binding.repo = repo
        holder.binding.listener = clickListener
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    inner class RepoHolder(var binding: RowRepoBinding) :
        ViewHolder(binding.root)

    internal inner class RepoDiffCallback(
        private var oldList: List<Repo>,
        private var newList: List<Repo>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldList[oldItemPosition].url == newList[newItemPosition].url
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }

}