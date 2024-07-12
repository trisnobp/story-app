package com.dicoding.storyapp.view.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import com.dicoding.storyapp.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        doOnTextChanged { text, _, _, _ ->
            if (text.toString().length < 8) {
                setError(context.getString(R.string.password_warning), null)
            } else {
                error = null
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.enter_your_password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}