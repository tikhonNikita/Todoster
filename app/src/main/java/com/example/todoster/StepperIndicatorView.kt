package com.example.todoster

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

class StepperIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val activeColor = ContextCompat.getColor(context, R.color.primary_light)
    private val inactiveColor = ContextCompat.getColor(context, R.color.primary_variant_light)

    private val activeWidth = 25f.dpToPx()
    private val inactiveWidth = 8f.dpToPx()
    private val indicatorHeight = 8f.dpToPx()
    private val spacing = 10f.dpToPx()
    private val cornerRadius = 4f.dpToPx()
    private val touchPadding = 8f.dpToPx()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()

    private var totalSteps = 3
    private var currentStep = 0
    private var animatedWidths = FloatArray(0)
    private var animator: ValueAnimator? = null

    private val stepBounds = mutableListOf<RectF>()
    private var onStepClickListener: ((Int) -> Unit)? = null

    init {
        setupSteps(totalSteps)
    }

    fun setupSteps(steps: Int) {
        totalSteps = steps
        animatedWidths = FloatArray(steps) { if (it == currentStep) activeWidth else inactiveWidth }
        updateStepBounds()
        requestLayout()
        invalidate()
    }

    fun setCurrentStep(step: Int, animate: Boolean = true) {
        if (step == currentStep || step < 0 || step >= totalSteps) return

        val oldStep = currentStep
        currentStep = step

        if (animate) {
            animateStepChange(oldStep, step)
        } else {
            for (i in 0 until totalSteps) {
                animatedWidths[i] = if (i == currentStep) activeWidth else inactiveWidth
            }
            updateStepBounds()
            invalidate()
        }
    }

    fun setOnStepClickListener(listener: (Int) -> Unit) {
        onStepClickListener = listener
    }

    private fun animateStepChange(fromStep: Int, toStep: Int) {
        animator?.cancel()

        val startWidths = animatedWidths.clone()
        val targetWidths =
            FloatArray(totalSteps) { if (it == toStep) activeWidth else inactiveWidth }

        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 300
            addUpdateListener { animation ->
                val progress = animation.animatedValue as Float

                for (i in 0 until totalSteps) {
                    animatedWidths[i] =
                        startWidths[i] + (targetWidths[i] - startWidths[i]) * progress
                }

                updateStepBounds()
                invalidate()
            }
        }
        animator?.start()
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val touchX = event.x
                val touchY = event.y

                for (i in stepBounds.indices) {
                    if (stepBounds[i].contains(touchX, touchY)) {
                        onStepClickListener?.invoke(i)
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
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