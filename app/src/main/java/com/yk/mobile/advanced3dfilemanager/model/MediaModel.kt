package com.yk.mobile.advanced3dfilemanager.model

import java.io.File

data class MediaModel(
    val file: File,
    val firstFileOfDirectory: File,
    val count :Int)
