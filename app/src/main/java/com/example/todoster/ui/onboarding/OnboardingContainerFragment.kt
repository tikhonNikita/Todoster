package com.example.todoster.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todoster.R
import com.example.todoster.ui.components.OnboardingViewPager

class OnboardingContainerFragment : Fragment() {

    private lateinit var onboardingViewPager: OnboardingViewPager
    private lateinit var adapter: OnboardingPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.onboarding_container_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSkipButton()
        setupOnboardingViewPager(view)
    }

    private fun setupSkipButton() {
        val skipButton = view?.findViewById<TextView>(R.id.skip_button)
        skipButton?.setOnClickListener {
            skipToEnd()
        }
    }

    private fun setupOnboardingViewPager(view: View) {
        onboardingViewPager = view.findViewById(R.id.onboarding_viewpager)

        adapter = OnboardingPagerAdapter(requireActivity())
        onboardingViewPager.setAdapter(adapter)
    }

    private fun navigateToNext() {
        val currentItem = onboardingViewPager.getCurrentItem()
        if (currentItem < adapter.itemCount - 1) {
            onboardingViewPager.setCurrentItem(currentItem + 1)
        } else {
            findNavController().navigate(R.id.action_onboarding_to_hello)
        }
    }

    private fun skipToEnd() {
        findNavController().navigate(R.id.action_onboarding_to_hello)
    }

    private inner class OnboardingPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        private val onboardingData = listOf(
            OnboardingData(
                imageRes = R.drawable.onboarding_image,
                title = "Your convenience in making a todo list",
                description = "Here's a mobile platform that helps you create task or to list so that it can help you in every job easier and faster.",
                buttonText = "Continue"
            ),
            OnboardingData(
                imageRes = R.drawable.onboarding_image_2,
                title = "Organize your tasks efficiently",
                description = "Keep track of your daily activities and never miss important deadlines with our intuitive task management system.",
                buttonText = "Continue"
            ),
            OnboardingData(
                imageRes = R.drawable.onboarding_image_3,
                title = "Boost your productivity",
                description = "Complete your tasks faster with smart features designed to boost your productivity and simplify your workflow.",
                buttonText = "Get Started"
            )
        )

        override fun getItemCount(): Int = onboardingData.size

        override fun createFragment(position: Int): Fragment {
            val page = onboardingData[position]
            return OnboardingStepFragment.newInstance(
                imageRes = page.imageRes,
                title = page.title,
                description = page.description,
                buttonText = page.buttonText,
                onContinueClicked = { navigateToNext() },
            )
        }
    }

    private data class OnboardingData(
        val imageRes: Int,
        val title: String,
        val description: String,
        val buttonText: String
    )
} 