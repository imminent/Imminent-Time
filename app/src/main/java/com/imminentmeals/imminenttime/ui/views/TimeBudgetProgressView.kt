package com.imminentmeals.imminenttime.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import com.imminentmeals.imminenttime.R
import kotlinx.android.synthetic.main.widget_time_budget_progress_view.view.*
import kotlin.properties.Delegates

class TimeBudgetProgressView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var progress: Float by Delegates.vetoable(0f) { _, _, new ->
        if (new < 0f || new > 1f) {
            return@vetoable false
        } else {
            progressDescription.text = "$new"
            progressDrawable.progress = new
            return@vetoable true
        }
    }
    @get:ColorInt
    inline var color
        get() = progressDrawable.color
        set(@ColorInt value) {
            progressDrawable.color = value
        }
    @PublishedApi
    internal val progressDrawable = ProgressDrawable()

    init {
        inflate(context, R.layout.widget_time_budget_progress_view, this)
        progressBar.background = progressDrawable
        if (isInEditMode) {
            color = ContextCompat.getColor(context, R.color.colorAccent)
            progress = 0.47f
        }
    }

    class ProgressDrawable : Drawable() {
        private var paintBrush = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            color = this@ProgressDrawable.color
        }
        @ColorInt
        var color: Int = Color.TRANSPARENT
        var progress: Float by Delegates.observable(0f) { _, _, _ ->
            invalidateSelf()
        }

        override fun draw(canvas: Canvas) {
            // Draws the progress completed
            canvas.drawRect(
                    bounds.left.toFloat(),
                    bounds.top.toFloat(),
                    bounds.right * progress,
                    bounds.bottom.toFloat(),
                    paintBrush
            )
            // Draws the amount remaining
            paintBrush.alpha.let { originalAlpha ->
                paintBrush.alpha /= 2
                canvas.drawRect(
                        bounds.right * progress,
                        bounds.top.toFloat(),
                        bounds.right.toFloat(),
                        bounds.bottom.toFloat(),
                        paintBrush
                )
                paintBrush.alpha = originalAlpha
            }
        }

        override fun setAlpha(alpha: Int) {
            paintBrush.alpha = alpha
        }

        override fun getOpacity() = 1 - paintBrush.alpha

        override fun setColorFilter(filter: ColorFilter?) {
            filter.let { paintBrush.colorFilter = filter }
        }

    }
}