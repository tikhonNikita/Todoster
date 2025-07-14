package com.example.todoster.ui.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.todoster.R
import com.example.todoster.core.ui.components.StepperIndicatorView

class OnboardingViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val viewPager: ViewPager2
    private val stepperIndicator: StepperIndicatorView
    
    private var onPageChangeListener: OnPageChangeListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.onboarding_viewpager, this, true)
        
        viewPager = findViewById(R.id.viewpager)
        stepperIndicator = findViewById(R.id.stepper_indicator)
        
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.OnboardingViewPager, defStyleAttr, 0
        )
        
        val activeColor = typedArray.getColor(
            R.styleable.OnboardingViewPager_activeColor,
            ContextCompat.getColor(context, com.example.todoster.core.ui.R.color.md_theme_primary)
        )
        val inactiveColor = typedArray.getColor(
            R.styleable.OnboardingViewPager_inactiveColor,
            ContextCompat.getColor(context, com.example.todoster.core.ui.R.color.md_theme_primaryContainer)
        )
        val activeWidth = typedArray.getDimension(
            R.styleable.OnboardingViewPager_activeWidth,
            25f.dpToPx()
        )
        val inactiveWidth = typedArray.getDimension(
            R.styleable.OnboardingViewPager_inactiveWidth,
            8f.dpToPx()
        )
        val indicatorHeight = typedArray.getDimension(
            R.styleable.OnboardingViewPager_indicatorHeight,
            8f.dpToPx()
        )
        val indicatorSpacing = typedArray.getDimension(
            R.styleable.OnboardingViewPager_indicatorSpacing,
            10f.dpToPx()
        )
        val cornerRadius = typedArray.getDimension(
            R.styleable.OnboardingViewPager_cornerRadius,
            4f.dpToPx()
        )
        val touchPadding = typedArray.getDimension(
            R.styleable.OnboardingViewPager_touchPadding,
            8f.dpToPx()
        )
        
        typedArray.recycle()
        
        stepperIndicator.setColors(activeColor, inactiveColor)
        stepperIndicator.setDimensions(activeWidth, inactiveWidth, indicatorHeight, indicatorSpacing, cornerRadius, touchPadding)
        
        setupViewPagerCallback()
    }
    
    private fun setupViewPagerCallback() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                stepperIndicator.updateScrollPosition(position, positionOffset)
                onPageChangeListener?.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
            
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                onPageChangeListener?.onPageSelected(position)
            }
            
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                onPageChangeListener?.onPageScrollStateChanged(state)
            }
        })
        
        stepperIndicator.setOnStepClickListener { stepIndex ->
            viewPager.currentItem = stepIndex
        }
    }
    
    fun setAdapter(adapter: FragmentStateAdapter) {
        viewPager.adapter = adapter
        stepperIndicator.setupSteps(adapter.itemCount)
    }
    
    fun setCurrentItem(item: Int, smoothScroll: Boolean = true) {
        viewPager.setCurrentItem(item, smoothScroll)
    }
    
    fun getCurrentItem(): Int = viewPager.currentItem
    
    fun setOnPageChangeListener(listener: OnPageChangeListener) {
        onPageChangeListener = listener
    }
    
    fun setStepClickable(clickable: Boolean) {
        stepperIndicator.isEnabled = clickable
    }
    
    private fun Float.dpToPx(): Float {
        return this * context.resources.displayMetrics.density
    }
    
    interface OnPageChangeListener {
        fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        fun onPageSelected(position: Int) {}
        fun onPageScrollStateChanged(state: Int) {}
    }
} 