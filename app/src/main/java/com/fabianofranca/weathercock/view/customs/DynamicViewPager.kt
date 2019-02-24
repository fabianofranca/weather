package android.support.v4.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.fabianofranca.weathercock.R

class DynamicViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {

    private var _swipeOrientation: SwipeOrientation = Companion.SwipeOrientation.HORIZONTAL

    public var swipeOrientation: SwipeOrientation
        set(value) {
            _swipeOrientation = value
            initSwipeMethods()
        }
        get(){
            return _swipeOrientation
        }

    init {
        swipeOrientation(context, attrs)
    }

    private fun swipeOrientation(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicViewPager)

        _swipeOrientation = enumValues<SwipeOrientation>()[typedArray.getInteger(
            R.styleable.DynamicViewPager_swipe_orientation,
            0
        )]

        typedArray.recycle()
        initSwipeMethods()
    }

    private fun initSwipeMethods() {
        if (_swipeOrientation == Companion.SwipeOrientation.VERTICAL) {
            setPageTransformer(true, VerticalPageTransformer())
            overScrollMode = View.OVER_SCROLL_NEVER
        }
    }

    private fun swapXY(event: MotionEvent): MotionEvent {
        val newX = (event.y / height) * width
        val newY = (event.x / width) * height

        event.setLocation(newX, newY)

        return event
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return super.onTouchEvent(
            if (_swipeOrientation == Companion.SwipeOrientation.VERTICAL) swapXY(event) else event
        )
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        setCurrentItemInternal(item, smoothScroll, false, 50)
    }

    private class VerticalPageTransformer: ViewPager.PageTransformer {

        override fun transformPage(page: View, position: Float) {
            when {
                position < -1 -> page.alpha = 0F
                position <= 1 -> {
                    page.alpha = 1F

                    page.translationX = page.width * -position

                    val yPosition = position * page.height
                    page.translationY = yPosition
                }
                else -> page.alpha = 0F
            }
        }
    }

    companion object {
        enum class SwipeOrientation {
            HORIZONTAL,
            VERTICAL
        }
    }
}