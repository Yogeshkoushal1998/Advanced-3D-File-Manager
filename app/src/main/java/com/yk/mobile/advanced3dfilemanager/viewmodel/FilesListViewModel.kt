package com.yk.mobile.advanced3dfilemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yk.mobile.advanced3dfilemanager.model.MediaModel
import com.yk.mobile.advanced3dfilemanager.repository.FilesListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import utility.FilesResult
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FilesListViewModel @Inject constructor(private val filesListRepository: FilesListRepository) :
    ViewModel() {

    val imageFilesLiveData: StateFlow<FilesResult<List<MediaModel>>> get() = filesListRepository.imageFilesLiveData

    val audiosFilesLiveData: StateFlow<FilesResult<List<File>>> get() = filesListRepository.audiosFilesLiveData

    val videosFilesLiveData: StateFlow<FilesResult<List<MediaModel>>> get() = filesListRepository.videosFilesLiveData

    val zipsFilesLiveData: StateFlow<FilesResult<List<File>>> get() = filesListRepository.zipsFilesLiveData

    val appsFilesLiveData: StateFlow<FilesResult<List<File>>> get() = filesListRepository.appsFilesLiveData

    val documentsFilesLiveData: StateFlow<FilesResult<List<File>>> get() = filesListRepository.documentsFilesLiveData

    val downloadFilesLiveData: StateFlow<FilesResult<List<File>>> get() = filesListRepository.downloadFilesLiveData

    val allFilesOfDirectoryOfSpecificType: StateFlow<FilesResult<List<File>>> get() = filesListRepository.allFilesOfDirectoryOfSpecificType


    fun getImageFilesList() {
        viewModelScope.launch(Dispatchers.IO) {
            filesListRepository.getImageFilesList()
        }
    }

    fun getAudiosFilesList() {
        viewModelScope.launch(Dispatchers.IO) {
            filesListRepository.getAudiosFilesList()
        }
    }

    fun getVideosFilesList() {
        viewModelScope.launch(Dispatchers.IO) {
            filesListRepository.getVideosFilesList()
        }
    }

    fun getZipsFilesList() {
        viewModelScope.launch(Dispatchers.IO) {
            filesListRepository.getZipsFilesList()
        }
    }

    fun getAppsFilesList() {
        viewModelScope.launch(Dispatchers.IO) {
            filesListRepository.getAppsFilesList()
        }
    }


    fun getDocumentsFilesList() {
        viewModelScope.launch(Dispatchers.IO) {
            filesListRepository.getDocumentsFilesList()
        }
    }

    fun getDownLoadsFilesList() {
        viewModelScope.launch(Dispatchers.IO) {
            filesListRepository.getDownLoadsFilesList()
        }
    }

    fun getAllFilesOfDirectoryOfSpecificType(file: File, fileType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            filesListRepository.getAllFilesOfDirectoryOfSpecificType(file, fileType)
        }
    }

}