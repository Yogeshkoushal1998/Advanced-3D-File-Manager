package com.yk.mobile.advanced3dfilemanager.repository

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.util.Locale
import javax.inject.Inject

class FilesSizeRepository @Inject constructor(@ApplicationContext private val context: Context) {
    val TAG = FilesSizeRepository::class.simpleName


    //MutableStateFlows
    private val _totalInternalSize = MutableStateFlow<Long>(0)
    private val _totalAvailableInternalSize = MutableStateFlow<Long>(0)
    private val _totalSdSize = MutableStateFlow<Long>(0)
    private val _totalAvailableSdSize = MutableStateFlow<Long>(0)
    private val _internalImageSize = MutableStateFlow<Long>(0)
    private val _internalAudioSize = MutableStateFlow<Long>(0)
    private val _internalVideoSize = MutableStateFlow<Long>(0)
    private val _internalZipsSize = MutableStateFlow<Long>(0)
    private val _internalAppsSize = MutableStateFlow<Long>(0)
    private val _internalDocumentSize = MutableStateFlow<Long>(0)
    private val _internalDownloadSize = MutableStateFlow<Long>(0)
    private val _sdImageSize = MutableStateFlow<Long>(0)
    private val _sdAudioSize = MutableStateFlow<Long>(0)
    private val _sdVideoSize = MutableStateFlow<Long>(0)
    private val _sdZipsSize = MutableStateFlow<Long>(0)
    private val _sdAppsSize = MutableStateFlow<Long>(0)
    private val _sdDocumentSize = MutableStateFlow<Long>(0)

    private val _isSdCardExist = MutableStateFlow<Boolean>(false)
    private val _sdCardFile = MutableStateFlow(File(""))


    //StateFlows
    val totalInternalSize: StateFlow<Long> get() = _totalInternalSize
    val totalSdSize: StateFlow<Long> get() = _totalSdSize
    val totalAvailableInternalSize: StateFlow<Long> get() = _totalAvailableInternalSize
    val totalAvailableSdSize: StateFlow<Long> get() = _totalAvailableSdSize

    val internalImageSize: StateFlow<Long> get() = _internalImageSize
    val internalAudioSize: StateFlow<Long> get() = _internalAudioSize
    val internalVideoSize: StateFlow<Long> get() = _internalVideoSize
    val internalZipsSize: StateFlow<Long> get() = _internalZipsSize
    val internalAppsSize: StateFlow<Long> get() = _internalAppsSize
    val internalDocumentSize: StateFlow<Long> get() = _internalDocumentSize
    val internalDownloadSize: StateFlow<Long> get() = _internalDownloadSize
    val sdImageSize: StateFlow<Long> get() = _sdImageSize
    val sdAudioSize: StateFlow<Long> get() = _sdAudioSize
    val sdVideoSize: StateFlow<Long> get() = _sdVideoSize
    val sdZipsSize: StateFlow<Long> get() = _sdZipsSize
    val sdAppsSize: StateFlow<Long> get() = _sdAppsSize
    val sdDocumentSize: StateFlow<Long> get() = _sdDocumentSize

    val isSdCardExist: StateFlow<Boolean> get() = _isSdCardExist
    val sdCardFile: StateFlow<File> get() = _sdCardFile

