package com.example.todoster.core.ui.components

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
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
        }
    }

    /**
     * Устанавливает текст метки для поля ввода
     */
    fun setLabel(label: String?) {
        if (label.isNullOrEmpty()) {
            labelTextView.visibility = GONE
        } else {
            labelTextView.text = label
            labelTextView.visibility = VISIBLE
        }
    }

    /**
     * Устанавливает сообщение об ошибке
     */
    fun setError(error: String?) {
        if (error.isNullOrEmpty()) {
            errorTextView.visibility = GONE
        } else {
            errorTextView.text = error
            errorTextView.visibility = VISIBLE
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
}