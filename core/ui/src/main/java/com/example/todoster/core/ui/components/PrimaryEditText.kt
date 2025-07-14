package com.example.todoster.core.ui.components

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import com.example.todoster.core.ui.R

class PrimaryEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.primaryEditTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        setSingleLine()
        inputType = inputType and InputType.TYPE_TEXT_FLAG_MULTI_LINE.inv()

        background = AppCompatResources.getDrawable(context, R.drawable.text_input_background)
    }
}