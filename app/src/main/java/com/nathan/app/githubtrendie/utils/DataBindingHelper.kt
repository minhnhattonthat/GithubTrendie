package com.nathan.app.githubtrendie.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("avatarUrl")
fun avatarUrl(imageView: ImageView?, url: String?) {
    Glide.with(imageView!!)
        .load(url)
        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
        .into(imageView)
}