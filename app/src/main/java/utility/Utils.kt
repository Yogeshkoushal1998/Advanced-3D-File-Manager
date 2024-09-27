package utility

import android.content.Context
import android.graphics.drawable.Drawable
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

    fun getApkIcon(context: Context, apkFilePath: String): Drawable? {
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageArchiveInfo(apkFilePath, 0)
        packageInfo?.let {
            it.applicationInfo.sourceDir = apkFilePath
            it.applicationInfo.publicSourceDir = apkFilePath
            return it.applicationInfo.loadIcon(packageManager)
        }
        return null
    }


}