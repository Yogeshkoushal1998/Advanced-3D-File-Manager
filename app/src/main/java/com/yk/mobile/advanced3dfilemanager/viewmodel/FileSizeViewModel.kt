package com.yk.mobile.advanced3dfilemanager.viewmodel

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yk.mobile.advanced3dfilemanager.repository.FilesSizeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileSizeViewModel @Inject constructor(private val filesSizeRepository: FilesSizeRepository) :
    ViewModel() {
    val totalInternalSize: StateFlow<Long> get() = filesSizeRepository.totalInternalSize
    val totalSdSize: StateFlow<Long> get() = filesSizeRepository.totalSdSize
    val totalAvailableInternalSize: StateFlow<Long> get() = filesSizeRepository.totalAvailableInternalSize
    val totalAvailableSdSize: StateFlow<Long> get() = filesSizeRepository.totalAvailableSdSize
    val internalImageSize: StateFlow<Long> get() = filesSizeRepository.internalImageSize
    val internalAudioSize: StateFlow<Long> get() = filesSizeRepository.internalAudioSize
    val internalVideoSize: StateFlow<Long> get() = filesSizeRepository.internalVideoSize
    val internalZipsSize: StateFlow<Long> get() = filesSizeRepository.internalZipsSize
    val internalAppsSize: StateFlow<Long> get() = filesSizeRepository.internalAppsSize
    val internalDocumentSize: StateFlow<Long> get() = filesSizeRepository.internalDocumentSize
    val internalDownloadSize: StateFlow<Long> get() = filesSizeRepository.internalDownloadSize
    val sdImageSize: StateFlow<Long> get() = filesSizeRepository.sdImageSize
    val sdAudioSize: StateFlow<Long> get() = filesSizeRepository.sdAudioSize
    val sdVideoSize: StateFlow<Long> get() = filesSizeRepository.sdVideoSize
    val sdZipsSize: StateFlow<Long> get() = filesSizeRepository.sdZipsSize
    val sdAppsSize: StateFlow<Long> get() = filesSizeRepository.sdAppsSize
    val sdDocumentSize: StateFlow<Long> get() = filesSizeRepository.sdDocumentSize
    val isSdCardExist:StateFlow<Boolean> get() = filesSizeRepository.isSdCardExist

    init {
        setSdStorageDetails()
    }

    private fun setSdStorageDetails() {
        viewModelScope.launch() {
            filesSizeRepository.setSdStorageDetails()
        }
    }

    fun getInternalStorageFilesSize() {
        viewModelScope.launch(Dispatchers.IO) {
            filesSizeRepository.getInternalStorageFilesSize()
        }
    }

    fun getSdStorageFilesSize() {
        viewModelScope.launch(Dispatchers.IO) {
            filesSizeRepository.getSdStorageFilesSize()
        }
    }

    fun getDownLoadsFilesSize() {
        viewModelScope.launch(Dispatchers.IO) {
            filesSizeRepository.getDownLoadsFilesSize()
        }
    }

    fun getTotalAndAvailableInternalMemorySize() {
        viewModelScope.launch(Dispatchers.IO) {
            filesSizeRepository.getTotalAndAvailableInternalMemorySize()
        }
    }

    fun getTotalAndAvailableSdMemorySize() {
        viewModelScope.launch(Dispatchers.IO) {
            filesSizeRepository.getTotalAndAvailableSdMemorySize()
        }
    }

}