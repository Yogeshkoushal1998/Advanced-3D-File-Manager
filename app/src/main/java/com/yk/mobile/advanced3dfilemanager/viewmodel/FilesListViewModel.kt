package com.yk.mobile.advanced3dfilemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yk.mobile.advanced3dfilemanager.repository.FilesListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilesListViewModel @Inject constructor(private val filesListRepository: FilesListRepository) :
    ViewModel() {
    val downloadFilesLiveData get() = filesListRepository.downloadFilesLiveData

    val imageFilesLiveData get() = filesListRepository.imageFilesLiveData

    val videosFilesLiveData get() = filesListRepository.videosFilesLiveData

    val documentsFilesLiveData get() = filesListRepository.documentsFilesLiveData


    fun getDownLoadsFilesList() {
        viewModelScope.launch(Dispatchers.IO) {
            filesListRepository.getDownLoadsFilesList()
        }
    }
    fun getImageFilesList() {
        viewModelScope.launch(Dispatchers.IO) {
            filesListRepository.getImageFilesList()
        }
    }
    fun getVideosFilesList() {
        viewModelScope.launch(Dispatchers.IO) {
            filesListRepository.getVideosFilesList()
        }
    }
    fun getDocumentsFilesList() {
        viewModelScope.launch(Dispatchers.IO) {
            filesListRepository.getDocumentsFilesList()
        }
    }





}