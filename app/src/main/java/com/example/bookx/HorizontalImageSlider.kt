package com.example.bookx

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

/**
 * A horizontal image slider that shows multiple images at once
 * with proper constructor overloads to avoid XML inflation errors
 */
class HorizontalImageSlider : RecyclerView {
    // Primary constructor
    constructor(context: Context) : super(context) {
        init()
    }

    // Constructor called when used in XML with attributes
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    // Constructor called when used in XML with style
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private val snapHelper = PagerSnapHelper()
    private var itemWidth: Int = 0
    private var onSlideChangeListener: ((Int) -> Unit)? = null
    private var currentPosition: Int = 0
    private var startX: Float = 0f
    private var isDragging = false

    private fun init() {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(this)
        setupScrollListener()
        calculateItemWidth()
    }

    private fun calculateItemWidth() {
        // Calculate item width to show 3 items at once
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        itemWidth = (screenWidth / 3.2).toInt() // Slightly less than exactly 3 to show a peek of the next item
    }

    fun setSlides(slides: List<SliderItem>, onItemClick: (Int) -> Unit = {}) {
        adapter = ImageSliderAdapter(slides, itemWidth) { position ->
            onItemClick(position)
        }
    }

    fun setOnSlideChangeListener(listener: (Int) -> Unit) {
        onSlideChangeListener = listener
    }

    private fun setupScrollListener() {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    val centerView = snapHelper.findSnapView(layoutManager)
                    centerView?.let {
                        val position = (layoutManager as LinearLayoutManager).getPosition(it)
                        if (position != currentPosition) {
                            currentPosition = position
                            onSlideChangeListener?.invoke(currentPosition)
                        }
                    }
                }
            }
        })
    }

    // Smooth scrolling to position with animation
    fun scrollToSlide(position: Int, smooth: Boolean = true) {
        if (smooth) {
            smoothScrollToPosition(position)
        } else {
            scrollToPosition(position)
        }
        currentPosition = position
    }

    // Override onTouchEvent to detect manual sliding
    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = e.x
                isDragging = false
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = e.x - startX
                if (Math.abs(deltaX) > 30) { // Threshold to consider it a drag
                    isDragging = true
                }
            }
        }
        return super.onTouchEvent(e)
    }
}

// Keep this class in the same file or create a separate file
data class SliderItem(
    val imageResId: Int,
    val name: String,
    val price: Double,
    val description: String
) : Serializable