package dev.abdurrohman.util.extensions

import android.app.Activity
import android.content.SharedPreferences
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.io.File

val CharSequence?.toFile get() = File(toString())
val CharSequence?.isValid get() = this != null && isNotEmpty()
val CharSequence?.isEmail
    get() = isValid && Patterns.EMAIL_ADDRESS.matcher(this.toString()).matches()

val CharSequence?.isAlphanumeric
    get() = toString().toCharArray().all { it.isLetterOrDigit() } &&
            toString().toCharArray().any { it.isDigit() } &&
            toString().toCharArray().any { it.isLetter() }

val File.isExist get() = exists()

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(requireActivity(), message, duration).show()

fun View.showSnackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_LONG,
    @ColorRes textMessageColor: Int = android.R.color.white,
    @ColorRes textActionColor: Int = android.R.color.holo_red_light,
    textAction: String = "Dismiss",
    onAction: () -> Unit = { }
) = Snackbar.make(this, message, duration).also {
    it.setTextColor(context.getColor(textMessageColor))
    it.setActionTextColor(context.getColor(textActionColor))
    it.setAction(textAction) { _ -> it.dismiss(); onAction() }
}.show()

fun View.dismissKeyboard() {
    (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)?.run {
        hideSoftInputFromWindow(windowToken, 0)
    }
}

@Suppress("UNCHECKED_CAST")
private inline fun <reified T> SharedPreferences.save(key: String, value: T) {
    edit {
        when (value) {
            is Int -> putInt(key, value)
            is String -> putString(key, value)
            is Float -> putFloat(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Set<*> -> putStringSet(key, value as? Set<String> ?: setOf())
        }
    }
}

fun safeRun(success: () -> Unit, error: (Exception) -> Unit) {
    try {
        success.invoke()
    } catch (ex: Exception) {
        ex.printStackTrace()
        error.invoke(ex)
    }
}
