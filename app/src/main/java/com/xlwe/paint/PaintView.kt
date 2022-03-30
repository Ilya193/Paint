package com.xlwe.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PaintView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.GREEN
        strokeWidth = width
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private var drawPath = Path()
    private var sx = 0f
    private var sy = 0f

    private var width = Constants.PROGRESS.toFloat()
    var color = Color.GREEN

    private val pathList = mutableListOf<Path>()
    private val strokeWidthList = mutableListOf<Float>()
    private val colorList = mutableListOf<Int>()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                start(touchX!!, touchY!!)
            }
            MotionEvent.ACTION_UP -> {
                up(touchX!!, touchY!!)
            }
            MotionEvent.ACTION_MOVE -> {
                move(touchX!!, touchY!!)
            }
        }

        invalidate()

        return true
    }

    private fun start(x: Float, y: Float) {
        drawPath = Path()
        pathList.add(drawPath)
        strokeWidthList.add(width)
        colorList.add(color)
        sx = x
        sy = y
        drawPath.moveTo(sx, sy)
    }

    private fun up(x: Float, y: Float) {
        drawPath.lineTo(x, y)
    }

    private fun move(x: Float, y: Float) {
        drawPath.quadTo(sx, sy, (x + sx) / 2, (y + sy) / 2)
        sx = x
        sy = y
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            for (i in 0 until pathList.size) {
                paint.strokeWidth = strokeWidthList[i]
                paint.color = colorList[i]
                drawPath(pathList[i], paint)
            }
        }
    }

    fun undo() {
        if (pathList.size > 0) {
            pathList.removeAt(pathList.size - 1)
            strokeWidthList.removeAt(strokeWidthList.size - 1)
            colorList.removeAt(colorList.size - 1)
        }

        invalidate()
    }

    fun reset() {
        pathList.clear()
        strokeWidthList.clear()
        colorList.clear()
        invalidate()
    }

    fun setStrokeWidth(progress: Int) {
        width = progress.toFloat()
    }
}