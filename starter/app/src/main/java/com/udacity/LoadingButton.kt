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
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }


    init {

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val buttonWidth = width.toFloat()
        val buttonHeight = height.toFloat()

        //Draw a rectangle as the button background
        val buttonBackgroundPaint = Paint()
        buttonBackgroundPaint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        canvas?.drawRect(0f, 0f, buttonWidth, buttonHeight, buttonBackgroundPaint)

        //Draw text in the center of the button
        val buttonTextPaint = Paint()
        buttonTextPaint.color = ContextCompat.getColor(context, R.color.white)
        buttonTextPaint.textSize = 32f
        buttonTextPaint.textAlign = Paint.Align.CENTER

        val textX = buttonWidth / 2f
        val textY = buttonHeight / 2f - (buttonTextPaint.descent() + buttonTextPaint.ascent()) / 2f
        canvas?.drawText("Download", textX, textY, buttonTextPaint)

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

}