package com.yk.mobile.advanced3dfilemanager.di

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import utility.AppLogger
import utility.FileHelper
import utility.SecuredPreferenceHelper
import utility.Utils
import java.io.IOException
import java.security.GeneralSecurityException
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


    @Provides
    @Singleton
    fun SecuredPreferenceHelper(@ApplicationContext context: Context): SecuredPreferenceHelper {
        val filename="com.google.androidx.esp";
        var sharedPreferences: SharedPreferences = context
            .getSharedPreferences(filename, Context.MODE_PRIVATE)
        val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    filename,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } catch (e: Exception) {
                AppLogger.ex(e)
            }
        }
        return SecuredPreferenceHelper(context,sharedPreferences)
    }

}