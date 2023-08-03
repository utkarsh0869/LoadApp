package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val originalButtonText = "Download"
    private var widthSize = 0
    private var heightSize = 0

    private var progress: Float = 0f

    private var valueAnimator = ValueAnimator()
    private var buttonText: String
    private var buttonBackgroundColor = R.attr.buttonBackgroundColor

    private var loadingButton : LoadingButton = findViewById(R.id.loading_button)

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            ButtonState.Loading -> {
                setText("We are Downloading")
                setBgColor("#004349")
                valueAnimator= ValueAnimator.ofFloat(0f, 1f).apply {
                    addUpdateListener {
                        progress = animatedValue as Float
                        invalidate()
                    }
                    repeatMode = ValueAnimator.REVERSE
                    repeatCount = ValueAnimator.INFINITE
                    duration = 3000
                    start()
                }
                disableLoadingButton()
            }

            ButtonState.Completed -> {
                setText("Downloaded")
                setBgColor("#07C2AA")
                valueAnimator.cancel()
                resetProgress()
                enableLoadingButton()
                resetButtonText()
            }

            ButtonState.Clicked -> {
            }
        }
        invalidate()
    }

    private fun resetButtonText() {
        buttonText = originalButtonText
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
                buttonText = originalButtonText
                buttonBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimary)
            } finally {
                recycle()
            }
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val buttonWidth = width.toFloat()
        val buttonHeight = height.toFloat()

        //Draw a rectangle as the button background
        paint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        canvas?.drawRect(0f, 0f, buttonWidth, buttonHeight, paint)

        //Draw text in the center of the button
        paint.color = ContextCompat.getColor(context, R.color.white)

        val textX = buttonWidth / 2f
        val textY = buttonHeight / 2f - (paint.descent() + paint.ascent()) / 2f
        canvas?.drawText(buttonText, textX, textY, paint)

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

    private fun setText(buttonText: String) {
        this.buttonText = buttonText
        invalidate()
        requestLayout()
    }

    private fun setBgColor(backgroundColor: String) {
        buttonBackgroundColor = Color.parseColor(backgroundColor)
        invalidate()
        requestLayout()
    }

    fun setLoadingButtonState(state: ButtonState) {
        buttonState = state
    }

    private fun disableLoadingButton() {
        loadingButton.isEnabled = false
    }

    private fun resetProgress() {
        progress = 0f
    }
    private fun enableLoadingButton() {
        loadingButton.isEnabled = true
    }
}