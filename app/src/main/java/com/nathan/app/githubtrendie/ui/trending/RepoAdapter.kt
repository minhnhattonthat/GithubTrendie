package com.nathan.app.githubtrendie.ui.trending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.nathan.app.githubtrendie.databinding.RowRepoBinding
import com.nathan.app.githubtrendie.ui.trending.RepoAdapter.RepoHolder
import com.nathan.app.githubtrendie.vo.Repo

class RepoAdapter internal constructor() :
    RecyclerView.Adapter<RepoHolder>() {

    private lateinit var repoList: List<Repo>

    private var lastPositionExpanded = -1

    fun updateList(list: List<Repo>) {
        lastPositionExpanded = -1
        if (!::repoList.isInitialized) {
            repoList = list
            notifyDataSetChanged()
        } else {
            val result = DiffUtil.calculateDiff(RepoDiffCallback(this.repoList, list))
            result.dispatchUpdatesTo(this)
            repoList = list
        }
    }

    fun sortByStar() {
        if (!::repoList.isInitialized) {
            return
        }
        lastPositionExpanded = -1
        repoList = repoList.sortedByDescending { it.stars }
        notifyDataSetChanged()
    }

    fun sortByName() {
        if (!::repoList.isInitialized) {
            return
        }
        lastPositionExpanded = -1
        repoList = repoList.sortedBy { it.name }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoHolder {
        val binding =
            RowRepoBinding.inflate(LayoutInflater.from(parent.context))
        return RepoHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoHolder, position: Int) {
        val repo = repoList[position]
        holder.binding.repo = repo
        holder.binding.isExpanded = position == lastPositionExpanded
        holder.binding.root.setOnClickListener {
            if (lastPositionExpanded == position) {
                lastPositionExpanded = -1
                notifyItemChanged(position)
            } else {
                val lastPosition = lastPositionExpanded
                lastPositionExpanded = position
                notifyItemChanged(lastPosition)
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (::repoList.isInitialized) repoList.size else 0
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