package com.nathan.app.githubtrendie.ui.trending

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nathan.app.githubtrendie.R

class LoadingAdapter(private val list: IntArray) :
    RecyclerView.Adapter<LoadingAdapter.LoadingHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingHolder {
        return LoadingHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_loading,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: LoadingHolder, position: Int) {
    }

    inner class LoadingHolder(view: View) : RecyclerView.ViewHolder(view)

}