package com.yk.mobile.advanced3dfilemanager.repository

import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.yk.mobile.advanced3dfilemanager.model.MediaModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import utility.AppLogger
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

    private val _downloadFilesLiveData =
        MutableStateFlow<FilesResult<List<File>>>(FilesResult.Loading())


    val downloadFilesLiveData: StateFlow<FilesResult<List<File>>> get() = _downloadFilesLiveData

    private val _imageFilesLiveData =
        MutableStateFlow<FilesResult<List<MediaModel>>>(FilesResult.Loading())
    val imageFilesLiveData: StateFlow<FilesResult<List<MediaModel>>> get() = _imageFilesLiveData

    private val _documentsFilesLiveData =
        MutableStateFlow<FilesResult<List<File>>>(FilesResult.Loading())
    val documentsFilesLiveData: StateFlow<FilesResult<List<File>>> get() = _documentsFilesLiveData

    private val _videosFilesLiveData =
        MutableStateFlow<FilesResult<List<MediaModel>>>(FilesResult.Loading())
    val videosFilesLiveData: StateFlow<FilesResult<List<MediaModel>>> get() = _videosFilesLiveData

    private val _audiosFilesLiveData =
        MutableStateFlow<FilesResult<List<File>>>(FilesResult.Loading())
    val audiosFilesLiveData: StateFlow<FilesResult<List<File>>> get() = _audiosFilesLiveData

    private val _zipsFilesLiveData =
        MutableStateFlow<FilesResult<List<File>>>(FilesResult.Loading())
    val zipsFilesLiveData: StateFlow<FilesResult<List<File>>> get() = _zipsFilesLiveData

    private val _appsFilesLiveData =
        MutableStateFlow<FilesResult<List<File>>>(FilesResult.Loading())
    val appsFilesLiveData: StateFlow<FilesResult<List<File>>> get() = _appsFilesLiveData

    private val _allFilesOfDirectoryOfSpecificType =
        MutableStateFlow<FilesResult<List<File>>>(FilesResult.Loading())

    val allFilesOfDirectoryOfSpecificType: StateFlow<FilesResult<List<File>>> get() = _allFilesOfDirectoryOfSpecificType

    private val _allFilesOfDirectory =
        MutableStateFlow<FilesResult<List<MediaModel>>>(FilesResult.Loading())
    val allFilesOfDirectory: StateFlow<FilesResult<List<MediaModel>>> get() = _allFilesOfDirectory


    suspend fun getImageFilesList() {
        _imageFilesLiveData.value = (FilesResult.Loading())
        try {
            runBlocking {
                // Launching two calculations concurrently on IO dispatcher
                val result = withContext(Dispatchers.IO) {
                    val internalListDef = async {
                        getImagesFoldersList(
                            Environment.getExternalStorageDirectory(),
                            Constants.PICTURES
                        )
                    }
                    val sdListDef = async {
                        getImagesFoldersList(sdCardFile.value, Constants.PICTURES)
                    }
                    val internalList = internalListDef.await()
                    val sdList = sdListDef.await()
                    internalList.addAll(sdList)
                    _imageFilesLiveData.value = (FilesResult.Success(internalList))
                }
            }

        } catch (e: Exception) {
            _imageFilesLiveData.value = (FilesResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getAudiosFilesList() {
        _audiosFilesLiveData.value = (FilesResult.Loading())
        try {
            val fileArrayList =
                getAllFilesOnBasisOfType(Environment.getExternalStorageDirectory(), Constants.AUDIO)
            _audiosFilesLiveData.value = (FilesResult.Success(fileArrayList))
        } catch (e: Exception) {
            _audiosFilesLiveData.value = (FilesResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getVideosFilesList() {
        _videosFilesLiveData.value = (FilesResult.Loading())
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
                    _videosFilesLiveData.value = (FilesResult.Success(internalList))
                }
            }

        } catch (e: Exception) {
            _videosFilesLiveData.value = (FilesResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getZipsFilesList() {
        _zipsFilesLiveData.value = (FilesResult.Loading())
        try {
            val fileArrayList =
                getAllFilesOnBasisOfType(Environment.getExternalStorageDirectory(), Constants.ZIP)
            _zipsFilesLiveData.value = (FilesResult.Success(fileArrayList))
        } catch (e: Exception) {
            _zipsFilesLiveData.value = (FilesResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getAppsFilesList() {
        _appsFilesLiveData.value = (FilesResult.Loading())
        try {
            val fileArrayList =
                getAllFilesOnBasisOfType(Environment.getExternalStorageDirectory(), Constants.APP)
            _appsFilesLiveData.value = (FilesResult.Success(fileArrayList))
        } catch (e: Exception) {
            _appsFilesLiveData.value = (FilesResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getDocumentsFilesList() {
        _documentsFilesLiveData.value = (FilesResult.Loading())
        try {
            val fileArrayList = getAllFilesOnBasisOfType(
                Environment.getExternalStorageDirectory(),
                Constants.DOCUMENT
            )
            _documentsFilesLiveData.value = (FilesResult.Success(fileArrayList))
        } catch (e: Exception) {
            _documentsFilesLiveData.value = (FilesResult.Error("Something Went Wrong"))
        }
    }

    private fun listWhatsAppMediaFilesByDirectory(file:File): Map<String, List<File>> {
        val projection = arrayOf(
/*
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
*/
                    MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATA
        )


        // Selection to filter WhatsApp media files, including hidden ones
        val selection = MediaStore.Files.FileColumns.DATA + " LIKE ?"
        val selectionArgs = arrayOf("%${file.path}%")

        val cursor = context.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            null
        )

        // Map to group files by their directories
        val mediaFilesByDirectory = mutableMapOf<String, MutableList<File>>()

        cursor?.use {
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)

            while (it.moveToNext()) {
                val data = it.getString(dataColumn)  // File path

                // Create File object
                val file = File(data)

                // Get parent directory
                val parentDirectory = file.parentFile?.absolutePath ?: "Unknown Directory"

                // Add file to its directory group
                mediaFilesByDirectory.computeIfAbsent(parentDirectory) { mutableListOf() }.add(file)
            }
        }

        // Log for debugging
        mediaFilesByDirectory.forEach { (directory, files) ->
            AppLogger.d("WhatsAppMedia", "Directory: $directory")
            files.forEach { file ->
                AppLogger.d("WhatsAppMedia", "  File: ${file.name}, Path: ${file.absolutePath}, Hidden: ${file.isHidden}")
            }
        }

        return mediaFilesByDirectory
    }




    suspend fun getDownLoadsFilesList() {
        _downloadFilesLiveData.value = (FilesResult.Loading())
        try {
            val fileArrayList = getAllFilesOnBasisOfType(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                ), Constants.ALL_TYPES_FILE
            )
            _downloadFilesLiveData.value = (FilesResult.Success(fileArrayList))
        } catch (e: Exception) {
            _downloadFilesLiveData.value = (FilesResult.Error("Something Went Wrong"))
        }
    }


    suspend fun getAllFilesOfDirectoryOfSpecificType(file: File, fileType: String) {
        _allFilesOfDirectoryOfSpecificType.value = (FilesResult.Loading())
        try {
            val fileArrayList =
                getAllFilesOnBasisOfType(file, fileType)
            _allFilesOfDirectoryOfSpecificType.value = (FilesResult.Success(fileArrayList))
        } catch (e: Exception) {
            _allFilesOfDirectoryOfSpecificType.value = (FilesResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getAllFilesOfDirectory(file: File) {
        _allFilesOfDirectory.value = (FilesResult.Loading())
        try {
            val fileArrayList =
                getAllFilesOfTypeDirectory(file)
            _allFilesOfDirectory.value = (FilesResult.Success(fileArrayList))
        } catch (e: Exception) {
            _allFilesOfDirectory.value = (FilesResult.Error("Something Went Wrong"))
        }
    }

    private fun getAllFilesOfTypeDirectory(file: File): List<MediaModel> {
        val fileArrayList = ArrayList<MediaModel>()
       /* val fileMap = listWhatsAppMediaFilesByDirectory(file)
        for (map in fileMap){
            AppLogger.d(TAG , "Directory Path${map.key}")
        }
        for (map in fileMap){
            fileArrayList.add(MediaModel(File(map.key), null, map.value.size))
        }*/
        val files = file.listFiles()
        if (files != null) {
            for (singleFile in files) {
                if (!singleFile.isHidden) {
                    if (!singleFile.isDirectory) {
                        fileArrayList.add(MediaModel(singleFile, singleFile,0))
                    } else {
                        fileArrayList.add(MediaModel(singleFile, null, getIntCount(singleFile)))
                    }
                }
            }
        }
        return fileArrayList
    }

    private fun getImagesFoldersList(file: File, fileType: String): ArrayList<MediaModel> {
        val fileArrayList = ArrayList<MediaModel>()
        val files = file.listFiles()
        if (files != null) {
            for (file1 in files) {
                if (file1.isDirectory && !file1.isHidden && file1.name != "Telegram") {
                    val directory = isDirectory(file1, fileType)
                    if (directory != null) {
                        val listOfFiles = getAllFilesOnBasisOfType(directory, fileType)
                        val firstFileOfDirectory = getFirstFileOfDirectory(directory, fileType)
                        fileArrayList.add(
                            MediaModel(
                                directory,
                                firstFileOfDirectory,
                                listOfFiles.size
                            )
                        )
                    }
                }
            }
        }
        return fileArrayList
    }

    private fun getIntCount(file: File): Int {
        var count = 0
        val files = file.listFiles()
        if (files != null) {
            for (singleFile in files) {
                if (!singleFile.isHidden) {
                    count += 1
                }
            }
        }
        return count
    }

    private fun getFirstFileOfDirectory(file: File, fileType: String): File {
        val files = file.listFiles()
        for (file1 in files) {
            if (fileHelper.getFileType(file1).equals(fileType)) {
                return file1
            }
        }
        return file
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

    private fun getAllFilesOnBasisOfType(file: File, fileType: String): ArrayList<File> {
        val fileArrayList = ArrayList<File>()
        val files = file.listFiles()
        if (files != null) {
            for (singleFile in files) {
                if (singleFile.isDirectory && !singleFile.isHidden && singleFile.name != "Telegram") {
                    fileArrayList.addAll(getAllFilesOnBasisOfType(singleFile, fileType))
                } else {
                    if (!singleFile.isHidden && fileType.equals(Constants.ALL_TYPES_FILE)) {
                        fileArrayList.add(singleFile)
                    } else if (!singleFile.isHidden && fileHelper.getFileType(singleFile)
                            .equals(fileType)
                    ) {
                        fileArrayList.add(singleFile)
                    }
                }
            }
        }
        return fileArrayList
    }


}