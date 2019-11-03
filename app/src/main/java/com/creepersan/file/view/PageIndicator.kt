package com.creepersan.file.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.creepersan.file.R
import com.creepersan.file.manager.TypedValueManager
import java.util.ArrayList

class PageIndicator : View{
    private var position = 0F
    private var count = 0
    private var dotDistance = TypedValueManager.dp2px(20f);
    private var dotRadius = TypedValueManager.dp2px(5f)
    private var activeDotRadius = TypedValueManager.dp2px(6f)
    private var dotColor = Color.parseColor("#FFFFFFFF")
    private var activeDotColor = Color.parseColor("#99FFFFFF")
    private val drawableList = ArrayList<Int>()

    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context:Context, attrs:AttributeSet?, resID:Int):super(context, attrs, resID){
        attrs?.apply {
            val typedArray = context.obtainStyledAttributes(this, R.styleable.PageIndicator)
            position = typedArray.getFloat(R.styleable.PageIndicator_position, position)
            count = typedArray.getInteger(R.styleable.PageIndicator_count, count)
            dotDistance = typedArray.getDimensionPixelSize(R.styleable.PageIndicator_dotDistance, dotDistance.toInt()).toFloat()
            dotRadius = typedArray.getDimensionPixelSize(R.styleable.PageIndicator_dotRadius, dotRadius.toInt()).toFloat()
            activeDotRadius = typedArray.getDimensionPixelSize(R.styleable.PageIndicator_activeDotRadius, activeDotRadius.toInt()).toFloat()
            dotColor = typedArray.getColor(R.styleable.PageIndicator_dotColor, dotColor)
            activeDotColor = typedArray.getColor(R.styleable.PageIndicator_activeDotColor, activeDotColor)
            typedArray.recycle()
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val radius = max(activeDotRadius, dotRadius).toInt()
        setMeasuredDimension(
            measureSpec(widthMeasureSpec, radius * 2 + ((count - 1) * dotDistance).toInt()),
            measureSpec(heightMeasureSpec, radius * 2)
        )
    }

    private fun measureSpec(measureSpec:Int, defSize:Int):Int{
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        return when(mode){
            MeasureSpec.AT_MOST -> defSize
            MeasureSpec.EXACTLY -> size
            MeasureSpec.UNSPECIFIED -> defSize
            else -> defSize
        }
    }

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        if (count < 1) return

        val paddingRect = RectF(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            canvas.width - paddingRight.toFloat(),
            canvas.height - paddingBottom.toFloat()
        )

        val centerX = paddingRect.top + paddingRect.height() / 2
        val centerY = paddingRect.left + paddingRect.width() / 2
        val radiusMax = max(activeDotRadius, dotRadius).toInt()
        val drawWidth = radiusMax * 2 + ((count - 1) * dotRadius).toInt()
        val drawHeight = radiusMax * 2
        val drawRect = RectF(
            centerX - drawWidth / 2,
            centerY - drawHeight / 2,
            centerX + drawWidth / 2,
            centerY + drawHeight / 2
        )

        // 绘制点
        var x = radiusMax.toFloat()
        paint.color = dotColor
        for (i in 0 until count){
            canvas.drawCircle(x, radiusMax.toFloat(), dotRadius, paint)
            x += dotDistance
        }

        // 绘制高亮点
        paint.color = activeDotColor
        canvas.drawCircle(radiusMax + position * dotDistance, radiusMax.toFloat(), dotRadius, paint)

    }

    fun setPosition(position : Float){
        this.position = position
        invalidate()
    }

    fun setCount(count:Int){
        this.count = count
        requestLayout()
    }

    private fun max(a:Float, b:Float):Float{
        return kotlin.math.max(a, b)
    }

}