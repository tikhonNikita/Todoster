package com.example.todoster.ui


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoster.R

class SignupFragment() : Fragment(R.layout.sign_up_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val signupButton = view.findViewById<View>(R.id.btn_signup)
        signupButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}