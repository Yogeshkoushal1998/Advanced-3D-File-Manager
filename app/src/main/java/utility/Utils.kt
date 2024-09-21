package utility

import android.content.Context
import com.yk.mobile.advanced3dfilemanager.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class Utils @Inject constructor(@ApplicationContext val context: Context) {

    fun getFormatedDate(timestamp: Long, dateFormat: String?): String {
        val date = Date(timestamp)
        val sdf = SimpleDateFormat(dateFormat, Locale("en"))
        return sdf.format(date)
    }

    fun getFileExtensionIcon(type: String?): Int {
        var resId: Int = R.drawable.ic_unknown
        if (type != null) {
            resId = context.getResources().getIdentifier(
                "ic_neu_" + type.lowercase(Locale.getDefault()), "drawable",
                context.getPackageName()
            )
            if (resId == 0) {
                resId = R.drawable.ic_unknown
            }
        }
        return resId
    }

}