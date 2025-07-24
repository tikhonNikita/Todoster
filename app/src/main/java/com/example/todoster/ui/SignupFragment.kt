package com.example.todoster.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoster.R
import com.example.todoster.core.ui.components.PrimaryEditText

class SignupFragment() : Fragment(R.layout.sign_up_fragment) {
    
    private lateinit var emailEditText: PrimaryEditText
    private lateinit var passwordEditText: PrimaryEditText
    private lateinit var confirmPasswordEditText: PrimaryEditText
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEditText = view.findViewById(R.id.et_email)
        passwordEditText = view.findViewById(R.id.et_password)
        confirmPasswordEditText = view.findViewById(R.id.et_confirm_password)
        
        val signupButton = view.findViewById<View>(R.id.btn_signup)
        signupButton.setOnClickListener {
            if (validateInput()) {
                // Если валидация прошла успешно, переходим обратно
                findNavController().popBackStack()
            }
        }
    }
    
    /**
     * Валидация введенных данных
     */
    private fun validateInput(): Boolean {
        var isValid = true
        
        // Очищаем предыдущие ошибки
        emailEditText.clearError()
        passwordEditText.clearError()
        confirmPasswordEditText.clearError()
        
        // Валидация email
        val email = emailEditText.getText().trim()
        if (email.isEmpty()) {
            emailEditText.setError("Email is required")
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email address")
            isValid = false
        }
        
        // Валидация пароля
        val password = passwordEditText.getText()
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required")
            isValid = false
        } else if (password.length < 6) {
            passwordEditText.setError("Password must be at least 6 characters")
            isValid = false
        }
        
        // Валидация подтверждения пароля
        val confirmPassword = confirmPasswordEditText.getText()
        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Please confirm your password")
            isValid = false
        } else if (password != confirmPassword) {
            confirmPasswordEditText.setError("Passwords do not match")
            isValid = false
        }
        
        return isValid
    }
}