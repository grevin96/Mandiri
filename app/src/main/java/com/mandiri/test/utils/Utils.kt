package com.mandiri.test.utils

import android.content.Context

class Utils {
    companion object {
        fun gap(context: Context, value: Int) = (value * context.resources.displayMetrics.density).toInt()
    }
}