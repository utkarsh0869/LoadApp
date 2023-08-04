package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val textRect = Rect()

    private var animatedValue: Float = 0f

    private var valueAnimator = ValueAnimator()
    private var buttonText: String

    private var loadingButton : LoadingButton = findViewById(R.id.loading_button)

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            ButtonState.Loading -> {

                setButtonText(context.getString(R.string.we_are_downloading_text))

                valueAnimator = ValueAnimator.ofFloat(0f, 1f)
                valueAnimator.duration = 3000
                valueAnimator.repeatMode = ValueAnimator.REVERSE
                valueAnimator.repeatCount = ValueAnimator.INFINITE

                valueAnimator.addUpdateListener { animator ->
                    animatedValue = animator.animatedValue as Float
                    invalidate()
                }
                valueAnimator.start()
                disableLoadingButton()
            }

            ButtonState.Completed -> {

                setButtonText(context.getString(R.string.downloaded_text))
                valueAnimator.cancel()
                resetAnimatedValue()
                enableLoadingButton()
            }

            ButtonState.Clicked -> {
            }
        }
        invalidate()
    }

    fun resetButtonText() {
        buttonText = context.getString(R.string.original_text)
        invalidate()
        requestLayout()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
        color = Color.WHITE
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0, 0).apply {

            try {
                buttonText = context.getString(R.string.original_text)
            } finally {
                recycle()
            }
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val backgroundWidth = measuredWidth.toFloat()
        val backgroundHeight = measuredHeight.toFloat()

        paint.getTextBounds(buttonText, 0, buttonText.length, textRect)
        canvas?.drawRect(0f, 0f, backgroundWidth, backgroundHeight, backgroundPaint)

        if (buttonState == ButtonState.Loading) {
            var progressVal = animatedValue * measuredWidth.toFloat()
            canvas?.drawRect(0f, 0f, progressVal, backgroundHeight, inProgressBackgroundPaint)

            // Calculate the position to draw the circle on the right side of the button
            val circleRadius = measuredHeight.toFloat() / 4
            val circleX = measuredWidth.toFloat() - circleRadius * 2
            val circleY = measuredHeight.toFloat() / 2

            canvas?.drawArc(circleX - circleRadius, circleY - circleRadius,
                circleX + circleRadius, circleY + circleRadius,
                0f, progressVal, true, inProgressArcPaint)
        }
        val centerX = measuredWidth.toFloat() / 2
        val centerY = measuredHeight.toFloat() / 2 - textRect.centerY()

        canvas?.drawText(buttonText,centerX, centerY, paint)

    }

    private val inProgressArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private fun setButtonText(buttonText: String) {
        this.buttonText = buttonText
        invalidate()
        requestLayout()
    }

    fun setLoadingButtonState(state: ButtonState) {
        buttonState = state
    }

    private fun disableLoadingButton() {
        loadingButton.isEnabled = false
    }

    private fun resetAnimatedValue() {
        animatedValue = 0f
    }
    private fun enableLoadingButton() {
        loadingButton.isEnabled = true
    }

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.colorPrimary)
    }

    private val inProgressBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
    }
}
