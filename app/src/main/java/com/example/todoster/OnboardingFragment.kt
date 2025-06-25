package com.example.todoster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.todoster.utils.argument

class OnboardingPageFragment : Fragment() {

    private val imageRes by argument<Int>()
    private val title by argument<String>()
    private val description by argument<String>()
    private val buttonText by argument<String>()

    var onButtonClicked: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    private fun setupViews(view: View) {
        view.findViewById<ImageView>(R.id.viewpager_placeholder).setImageResource(imageRes)
        view.findViewById<TextView>(R.id.onboarding_title).text = title
        view.findViewById<TextView>(R.id.onboarding_description).text = description
        view.findViewById<Button>(R.id.complete_onboarding_button).text = buttonText


        view.findViewById<Button>(R.id.complete_onboarding_button).setOnClickListener {
            onButtonClicked?.invoke()
        }
    }

    companion object {
        fun newInstance(
            imageRes: Int,
            title: String,
            description: String,
            buttonText: String,
            onContinueClicked: () -> Unit
        ): OnboardingPageFragment {
            return OnboardingPageFragment().apply {
                arguments = bundleOf(
                    "imageRes" to imageRes,
                    "title" to title,
                    "description" to description,
                    "buttonText" to buttonText,
                )
                this.onButtonClicked = onContinueClicked
            }
        }
    }
}