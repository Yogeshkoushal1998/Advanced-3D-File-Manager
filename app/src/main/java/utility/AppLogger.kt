package utility

import android.util.Log


object AppLogger {
    private const val DEBUG = true
    private const val TAG_PREFIX = "@3D File Manager: "

    fun v(tag: String?, string: String) {
        if (DEBUG) {
            Log.v(tag, TAG_PREFIX + string)
        }
    }

    fun d(tag: String?, string: String) {
        if (DEBUG) {
            Log.d(tag, TAG_PREFIX + string)
        }
    }

    fun e(tag: String?, string: String) {
        if (DEBUG) {
            Log.e(tag, TAG_PREFIX + string)
        }
    }

    fun i(tag: String?, string: String) {
        if (DEBUG) {
            Log.i(tag, TAG_PREFIX + string)
        }
    }

    fun printStackTrace(ex: Exception) {
        if (DEBUG) {
            ex.printStackTrace()
        }
    }

    fun w(tag: String?, string: String) {
        if (DEBUG) {
            Log.w(tag, TAG_PREFIX + string)
        }
    }

    fun ex(ex: Exception) {
        if (DEBUG) {
            Log.d("", "=====================================================================\n")
            ex.printStackTrace()
        }
    }
}
