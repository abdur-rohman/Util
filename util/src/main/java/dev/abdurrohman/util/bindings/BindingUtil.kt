package dev.abdurrohman.util.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import dev.abdurrohman.util.R
import dev.abdurrohman.util.extensions.isValid

@BindingAdapter("url")
fun setImageUrl(imageView: ImageView, string: String?) {
    if (string.isValid) Glide.with(imageView.context)
        .load(string)
        .apply { error(R.drawable.ic_file_not_found) }
        .into(imageView)
    else imageView.setImageResource(R.drawable.ic_file_not_found)
}