    suspend fun getTotalAndAvailableInternalMemorySize() {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        var blockSize: Long = 0
        var totalBlocks: Long = 0
        var availableBlocks: Long = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.blockSizeLong
            totalBlocks = stat.blockCountLong
            availableBlocks = stat.availableBlocksLong
        }
        _totalInternalSize.value = totalBlocks * blockSize
        _totalAvailableInternalSize.value = availableBlocks * blockSize
    }

    suspend fun getTotalAndAvailableSdMemorySize() {
        if (isSdCardExist.value) {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(sdCardFile.value.getPath())
            var blockSize: Long = 0
            var availableBlocks: Long = 0
            var totalBlocks: Long = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.blockSizeLong
                availableBlocks = stat.availableBlocksLong
                totalBlocks = stat.blockCountLong
            }
            _totalAvailableSdSize.value = blockSize * availableBlocks
            _totalSdSize.value = blockSize * totalBlocks
        }
    }


    private fun calculateFilesSizeDownload(file: File): ArrayList<File> {
        val arrayList = ArrayList<File>()
        val files = file.listFiles()

        if (files != null) {
            for (singleFile in files) {
                if (singleFile.isDirectory && !singleFile.isHidden) {
                    arrayList.addAll(calculateFilesSizeDownload(singleFile))
                } else {
                    _internalDownloadSize.value += singleFile.length()
                }
            }
        }
        return arrayList
    }

    suspend fun getDownLoadsFilesSize() {
        _internalDownloadSize.value = 0L
        val calculateFilesSizeDownload = calculateFilesSizeDownload(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            )
        )
    }

    suspend fun getInternalStorageFilesSize() {
        _internalImageSize.value = 0L
        _internalAudioSize.value = 0L
        _internalVideoSize.value = 0L
        _internalZipsSize.value = 0L
        _internalAppsSize.value = 0L
        _internalDocumentSize.value = 0L
        val calculateFilesSize = calculateFilesSize(Environment.getExternalStorageDirectory(), true)
    }

    suspend fun getSdStorageFilesSize() {
        _sdImageSize.value = 0L
        _sdAudioSize.value = 0L
        _sdVideoSize.value = 0L
        _sdZipsSize.value = 0L
        _sdAppsSize.value = 0L
        _sdDocumentSize.value = 0L
        val calculateFilesSize = calculateFilesSize(sdCardFile.value, false)
    }


    private fun calculateFilesSize(file: File, isInternal: Boolean): ArrayList<File> {
        val arrayList = ArrayList<File>()
        val files = file.listFiles()

        if (files != null) {
            for (singleFile in files) {
                if (singleFile.isDirectory && !singleFile.isHidden) {
                    arrayList.addAll(calculateFilesSize(singleFile, isInternal))
                } else {
                    if (singleFile.name.lowercase(Locale.getDefault())
                            .endsWith(".jpeg") || singleFile.name.lowercase(
                            Locale.getDefault()
                        ).endsWith(".jpg") || singleFile.name.lowercase(Locale.getDefault())
                            .endsWith(".png")
                    ) {
                        if (isInternal) _internalImageSize.value += singleFile.length() else {
                            _sdImageSize.value += singleFile.length()
                        }

                    } else if (singleFile.name.lowercase(Locale.getDefault()).endsWith(".mp4")) {
                        if (isInternal) _internalVideoSize.value += singleFile.length() else _sdVideoSize.value += singleFile.length()
                    } else if (singleFile.name.lowercase(Locale.getDefault())
                            .endsWith(".mp3") || singleFile.name.lowercase(
                            Locale.getDefault()
                        ).endsWith(".wav")
                    ) {
                        if (isInternal) _internalAudioSize.value += singleFile.length() else _sdAudioSize.value += singleFile.length()
                    } else if (singleFile.name.lowercase(Locale.getDefault())
                            .endsWith(".zip") || singleFile.name.lowercase(
                            Locale.getDefault()
                        ).endsWith(".zipx")
                    ) {
                        if (isInternal) _internalZipsSize.value += singleFile.length() else _sdZipsSize.value += singleFile.length()
                    } else if (singleFile.name.lowercase(Locale.getDefault()).endsWith(".apk")) {
                        if (isInternal) _internalAppsSize.value += singleFile.length() else _sdAppsSize.value += singleFile.length()
                    } else if (singleFile.name.lowercase(Locale.getDefault())
                            .endsWith(".doc") || singleFile.name.lowercase(
                            Locale.getDefault()
                        ).endsWith(".docx") || singleFile.name.lowercase(Locale.getDefault())
                            .endsWith(".xls") || singleFile.name.lowercase(
                            Locale.getDefault()
                        ).endsWith(".xlsx") || singleFile.name.lowercase(Locale.getDefault())
                            .endsWith(".txt") || singleFile.name.lowercase(
                            Locale.getDefault()
                        ).endsWith(".ppt") || singleFile.name.lowercase(Locale.getDefault())
                            .endsWith(".pdf")
                    ) {
                        if (isInternal) _internalDocumentSize.value += singleFile.length() else _sdDocumentSize.value += singleFile.length()
                    }
                }
            }
        }
        return arrayList
    }

    private fun getStorageDirectories(context: Context): Array<String> {
        val storageDirectories: Array<String>
        val rawSecondaryStoragesStr: String? = System.getenv("SECONDARY_STORAGE")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val results = mutableListOf<String>()
            val externalDirs = context.getExternalFilesDirs(null)
            for (file in externalDirs) {
                var path: String? = null
                try {
                    path = file.path.split("/Android")[0]
                } catch (e: Exception) {
                    e.printStackTrace()
                    path = null
                }
                if (path != null) {
                    if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Environment.isExternalStorageRemovable(
                            file
                        ))
                        || rawSecondaryStoragesStr != null && rawSecondaryStoragesStr.contains(path)
                    ) {
                        results.add(path)
                    }
                }
            }
            storageDirectories = results.toTypedArray()
        } else {
            val rv = mutableSetOf<String>()

            if (!rawSecondaryStoragesStr.isNullOrEmpty()) {
                val rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator)
                rv.addAll(rawSecondaryStorages)
            }
            storageDirectories = rv.toTypedArray()
        }
        return storageDirectories
    }

    suspend fun setSdStorageDetails() {
        val retArray: Array<String> =
            getStorageDirectories(context)
        if (retArray.size == 0) {
            _isSdCardExist.value = false
        } else {
            for (i in retArray.indices) {
                _isSdCardExist.value = true
                _sdCardFile.value = File(
                    File(retArray[i]).path.split("/Android".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()[0])
            }
        }
    }


}