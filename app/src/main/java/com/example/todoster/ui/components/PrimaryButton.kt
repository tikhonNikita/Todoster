package com.example.todoster.ui.components

import android.content.Context
import android.util.AttributeSet
import com.example.todoster.R
import com.google.android.material.button.MaterialButton

class PrimaryButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.primaryButtonStyle
) : MaterialButton(context, attrs, defStyleAttr)