package com.yk.mobile.advanced3dfilemanager.repository

import android.content.Context
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.yk.mobile.advanced3dfilemanager.model.MediaModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import utility.Constants
import utility.FileHelper
import utility.FilesResult
import java.io.File
import javax.inject.Inject


class FilesListRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fileHelper: FileHelper, private val filesSizeRepository: FilesSizeRepository
) {

    val TAG = FilesListRepository::class.simpleName

    val sdCardFile: StateFlow<File> get() = filesSizeRepository.sdCardFile

    val isSdCardExist: StateFlow<Boolean> get() = filesSizeRepository.isSdCardExist

    private val _downloadFilesLiveData = MutableLiveData<FilesResult<List<File>>>()
    val downloadFilesLiveData get() = _downloadFilesLiveData

    private val _imageFilesLiveData = MutableLiveData<FilesResult<List<MediaModel>>>()
    val imageFilesLiveData get() = _imageFilesLiveData

    private val _documentsFilesLiveData = MutableLiveData<FilesResult<List<File>>>()
    val documentsFilesLiveData get() = _documentsFilesLiveData

    private val _videosFilesLiveData = MutableLiveData<FilesResult<List<MediaModel>>>()
    val videosFilesLiveData get() = _videosFilesLiveData

    private fun getDownloadsFilesPath(file: File): ArrayList<File> {
        val arrayList = ArrayList<File>()
        val files = file.listFiles()
        if (files != null) {
            for (singleFile in files) {
                if (singleFile.isDirectory && !singleFile.isHidden) {
                    arrayList.addAll(getDownloadsFilesPath(singleFile))
                } else {
                    arrayList.add(singleFile)
                }
            }
        }
        return arrayList
    }


    suspend fun getDownLoadsFilesList() {
        _downloadFilesLiveData.postValue(FilesResult.Loading())
        try {
            val fileArrayList = getDownloadsFilesPath(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                )
            )
            _downloadFilesLiveData.postValue(FilesResult.Success(fileArrayList))
        } catch (e: Exception) {
            _downloadFilesLiveData.postValue(FilesResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getImageFilesList() {
        _imageFilesLiveData.postValue(FilesResult.Loading())
        try {
            runBlocking {
                // Launching two calculations concurrently on IO dispatcher
                val result = withContext(Dispatchers.IO) {
                    val internalListDef = async {
                        getImagesFoldersList(
                            Environment.getExternalStorageDirectory(),
                            Constants.VIDEO
                        )
                    }
                    val sdListDef = async {
                        getImagesFoldersList(sdCardFile.value, Constants.VIDEO)
                    }
                    val internalList = internalListDef.await()
                    val sdList = sdListDef.await()
                    internalList.addAll(sdList)
                    _imageFilesLiveData.postValue(FilesResult.Success(internalList))
                }
            }

        } catch (e: Exception) {
            _imageFilesLiveData.postValue(FilesResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getVideosFilesList() {
        _videosFilesLiveData.postValue(FilesResult.Loading())
        try {
            runBlocking {
                // Launching two calculations concurrently on IO dispatcher
                val result = withContext(Dispatchers.IO) {
                    val internalListDef = async {
                        getImagesFoldersList(
                            Environment.getExternalStorageDirectory(),
                            Constants.VIDEO
                        )
                    }
                    val sdListDef = async {
                        getImagesFoldersList(sdCardFile.value, Constants.VIDEO)
                    }
                    val internalList = internalListDef.await()
                    val sdList = sdListDef.await()
                    internalList.addAll(sdList)
                    _videosFilesLiveData.postValue(FilesResult.Success(internalList))
                }
            }

        } catch (e: Exception) {
            _videosFilesLiveData.postValue(FilesResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getDocumentsFilesList() {
        _documentsFilesLiveData.postValue(FilesResult.Loading())
        try {
            val fileArrayList = getDownloadsFilesPath(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                )
            )
            _documentsFilesLiveData.postValue(FilesResult.Success(fileArrayList))
        } catch (e: Exception) {
            _documentsFilesLiveData.postValue(FilesResult.Error("Something Went Wrong"))
        }
    }


    private fun getImagesFoldersList(file: File, fileType: String): ArrayList<MediaModel> {
        val fileArrayList = ArrayList<MediaModel>()
        val files = file.listFiles()
        if (files != null) {
            for (file1 in files) {
                if (file1.isDirectory && !file1.isHidden && file1.name != "Telegram") {
                    val directory = isDirectory(file1, fileType)
                    if (directory != null) {
                        val anInt = getInt(directory, fileType)
                        //                        if (anInt>1){
                        fileArrayList.add(MediaModel(directory, anInt))
                        //                        }
                    }
                }
            }
        }
        return fileArrayList
    }

    private fun getInt(file: File, fileType: String): Int {
        var count = 0
        val files = file.listFiles()
        for (file1 in files) {
            if (fileHelper.getFileType(file1).equals(fileType)) {
                count += 1
            }
        }
        return count
    }

    private fun isDirectory(file: File, fileType: String): File? {
        val files = file.listFiles()

        if (files != null) {
            for (file1 in files) {
                if (file1.isDirectory && !file1.isHidden) {
                    val dir = isDir(file1, fileType)
                    if (dir != null) {
                        return file1
                    }
                } else {
                    if (fileHelper.getFileType(file1).equals(fileType)) {
                        return file
                    }
                }
            }
        }
        return null
    }

    private fun isDir(file: File, fileType: String): File? {
        val files = file.listFiles()

        if (files != null) {
            for (file1 in files) {
                if (file1.isDirectory && !file1.isHidden) {
                    isDir(file1, fileType)
                } else {
                    if (fileHelper.getFileType(file1).equals(fileType)
                    ) {
                        return file1
                    }
                }
            }
        }
        return null
    }


}