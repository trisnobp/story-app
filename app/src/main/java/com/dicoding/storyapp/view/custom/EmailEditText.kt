package com.dicoding.storyapp.view.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import com.dicoding.storyapp.R

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {
    init {
        doOnTextChanged { text, _, _, _ ->
            if (!Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
                setError(context.getString(R.string.the_format_of_email_address_is_invalid), null)
            } else {
                error = null
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.enter_your_email)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}