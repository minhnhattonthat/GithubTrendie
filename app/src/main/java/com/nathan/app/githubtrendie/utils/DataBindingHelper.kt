package com.nathan.app.githubtrendie.utils

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.text.NumberFormat

@BindingAdapter("avatarUrl")
fun avatarUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView)
        .load(url)
        .apply(
            RequestOptions()
                .centerCrop().circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        )
        .into(imageView)
}

@BindingAdapter("tintColor")
fun tintColor(imageView: ImageView, tintString: String?) {
    if (tintString != null) {
        imageView.setColorFilter(Color.parseColor(tintString))
    }
}

@BindingAdapter("formattedNumber")
fun formattedNumber(textView: TextView, number: Int) {
    textView.text = NumberFormat.getInstance().format(number)
}
