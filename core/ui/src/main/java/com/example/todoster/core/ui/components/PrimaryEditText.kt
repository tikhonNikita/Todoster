package com.example.todoster.core.ui.components

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import com.example.todoster.core.ui.R

class PrimaryEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val labelTextView: TextView
    private val inputEditText: EditText
    private val errorTextView: TextView

    init {
        // Inflate the compound layout
        LayoutInflater.from(context).inflate(R.layout.primary_edit_text, this, true)
        
        // Find views
        labelTextView = findViewById(R.id.tv_label)
        inputEditText = findViewById(R.id.et_input)
        errorTextView = findViewById(R.id.tv_error)

        // Handle custom attributes
        context.withStyledAttributes(attrs, R.styleable.PrimaryEditText) {
            // Set label if provided
            getString(R.styleable.PrimaryEditText_label)?.let { labelText ->
                setLabel(labelText)
            }
            
            // Set error message if provided (mainly for XML preview)
            getString(R.styleable.PrimaryEditText_errorMessage)?.let { errorText ->
                setError(errorText)
            }
            
            // Set hint
            getString(R.styleable.PrimaryEditText_android_hint)?.let { hint ->
                inputEditText.hint = hint
            }
            
            // Set input type
            val inputType = getInt(R.styleable.PrimaryEditText_android_inputType, InputType.TYPE_CLASS_TEXT)
            inputEditText.inputType = inputType and InputType.TYPE_TEXT_FLAG_MULTI_LINE.inv()
            
            // Set initial text
            getString(R.styleable.PrimaryEditText_android_text)?.let { text ->
                inputEditText.setText(text)
            }
            
            // Set autofill hints (API 26+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getString(R.styleable.PrimaryEditText_android_autofillHints)?.let { autofillHints ->
                    inputEditText.setAutofillHints(autofillHints)
                }
                
                // Set autofill importance
                val autofillImportance = getInt(R.styleable.PrimaryEditText_android_importantForAutofill, View.IMPORTANT_FOR_AUTOFILL_AUTO)
                inputEditText.importantForAutofill = autofillImportance
            }
            
            // Set content description
            getString(R.styleable.PrimaryEditText_android_contentDescription)?.let { contentDesc ->
                inputEditText.contentDescription = contentDesc
            }
            
            // Set IME options
            val imeOptions = getInt(R.styleable.PrimaryEditText_android_imeOptions, EditorInfo.IME_NULL)
            if (imeOptions != EditorInfo.IME_NULL) {
                inputEditText.imeOptions = imeOptions
            }
            
            // Set max lines
            val maxLines = getInt(R.styleable.PrimaryEditText_android_maxLines, -1)
            if (maxLines > 0) {
                inputEditText.maxLines = maxLines
            }
            
            // Set min lines
            val minLines = getInt(R.styleable.PrimaryEditText_android_minLines, -1)
            if (minLines > 0) {
                inputEditText.minLines = minLines
            }
            
            // Set max length
            val maxLength = getInt(R.styleable.PrimaryEditText_android_maxLength, -1)
            if (maxLength > 0) {
                inputEditText.filters = arrayOf(android.text.InputFilter.LengthFilter(maxLength))
            }
            
            // Set enabled state
            val enabled = getBoolean(R.styleable.PrimaryEditText_android_enabled, true)
            inputEditText.isEnabled = enabled
        }
    }

    /**
     * Устанавливает текст метки для поля ввода
     */
    fun setLabel(label: String?) {
        if (label.isNullOrEmpty()) {
            labelTextView.visibility = View.GONE
        } else {
            labelTextView.text = label
            labelTextView.visibility = View.VISIBLE
            // Обновляем content description для accessibility
            inputEditText.contentDescription = inputEditText.contentDescription ?: label
        }
    }

    /**
     * Устанавливает сообщение об ошибке
     */
    fun setError(error: String?) {
        if (error.isNullOrEmpty()) {
            errorTextView.visibility = View.GONE
        } else {
            errorTextView.text = error
            errorTextView.visibility = View.VISIBLE
        }
    }

    /**
     * Очищает сообщение об ошибке
     */
    fun clearError() {
        setError(null)
    }

    /**
     * Получает текст из поля ввода
     */
    fun getText(): String {
        return inputEditText.text.toString()
    }

    /**
     * Устанавливает текст в поле ввода
     */
    fun setText(text: String) {
        inputEditText.setText(text)
    }

    /**
     * Получает доступ к внутреннему EditText для дополнительной настройки
     */
    fun getEditText(): EditText {
        return inputEditText
    }

    /**
     * Устанавливает подсказку для поля ввода
     */
    fun setHint(hint: String) {
        inputEditText.hint = hint
    }

    /**
     * Устанавливает тип ввода
     */
    fun setInputType(inputType: Int) {
        inputEditText.inputType = inputType and InputType.TYPE_TEXT_FLAG_MULTI_LINE.inv()
    }
    
    /**
     * Устанавливает IME options
     */
    fun setImeOptions(imeOptions: Int) {
        inputEditText.imeOptions = imeOptions
    }
    
    /**
     * Устанавливает максимальную длину текста
     */
    fun setMaxLength(maxLength: Int) {
        if (maxLength > 0) {
            inputEditText.filters = arrayOf(android.text.InputFilter.LengthFilter(maxLength))
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return SavedState(superState).apply {
            text = getText()
            labelText = labelTextView.text.toString()
            errorText = if (errorTextView.visibility == View.VISIBLE) {
                errorTextView.text.toString()
            } else {
                null
            }
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        
        super.onRestoreInstanceState(state.superState)
        setText(state.text)
        setLabel(state.labelText)
        state.errorText?.let { setError(it) }
    }

    override fun dispatchSaveInstanceState(container: android.util.SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: android.util.SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }



    private class SavedState : BaseSavedState {
        var text: String = ""
        var labelText: String = ""
        var errorText: String? = null

        constructor(superState: Parcelable?) : super(superState)

        private constructor(parcel: Parcel) : super(parcel) {
            text = parcel.readString() ?: ""
            labelText = parcel.readString() ?: ""
            errorText = parcel.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(text)
            out.writeString(labelText)
            out.writeString(errorText)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}