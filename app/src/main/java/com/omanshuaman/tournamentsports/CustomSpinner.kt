package com.omanshuaman.tournamentsports

import android.content.Context
import androidx.appcompat.widget.AppCompatSpinner
import android.widget.Spinner
import com.omanshuaman.tournamentsports.CustomSpinner.OnSpinnerEventsListener
import android.content.res.Resources.Theme
import android.util.AttributeSet

/********************************************
 * Created by DailyCoding on 15-May-21.  *
 */
class CustomSpinner : AppCompatSpinner {
    interface OnSpinnerEventsListener {
        fun onPopupWindowOpened(spinner: Spinner?)
        fun onPopupWindowClosed(spinner: Spinner?)
    }

    private var mListener: OnSpinnerEventsListener? = null
    private var mOpenInitiated = false

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context, mode: Int) : super(context, mode) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, mode: Int) : super(
        context,
        attrs,
        defStyleAttr,
        mode
    ) {
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        mode: Int,
        popupTheme: Theme?
    ) : super(context, attrs, defStyleAttr, mode, popupTheme) {
    }

    override fun performClick(): Boolean {
        mOpenInitiated = true
        if (mListener != null) {
            mListener!!.onPopupWindowOpened(this)
        }
        return super.performClick()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasBeenOpened() && hasFocus) {
            performClosedEvent()
        }
    }

    /**
     * Register the listener which will listen for events.
     */
    fun setSpinnerEventsListener(
        onSpinnerEventsListener: OnSpinnerEventsListener?
    ) {
        mListener = onSpinnerEventsListener
    }

    /**
     * Propagate the closed Spinner event to the listener from outside if needed.
     */
    fun performClosedEvent() {
        mOpenInitiated = false
        if (mListener != null) {
            mListener!!.onPopupWindowClosed(this)
        }
    }

    /**
     * A boolean flag indicating that the Spinner triggered an open event.
     *
     * @return true for opened Spinner
     */
    fun hasBeenOpened(): Boolean {
        return mOpenInitiated
    }
}