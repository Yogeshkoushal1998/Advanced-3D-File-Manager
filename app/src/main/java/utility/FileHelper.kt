package utility

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.Locale
import javax.inject.Inject

class FileHelper @Inject constructor(@ApplicationContext val mContext: Context) {


    fun openFile(filePath: String) {
        val file = File(filePath)
        val intent = Intent(Intent.ACTION_VIEW)
        val contentUri: Uri
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                contentUri =
                    FileProvider.getUriForFile(mContext, mContext.packageName + ".provider", file)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                contentUri = Uri.fromFile(file)
            }

            if (filePath.lowercase(Locale.getDefault())
                    .contains(".jpg") || filePath.lowercase(Locale.getDefault())
                    .contains(".jpeg") || filePath.lowercase(Locale.getDefault()).contains(".png")
            ) {
                intent.setDataAndType(contentUri, "image/*")
            } else if (filePath.lowercase(Locale.getDefault())
                    .contains(".xls") || filePath.lowercase(
                    Locale.getDefault()
                ).contains(".xlsx")
                || filePath.lowercase(Locale.getDefault()).contains(".csv")
            ) {
                intent.setDataAndType(contentUri, "application/vnd.ms-excel")
            } else if (filePath.lowercase(Locale.getDefault()).contains(".pdf")) {
                intent.setDataAndType(contentUri, "application/pdf")
            } else if (filePath.lowercase(Locale.getDefault())
                    .contains(".doc") || filePath.lowercase(
                    Locale.getDefault()
                ).contains(".docx")
            ) {
                intent.setDataAndType(contentUri, "application/msword")
            } else if (filePath.lowercase(Locale.getDefault())
                    .contains(".ppt") || filePath.lowercase(
                    Locale.getDefault()
                ).contains(".pptx")
            ) {
                intent.setDataAndType(contentUri, "application/vnd.ms-powerpoint")
            } else if (filePath.lowercase(Locale.getDefault())
                    .contains(".zip") || filePath.lowercase(
                    Locale.getDefault()
                ).contains(".rar")
            ) {
                // WAV audio file
                intent.setDataAndType(contentUri, "application/x-wav")
            } else if (filePath.lowercase(Locale.getDefault()).contains(".rtf")) {
                // RTF file
                intent.setDataAndType(contentUri, "application/rtf")
            } else if (filePath.lowercase(Locale.getDefault())
                    .contains(".wav") || filePath.lowercase(
                    Locale.getDefault()
                ).contains(".mp3")
            ) {
                // WAV audio file
                intent.setDataAndType(contentUri, "audio/x-wav")
            } else if (filePath.lowercase(Locale.getDefault()).contains(".gif")) {
                // GIF file
                intent.setDataAndType(contentUri, "image/gif")
            } else if (filePath.lowercase(Locale.getDefault()).contains(".txt")) {
                intent.setDataAndType(contentUri, "text/plain")
            } else if (filePath.lowercase(Locale.getDefault()).contains(".xml")) {
                intent.setDataAndType(contentUri, "text/xml")
            } else if (filePath.lowercase(Locale.getDefault())
                    .contains(".xlsm") || filePath.lowercase(
                    Locale.getDefault()
                ).contains(".xlsb")
            ) {
                intent.setDataAndType(contentUri, "application/vnd.ms-excel")
            } else if (filePath.lowercase(Locale.getDefault()).contains(".msg")) {
                intent.setDataAndType(contentUri, "application/vnd.ms-outlook")
            } else if (filePath.lowercase(Locale.getDefault())
                    .contains(".3gp") || filePath.lowercase(
                    Locale.getDefault()
                ).contains(".mpg")
                || filePath.lowercase(Locale.getDefault()).contains(".mpeg") || filePath.lowercase(
                    Locale.getDefault()
                ).contains(".mpe")
                || filePath.lowercase(Locale.getDefault()).contains(".mp4") || filePath.lowercase(
                    Locale.getDefault()
                ).contains(".avi")
                || filePath.lowercase(Locale.getDefault()).contains(".mov") || filePath.lowercase(
                    Locale.getDefault()
                ).contains(".ogg")
                || isVideoFile(filePath)
            ) {
                intent.setDataAndType(contentUri, "video/*")
            } else if (filePath.lowercase(Locale.getDefault())
                    .contains(".VSDX") || filePath.lowercase(
                    Locale.getDefault()
                ).contains(".vsdx")
            ) {
                intent.setDataAndType(contentUri, "application/x-visio")
            } else if (filePath.lowercase(Locale.getDefault())
                    .contains(".dwt") || filePath.lowercase(
                    Locale.getDefault()
                ).contains(".dwg")
            ) {
                intent.setDataAndType(contentUri, "application/acad")
            } else if (filePath.lowercase(Locale.getDefault()).contains(".dxf")) {
                intent.setDataAndType(contentUri, "application/dxf")
            } else if (filePath.lowercase(Locale.getDefault()).contains(".lzh")) {
                intent.setDataAndType(contentUri, "application/octet-stream")
            } else if (filePath.lowercase(Locale.getDefault()).contains(".pptm")) {
                intent.setDataAndType(
                    contentUri,
                    "application/vnd.ms-powerpoint.presentation.macroEnabled.12"
                )
            } else if (filePath.lowercase(Locale.getDefault()).contains(".bmp")) {
                // bmp file
                intent.setDataAndType(contentUri, "image/bmp")
            } else if (filePath.lowercase(Locale.getDefault()).contains(".Dgn")) {
                // Dgn file
                intent.setDataAndType(contentUri, "image/vnd.dgn")
            } else if (filePath.lowercase(Locale.getDefault()).contains(".stl")) {
                // stl file,application/vnd.ms-pki.stl,application/x-navistyle
                intent.setDataAndType(contentUri, "application/sla")
            } else if (filePath.lowercase(Locale.getDefault()).contains(".dwx")) {
                // dwx file
            } else if (filePath.lowercase(Locale.getDefault()).contains(".docm")) {
                // docm file
                intent.setDataAndType(
                    contentUri,
                    "application/vnd.ms-word.document.macroEnabled.12"
                )
            } else if (filePath.lowercase(Locale.getDefault())
                    .contains(".tif") || filePath.lowercase(
                    Locale.getDefault()
                ).contains(".tiff")
            ) {
                // tif file,image/x-tiffm
                intent.setDataAndType(contentUri, "image/tiff")
            } else if (filePath.lowercase(Locale.getDefault()).contains(".psd")) {
                // psd file
                intent.setDataAndType(contentUri, "application/octet-stream")
            } else {
                intent.setDataAndType(contentUri, "*/*")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mContext.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            //show toast
        } catch (e: Exception) {
            Log.d("Exception::", "Exception found")
        }
    }


    fun isVideoFile(filePath: String?): Boolean {
        if (filePath == null) {
            return false
        }
        return (filePath.lowercase(Locale.getDefault()).endsWith(".3gp") || filePath.lowercase(
            Locale.getDefault()
        ).endsWith(".mpg")
                || filePath.lowercase(Locale.getDefault()).endsWith(".mpeg") || filePath.lowercase(
            Locale.getDefault()
        ).endsWith(".mpe")
                || filePath.lowercase(Locale.getDefault()).endsWith(".mp4") || filePath.lowercase(
            Locale.getDefault()
        ).endsWith(".avi")
                || filePath.lowercase(Locale.getDefault()).endsWith(".mov") || filePath.lowercase(
            Locale.getDefault()
        ).endsWith(".ogg")
                || filePath.lowercase(Locale.getDefault()).endsWith(".webm") || filePath.lowercase(
            Locale.getDefault()
        ).endsWith(".mp2")
                || filePath.lowercase(Locale.getDefault()).endsWith(".mpv") || filePath.lowercase(
            Locale.getDefault()
        ).endsWith(".m4p")
                || filePath.lowercase(Locale.getDefault()).endsWith(".m4v") || filePath.lowercase(
            Locale.getDefault()
        ).endsWith(".wmv"))
    }


    fun getFileType(file: File): String {
        val filePath = file.path
        if(file.isDirectory){
            return Constants.DIRECTORY
        }else{
            try {
                if (filePath.lowercase(Locale.getDefault())
                        .contains(".jpg") || filePath.lowercase(Locale.getDefault())
                        .contains(".jpeg") || filePath.lowercase(Locale.getDefault())
                        .contains(".png") || filePath.lowercase(Locale.getDefault())
                        .contains(".Dgn") || filePath.lowercase(Locale.getDefault()).contains(".bmp")
                ) {
                    return Constants.IMAGE
                } else if (filePath.lowercase(Locale.getDefault())
                        .contains(".xls") || filePath.lowercase(
                        Locale.getDefault()
                    ).contains(".xlsx")
                    || filePath.lowercase(Locale.getDefault()).contains(".csv") || filePath.lowercase(
                        Locale.getDefault()
                    ).contains(".pdf") || filePath.lowercase(Locale.getDefault())
                        .contains(".doc") || filePath.lowercase(
                        Locale.getDefault()
                    ).contains(".docx") || filePath.lowercase(Locale.getDefault())
                        .contains(".VSDX") || filePath.lowercase(
                        Locale.getDefault()
                    ).contains(".vsdx") || filePath.lowercase(Locale.getDefault())
                        .contains(".dwt") || filePath.lowercase(
                        Locale.getDefault()
                    ).contains(".dwg") || filePath.lowercase(Locale.getDefault())
                        .contains(".dxf") || filePath.lowercase(Locale.getDefault())
                        .contains(".lzh") || filePath.lowercase(Locale.getDefault())
                        .contains(".pptm") || filePath.lowercase(Locale.getDefault())
                        .contains(".stl") || filePath.lowercase(Locale.getDefault())
                        .contains(".dwx") || filePath.lowercase(Locale.getDefault())
                        .contains(".docm") || filePath.lowercase(Locale.getDefault())
                        .contains(".tif") || filePath.lowercase(
                        Locale.getDefault()
                    ).contains(".tiff") || filePath.lowercase(Locale.getDefault())
                        .contains(".psd") || filePath.lowercase(Locale.getDefault())
                        .contains(".txt") || filePath.lowercase(Locale.getDefault())
                        .contains(".xml") || filePath.lowercase(Locale.getDefault())
                        .contains(".xlsm") || filePath.lowercase(
                        Locale.getDefault()
                    ).contains(".xlsb") || filePath.lowercase(Locale.getDefault())
                        .contains(".msg") || filePath.lowercase(Locale.getDefault())
                        .contains(".ppt") || filePath.lowercase(
                        Locale.getDefault()
                    ).contains(".pptx") || filePath.lowercase(Locale.getDefault()).contains(".rtf")
                ) {
                    return Constants.DOCUMENT
                } else if (filePath.lowercase(Locale.getDefault())
                        .contains(".zip") || filePath.lowercase(
                        Locale.getDefault()
                    ).contains(".rar")
                ) {
                    // WAV audio file
                    return Constants.ZIP
                } else if (filePath.lowercase(Locale.getDefault())
                        .contains(".wav") || filePath.lowercase(
                        Locale.getDefault()
                    ).contains(".mp3")
                ) {
                    // WAV audio file
                    return Constants.AUDIO
                } else if (filePath.lowercase(Locale.getDefault())
                        .contains(".3gp") || filePath.lowercase(
                        Locale.getDefault()
                    ).contains(".mpg")
                    || filePath.lowercase(Locale.getDefault()).contains(".mpeg") || filePath.lowercase(
                        Locale.getDefault()
                    ).contains(".mpe")
                    || filePath.lowercase(Locale.getDefault()).contains(".mp4") || filePath.lowercase(
                        Locale.getDefault()
                    ).contains(".avi")
                    || filePath.lowercase(Locale.getDefault()).contains(".mov") || filePath.lowercase(
                        Locale.getDefault()
                    ).contains(".ogg")
                    || isVideoFile(filePath)||filePath.lowercase(Locale.getDefault()).contains(".gif")
                ) {
                    return Constants.VIDEO
                } else {
                    return Constants.DOCUMENT
                }
            } catch (e: ActivityNotFoundException) {
                //show toast
                Log.d("Exception::", "Exception found")
            } catch (e: Exception) {
                Log.d("Exception::", "Exception found")
            }
        }


        return ""
    }

}