package com.example.todoster.core.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.todoster.core.ui.R

class StepperIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var activeColor = ContextCompat.getColor(context, R.color.md_theme_primary)
    private var inactiveColor = ContextCompat.getColor(context, R.color.md_theme_primaryContainer)

    private var activeWidth = 25f.dpToPx()
    private var inactiveWidth = 8f.dpToPx()
    private var indicatorHeight = 8f.dpToPx()
    private var spacing = 10f.dpToPx()
    private var cornerRadius = 4f.dpToPx()
    private var touchPadding = 8f.dpToPx()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()

    private var totalSteps = 3
    private var currentStep = 0
    private var animatedWidths = FloatArray(0)

    private val stepBounds = mutableListOf<RectF>()
    private var onStepClickListener: ((Int) -> Unit)? = null

    init {
        setupSteps(totalSteps)
    }

    fun setColors(activeColor: Int, inactiveColor: Int) {
        this.activeColor = activeColor
        this.inactiveColor = inactiveColor
    }

    fun setDimensions(
        activeWidth: Float,
        inactiveWidth: Float,
        indicatorHeight: Float,
        spacing: Float,
        cornerRadius: Float,
        touchPadding: Float
    ) {
        this.activeWidth = activeWidth
        this.inactiveWidth = inactiveWidth
        this.indicatorHeight = indicatorHeight
        this.spacing = spacing
        this.cornerRadius = cornerRadius
        this.touchPadding = touchPadding

        animatedWidths = FloatArray(totalSteps) { if (it == currentStep) activeWidth else inactiveWidth }
        updateStepBounds()
    }

    fun setupSteps(steps: Int) {
        totalSteps = steps
        animatedWidths = FloatArray(steps) { if (it == currentStep) activeWidth else inactiveWidth }
        updateStepBounds()
        requestLayout()
        invalidate()
    }

    fun setOnStepClickListener(listener: (Int) -> Unit) {
        onStepClickListener = listener
    }

    fun updateScrollPosition(position: Int, positionOffset: Float) {
        if (position < 0 || position >= totalSteps) return

        currentStep =
            if (positionOffset > 0.5f && position + 1 < totalSteps) position + 1 else position

        for (i in 0 until totalSteps) {
            animatedWidths[i] = when {
                i == position -> {
                    activeWidth + (inactiveWidth - activeWidth) * positionOffset
                }

                i == position + 1 && position + 1 < totalSteps -> {
                    inactiveWidth + (activeWidth - inactiveWidth) * positionOffset
                }

                else -> inactiveWidth
            }
        }

        updateStepBounds()
        invalidate()
    }

    private fun updateStepBounds() {
        stepBounds.clear()
        var currentX = 0f

        for (i in 0 until totalSteps) {
            val width = animatedWidths[i]

            stepBounds.add(
                RectF(
                    currentX - touchPadding,
                    -touchPadding,
                    currentX + width + touchPadding,
                    indicatorHeight + touchPadding
                )
            )

            currentX += width + spacing
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val activeStepWidth = activeWidth
        val inactiveStepsCount = totalSteps - 1
        val inactiveStepsWidth = inactiveStepsCount * inactiveWidth
        val totalSpacingWidth = inactiveStepsCount * spacing

        val totalWidth = (activeStepWidth + inactiveStepsWidth + totalSpacingWidth).toInt()
        val height = indicatorHeight.toInt()

        setMeasuredDimension(totalWidth, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var currentX = 0f

        for (i in 0 until totalSteps) {
            val width = animatedWidths[i]
            val isActive = i == currentStep

            paint.color = if (isActive) activeColor else inactiveColor

            rectF.set(
                currentX,
                0f,
                currentX + width,
                indicatorHeight
            )

            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)

            currentX += width + spacing
        }
    }

    private fun Float.dpToPx(): Float {
        return this * context.resources.displayMetrics.density
    }
}