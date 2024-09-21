package com.yk.mobile.advanced3dfilemanager.di

import android.content.Context
import com.yk.mobile.advanced3dfilemanager.repository.FilesListRepository
import com.yk.mobile.advanced3dfilemanager.repository.FilesSizeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import utility.FileHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideFilesSizeRepository(@ApplicationContext context: Context): FilesSizeRepository {
        return FilesSizeRepository(context)
    }

    @Provides
    @Singleton
    fun provideFilesListRepository(
        @ApplicationContext context: Context,
        fileHelper: FileHelper,
        filesSizeRepository: FilesSizeRepository
    ): FilesListRepository {
        return FilesListRepository(context, fileHelper, filesSizeRepository)
    }


}