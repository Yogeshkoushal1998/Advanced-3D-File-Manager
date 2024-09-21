package com.yk.mobile.advanced3dfilemanager.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import utility.FileHelper
import utility.Utils
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {
    @Singleton
    @Provides
    fun getUtils(@ApplicationContext context: Context): Utils {
        return Utils(context)
    }

    @Provides
    @Singleton
    fun provideOpenFileHelper(@ApplicationContext context: Context): FileHelper {
        return FileHelper(context)
    }
}