package utility

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
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
                    .endsWith(".jpg") || filePath.lowercase(Locale.getDefault())
                    .endsWith(".jpeg") || filePath.lowercase(Locale.getDefault())
                    .endsWith(".png") || filePath.lowercase(Locale.getDefault())
                    .endsWith(".heic") || filePath.lowercase(Locale.getDefault()).endsWith(".webp")
            ) {
                intent.setDataAndType(contentUri, "image/*")
            } else if(filePath.lowercase(Locale.getDefault())
                    .endsWith(".apk")) {
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant permission to read the URI
            }else if (filePath.lowercase(Locale.getDefault())
                    .endsWith(".xls") || filePath.lowercase(
                    Locale.getDefault()
                ).endsWith(".xlsx")
                || filePath.lowercase(Locale.getDefault()).endsWith(".csv")
            ) {
                intent.setDataAndType(contentUri, "application/vnd.ms-excel")
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".pdf")) {
                intent.setDataAndType(contentUri, "application/pdf")
            } else if (filePath.lowercase(Locale.getDefault())
                    .endsWith(".doc") || filePath.lowercase(
                    Locale.getDefault()
                ).endsWith(".docx")
            ) {
                intent.setDataAndType(contentUri, "application/msword")
            } else if (filePath.lowercase(Locale.getDefault())
                    .endsWith(".ppt") || filePath.lowercase(
                    Locale.getDefault()
                ).endsWith(".pptx")
            ) {
                intent.setDataAndType(contentUri, "application/vnd.ms-powerpoint")
            } else if (filePath.lowercase(Locale.getDefault())
                    .endsWith(".zip") || filePath.lowercase(
                    Locale.getDefault()
                ).endsWith(".rar")
            ) {
                // WAV audio file
                intent.setDataAndType(contentUri, "application/x-wav")
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".rtf")) {
                // RTF file
                intent.setDataAndType(contentUri, "application/rtf")
            } else if (filePath.lowercase(Locale.getDefault())
                    .endsWith(".wav") || filePath.lowercase(
                    Locale.getDefault()
                ).endsWith(".mp3")
            ) {
                // WAV audio file
                intent.setDataAndType(contentUri, "audio/x-wav")
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".gif")) {
                // GIF file
                intent.setDataAndType(contentUri, "image/gif")
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".txt")) {
                intent.setDataAndType(contentUri, "text/plain")
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".xml")) {
                intent.setDataAndType(contentUri, "text/xml")
            } else if (filePath.lowercase(Locale.getDefault())
                    .endsWith(".xlsm") || filePath.lowercase(
                    Locale.getDefault()
                ).endsWith(".xlsb")
            ) {
                intent.setDataAndType(contentUri, "application/vnd.ms-excel")
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".msg")) {
                intent.setDataAndType(contentUri, "application/vnd.ms-outlook")
            } else if (filePath.lowercase(Locale.getDefault())
                    .endsWith(".3gp") || filePath.lowercase(
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
                || isVideoFile(filePath)
            ) {
                intent.setDataAndType(contentUri, "video/*")
            } else if (filePath.lowercase(Locale.getDefault())
                    .endsWith(".VSDX") || filePath.lowercase(
                    Locale.getDefault()
                ).endsWith(".vsdx")
            ) {
                intent.setDataAndType(contentUri, "application/x-visio")
            } else if (filePath.lowercase(Locale.getDefault())
                    .endsWith(".dwt") || filePath.lowercase(
                    Locale.getDefault()
                ).endsWith(".dwg")
            ) {
                intent.setDataAndType(contentUri, "application/acad")
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".dxf")) {
                intent.setDataAndType(contentUri, "application/dxf")
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".lzh")) {
                intent.setDataAndType(contentUri, "application/octet-stream")
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".pptm")) {
                intent.setDataAndType(
                    contentUri,
                    "application/vnd.ms-powerpoint.presentation.macroEnabled.12"
                )
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".bmp")) {
                // bmp file
                intent.setDataAndType(contentUri, "image/bmp")
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".Dgn")) {
                // Dgn file
                intent.setDataAndType(contentUri, "image/vnd.dgn")
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".stl")) {
                // stl file,application/vnd.ms-pki.stl,application/x-navistyle
                intent.setDataAndType(contentUri, "application/sla")
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".dwx")) {
                // dwx file
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".docm")) {
                // docm file
                intent.setDataAndType(
                    contentUri,
                    "application/vnd.ms-word.document.macroEnabled.12"
                )
            } else if (filePath.lowercase(Locale.getDefault())
                    .endsWith(".tif") || filePath.lowercase(
                    Locale.getDefault()
                ).endsWith(".tiff")
            ) {
                // tif file,image/x-tiffm
                intent.setDataAndType(contentUri, "image/tiff")
            } else if (filePath.lowercase(Locale.getDefault()).endsWith(".psd")) {
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
        if (file.isDirectory) {
            return Constants.DIRECTORY
        } else {
            try {
                if (filePath.lowercase(Locale.getDefault())
                        .endsWith(".jpg") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".jpeg") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".png") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".Dgn") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".bmp") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".heic") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".webp")

                ) {
                    return Constants.PICTURES
                } else if (filePath.lowercase(Locale.getDefault())
                        .endsWith(".xls") || filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".xlsx")
                    || filePath.lowercase(Locale.getDefault())
                        .endsWith(".csv") || filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".pdf") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".doc") || filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".docx") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".VSDX") || filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".vsdx") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".dwt") || filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".dwg") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".dxf") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".lzh") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".pptm") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".stl") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".dwx") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".docm") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".tif") || filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".tiff") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".psd") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".txt") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".xml") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".xlsm") || filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".xlsb") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".msg") || filePath.lowercase(Locale.getDefault())
                        .endsWith(".ppt") || filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".pptx") || filePath.lowercase(Locale.getDefault()).endsWith(".rtf")
                ) {
                    return Constants.DOCUMENT
                } else if (filePath.lowercase(Locale.getDefault())
                        .endsWith(".zip") || filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".rar") || filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".jar")
                ) {
                    // WAV audio file
                    return Constants.ZIP
                } else if (filePath.lowercase(Locale.getDefault())
                        .endsWith(".wav") ||
                    filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".mp3")
                    ||filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".aac")
                ) {
                    // WAV audio file
                    return Constants.AUDIO
                } else if (filePath.lowercase(Locale.getDefault())
                        .endsWith(".3gp") || filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".mpg")
                    || filePath.lowercase(Locale.getDefault())
                        .endsWith(".mpeg") || filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".mpe")
                    || filePath.lowercase(Locale.getDefault())
                        .endsWith(".mp4") || filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".avi")
                    || filePath.lowercase(Locale.getDefault())
                        .endsWith(".mov") || filePath.lowercase(
                        Locale.getDefault()
                    ).endsWith(".ogg")
                    || isVideoFile(filePath) || filePath.lowercase(Locale.getDefault())
                        .endsWith(".gif")
                ) {
                    return Constants.VIDEO
                } else if(filePath.lowercase(Locale.getDefault())
                        .endsWith(".apk")) {
                    return Constants.APP
                } else {
                    return Constants.UNIDENTIFIED
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